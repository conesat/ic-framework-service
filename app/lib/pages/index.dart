import 'dart:ui';

import 'package:app/common/global.dart';
import 'package:app/common/utils/ws.dart';
import 'package:app/pages/chat_index.dart';
import 'package:app/pages/home.dart';
import 'package:app/pages/mine.dart';
import 'package:app/pages/work.dart';
import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:provider/provider.dart';

import '../common/theme/app_theme.dart';

class IndexPage extends StatefulWidget {
  const IndexPage({super.key});

  @override
  State<IndexPage> createState() => _IndexPageState();
}

class _IndexPageState extends State<IndexPage> {
  late ThemeData themeData;
  late DataModel _dataModel;
  final PageController _pageController = PageController();

  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    WS.connect();
  }

  @override
  Widget build(BuildContext context) {
    themeData = Theme.of(context);
    GlobalData.context = context;
    double bottomPadding = MediaQuery.of(context).padding.bottom;
    return ChangeNotifierProvider(
        create: (context) => DataModel(),
        builder: (context, child) {
          _dataModel = context.read<DataModel>();
          return Scaffold(
              resizeToAvoidBottomInset: false,
              body: Stack(
                children: [
                  Positioned(
                      top: 0,
                      left: 0,
                      right: 0,
                      bottom: 0,
                      child: PageView(
                        physics: const NeverScrollableScrollPhysics(),
                        controller: _pageController,
                        children: const [
                          HomePage(),
                          WorkPage(),
                          ChatIndexPage(),
                          MyPage(),
                        ],
                      )),
                  Positioned(
                    left: 0,
                    right: 0,
                    bottom: 0,
                    child: ClipRRect(
                      borderRadius: const BorderRadius.only(
                          topLeft: Radius.circular(AppTheme.borderRadius * 2),
                          topRight: Radius.circular(AppTheme.borderRadius * 2)),
                      child: BackdropFilter(
                        filter: ImageFilter.blur(sigmaX: 18, sigmaY: 18),
                        child: Container(
                          width: double.maxFinite,
                          padding: EdgeInsets.only(
                              bottom: bottomPadding / 2, top: 4),
                          decoration: BoxDecoration(
                            color: themeData.highlightColor.withAlpha(10),
                          ),
                          child: Selector<DataModel, int>(
                              selector: (content, tab) => _dataModel.tab,
                              builder: (context, tab, child) {
                                return Row(
                                  mainAxisSize: MainAxisSize.max,
                                  children: [
                                    buildTabItem(0, "首页", "home"),
                                    buildTabItem(1, "工作", "work"),
                                    buildTabItem(2, "消息", "msg"),
                                    buildTabItem(3, "我的", "my"),
                                  ],
                                );
                              }),
                        ),
                      ),
                    ),
                  ),
                ],
              ));
        });
  }

  Expanded buildTabItem(index, title, pic) {
    return Expanded(
        flex: 1,
        child: GestureDetector(
            onTap: () {
              _dataModel.changeTab(index);
              _pageController.jumpToPage(index);
            },
            child: Container(
                color: Colors.transparent,
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.center,
                  children: [
                    SizedBox(
                      width: 36,
                      height: 36,
                      child: SvgPicture.asset(
                          "assets/svg/icons/$pic${(index == _dataModel.tab ? "_1" : "_0")}.svg"),
                    ),
                    Text(
                      title,
                      style: TextStyle(
                          fontSize: 12,
                          color: index == _dataModel.tab
                              ? AppTheme.primaryColor
                              : themeData.secondaryHeaderColor),
                    )
                  ],
                ))));
  }
}

class DataModel extends ChangeNotifier {
  bool disposed = false;
  int tab = 0;

  void changeTab(val) {
    if (disposed) {
      return;
    }
    tab = val;
    notifyListeners();
  }
}
