import 'dart:async';

import 'package:app/common/api/sys/api_ad.dart';
import 'package:app/common/global.dart';
import 'package:app/components/controller/video_payer_controller.dart';
import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/material.dart';
import 'package:flutter_svg/svg.dart';
import 'package:provider/provider.dart';

import '../common/entity/ad.dart';
import '../components/cache_video_player.dart';

class WelcomePage extends StatefulWidget {
  final VoidCallback close;

  const WelcomePage(this.close, {super.key});

  @override
  State<WelcomePage> createState() => _WelcomePageState();
}

class _WelcomePageState extends State<WelcomePage> {
  int _waitTime = 6;
  Ad _ad = Ad();
  late DataModel _dataModel;
  final VideoPlayController _videoPlayController = VideoPlayController();

  @override
  void initState() {
    super.initState();
    ApiAd.ad(success: (r) {
      _ad = Ad.fromJson(r);
      if (_ad.appAdFileUrl == null) {
        widget.close();
        return;
      }
      setState(() {});
      if (_ad.appAdType!.code == 3) {
        Timer.periodic(const Duration(seconds: 1), (timer) {
          if (_waitTime > 0) {
            _dataModel.changeTime(_waitTime--);
          } else {
            timer.cancel();
            widget.close();
          }
        });
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return ChangeNotifierProvider(
        create: (context) => DataModel(),
        builder: (context, child) {
          _dataModel = context.read<DataModel>();
          return Scaffold(
            body: Stack(
              children: [
                Positioned(
                  left: 0,
                  right: 0,
                  top: 0,
                  bottom: 0,
                  child: Builder(builder: (context) {
                    if (_ad.appAdFileUrl != null && _ad.appAdType!.code == 3) {
                      return CachedNetworkImage(
                        cacheKey: "start-ad",
                        imageUrl: _ad.appAdFileUrl!,
                        fit: BoxFit.cover,
                        errorWidget: (context, url, error) =>
                            const Icon(Icons.error),
                        fadeInDuration: const Duration(milliseconds: 250),
                      );
                    } else if (_ad.appAdFileUrl != null &&
                        _ad.appAdType!.code == 1) {
                      return CachedVideoPlayer(
                        videoUrl: _ad.appAdFileUrl!,
                        onVideoChanged: (int position, int duration) {
                          _dataModel.changeTime(duration - position);
                          _dataModel.changeCanClose(position > 5);
                          if (duration - position == 0) {
                            widget.close();
                            _videoPlayController.stop!();
                          }
                        },
                        videoPlayController: _videoPlayController,
                      );
                    } else {
                      return Center(
                        child: Container(
                          padding: const EdgeInsets.all(10),
                          child: SizedBox(
                              width: 60,
                              height: 60,
                              child: SvgPicture.asset('assets/svg/logo.svg')),
                        ),
                      );
                    }
                  }),
                ),
                // Replace with your actual welcome page content
                Selector<DataModel, int>(
                    selector: (content, time) => _dataModel.time,
                    builder: (context, time, child) {
                      return Positioned(
                        top: 60,
                        right: 20,
                        child: Offstage(
                          offstage: _ad.appAdFileUrl == null || time <= 0,
                          child: Container(
                            padding: const EdgeInsets.symmetric(
                                horizontal: 10, vertical: 5),
                            decoration: BoxDecoration(
                              color: GlobalData.themeData!.cardColor
                                  .withOpacity(0.5),
                              borderRadius: BorderRadius.circular(15),
                            ),
                            child: GestureDetector(
                              onTap: () {
                                if (_dataModel.canClose) {
                                  widget.close();
                                }
                              },
                              child: Text(
                                _dataModel.canClose
                                    ? '${time}s 跳过'
                                    : '${time}s',
                                style: const TextStyle(fontSize: 12),
                              ),
                            ),
                          ),
                        ),
                      );
                    }),
              ],
            ),
          );
        });
  }
}

class DataModel extends ChangeNotifier {
  bool disposed = false;
  bool canClose = false;
  int time = -1;

  void changeTime(val) {
    if (disposed) {
      return;
    }
    time = val;
    notifyListeners();
  }

  void changeCanClose(val) {
    if (disposed) {
      return;
    }
    canClose = val;
    notifyListeners();
  }
}
