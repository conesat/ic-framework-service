import 'package:flutter/material.dart';
import 'package:flutter_cache_manager/flutter_cache_manager.dart';
import 'package:video_player/video_player.dart';

import 'controller/video_payer_controller.dart';

class CachedVideoPlayer extends StatefulWidget {
  final String videoUrl;
  final bool loop;
  final VideoPlayController videoPlayController;
  final Function(int position, int duration) onVideoChanged;

  const CachedVideoPlayer(
      {super.key,
      this.loop = false,
      required this.videoUrl,
      required this.onVideoChanged,
      required this.videoPlayController});

  @override
  _CachedVideoPlayerState createState() => _CachedVideoPlayerState();
}

class _CachedVideoPlayerState extends State<CachedVideoPlayer> {
  late VideoPlayerController _controller;
  bool _isInitialized = false;
  bool _dispose = false;

  @override
  void initState() {
    super.initState();
    widget.videoPlayController.stop = () {
      if (_controller.value.isPlaying) {
        _controller.pause();
      }
    };
    _initializeVideo();
  }

  Future<void> _initializeVideo() async {
    if (_dispose) {
      return;
    }
    final fileInfo =
        await DefaultCacheManager().getFileFromCache(widget.videoUrl);
    if (fileInfo != null && fileInfo.file.existsSync()) {
      _controller = VideoPlayerController.file(fileInfo.file);
    } else {
      DefaultCacheManager().getSingleFile(widget.videoUrl);
      _controller =
          VideoPlayerController.networkUrl(Uri.parse(widget.videoUrl));
    }
    await _controller.initialize();
    if (_dispose) {
      return;
    }
    setState(() {
      _isInitialized = true;
    });

    _controller.setLooping(widget.loop);
    widget.onVideoChanged(0, _controller.value.duration.inSeconds);
    _controller.addListener(_onVideoChanged);
    _controller.play();
  }

  int beforePosition = 0;

  void _onVideoChanged() {
    if (_dispose) {
      return;
    }
    final position = _controller.value.position.inSeconds;
    final duration = _controller.value.duration.inSeconds;
    if (position != beforePosition) {
      widget.onVideoChanged(position, duration);
      beforePosition = position;
    }
  }

  @override
  void dispose() {
    _dispose = true;
    if (_controller.value.isInitialized) {
      _controller.dispose();
    }
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return _isInitialized
        ? SizedBox.expand(
            //尽量大的去填充父布局
            child: FittedBox(
            //使用FittedBox设置BoxFit.cover使子控件等比占据父容器
            fit: BoxFit.cover,
            child: SizedBox(
                width: _controller.value.size.width,
                height: _controller.value.size.height,
                child: VideoPlayer(_controller)),
          ))
        : const Center(child: CircularProgressIndicator());
  }
}
