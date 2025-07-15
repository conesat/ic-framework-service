import 'package:app/common/api/query.dart';
import 'package:app/common/api/sys/api_server_order.dart';
import 'package:app/common/entity/server_order.dart';
import 'package:app/common/global.dart';
import 'package:app/components/task_item.dart';
import 'package:app/components/user_avatar.dart';
import 'package:app/pages/setting.dart';
import 'package:flutter/material.dart';
import 'package:flutter_echarts/flutter_echarts.dart';
import 'package:provider/provider.dart';

class MyPage extends StatefulWidget {
  const MyPage({super.key});

  @override
  State<MyPage> createState() => _MyPageState();
}

class _MyPageState extends State<MyPage> {
  late DataModel _dataModel;
  final ScrollController _scrollController = ScrollController();

  List<ServerOrder> serverOrders = [];
  Query serverOrderQuery = Query(pageIndex: 1, pageSize: 30);
  @override
  void initState() {
    super.initState();
    _scrollController.addListener(() {
      if (_scrollController.offset > 0 && _scrollController.offset < 200) {
        _dataModel.changeScrollOpacity(1.0 - _scrollController.offset / 200);
      } else if (_scrollController.offset <= 0) {
        _dataModel.changeScrollOpacity(1.0);
      } else {
        _dataModel.changeScrollOpacity(0.0);
      }
    });
    getData();
  }

  @override
  Widget build(BuildContext context) {
    return ChangeNotifierProvider(
        create: (context) => DataModel(),
        builder: (context, child) {
          _dataModel = context.read<DataModel>();
          return Scaffold(
            body: Stack(children: [
              Positioned(
                left: 0,
                top: 0,
                height: 600,
                right: 0,
                child: Selector<DataModel, double>(
                    selector: (content, scrollOpacity) =>
                        _dataModel.scrollOpacity,
                    builder: (context, scrollOpacity, child) {
                      return Opacity(
                        opacity: scrollOpacity,
                        child: Image.asset(
                          'assets/imgs/bg_work.png',
                          fit: BoxFit.fill,
                          width: double.infinity,
                          height: 600,
                        ),
                      );
                    }),
              ),
              Positioned(
                left: 0,
                top: 0,
                right: 0,
                child: Container(
                  height: 600,
                  decoration: BoxDecoration(
                    gradient: LinearGradient(
                      begin: Alignment.topCenter,
                      end: Alignment.bottomCenter,
                      colors: [
                        GlobalData.themeData!.primaryColor.withAlpha(0),
                        GlobalData.themeData!.primaryColor.withAlpha(76),
                        GlobalData.themeData!.primaryColor,
                      ],
                    ),
                  ),
                ),
              ),
              Positioned(
                left: 0,
                top: 0,
                right: 0,
                bottom: 0,
                child: SingleChildScrollView(
                    controller: _scrollController,
                    child: Padding(
                      padding: const EdgeInsets.only(bottom: 80, top: 70),
                      child: Column(children: [
                        Padding(
                          padding: const EdgeInsets.symmetric(horizontal: 14),
                          child: buildTop(),
                        ),
                        buildCard(),
                        const SizedBox(height: 30),
                        Container(
                          padding: const EdgeInsets.symmetric(
                              horizontal: 14, vertical: 20),
                          decoration: BoxDecoration(
                            color: GlobalData.themeData!.primaryColor
                                .withAlpha(102),
                            borderRadius: const BorderRadius.only(
                                topLeft: Radius.circular(30),
                                topRight: Radius.circular(30)),
                          ),
                          child: SizedBox(
                            width: double.maxFinite,
                            child: Column(
                              children: [
                                Padding(
                                  padding: const EdgeInsets.all(8.0),
                                  child: Row(
                                    children: [
                                      const Expanded(
                                        flex: 1,
                                        child: Text(
                                          "最近任务",
                                          textAlign: TextAlign.left,
                                          style: TextStyle(
                                              fontWeight: FontWeight.bold,
                                              fontSize: 16),
                                        ),
                                      ),
                                      Row(
                                        children: [
                                          Text(
                                            "查看更多 ",
                                            style: TextStyle(
                                                fontSize: 14,
                                                color: GlobalData.themeData!
                                                    .secondaryHeaderColor),
                                          ),
                                          Icon(Icons.arrow_forward_ios,
                                              size: 14,
                                              color: GlobalData.themeData!
                                                  .secondaryHeaderColor)
                                        ],
                                      )
                                    ],
                                  ),
                                ),
                                const SizedBox(height: 20),
                                SizedBox(
                                  width: double.maxFinite,
                                  child: SingleChildScrollView(
                                    scrollDirection: Axis.horizontal,
                                    child: Row(
                                      mainAxisSize: MainAxisSize.min,
                                      mainAxisAlignment:
                                          MainAxisAlignment.start,
                                      children: [
                                        Container(
                                          padding: const EdgeInsets.symmetric(
                                              vertical: 4, horizontal: 14),
                                          decoration: BoxDecoration(
                                            color: GlobalData
                                                .themeData!.cardColor
                                                .withAlpha(153),
                                            borderRadius:
                                                BorderRadius.circular(30),
                                          ),
                                          child: const Text("客房：101"),
                                        ),
                                      ],
                                    ),
                                  ),
                                ),
                                const SizedBox(height: 10),
                                Selector<DataModel, int>(
                                    selector: (content, serverOrder) =>
                                        _dataModel.serverOrder,
                                    builder: (context, serverOrder, child) {
                                      if (serverOrder == 0) {
                                        return const Padding(
                                          padding: EdgeInsets.all(20.0),
                                          child: SizedBox(
                                            height: 30,
                                            width: 30,
                                            child: CircularProgressIndicator(),
                                          ),
                                        );
                                      }
                                      return ListView.builder(
                                          padding: const EdgeInsets.all(0),
                                          shrinkWrap: true,
                                          physics:
                                              const NeverScrollableScrollPhysics(),
                                          itemCount: serverOrders.length,
                                          itemBuilder: (content, index) {
                                            return TaskItem(
                                                serverOrders[index]);
                                          });
                                    })
                              ],
                            ),
                          ),
                        ),
                      ]),
                    )),
              ),
            ]),
          );
        });
  }

  Row buildTop() {
    return Row(
      children: [
        Container(
          decoration: BoxDecoration(
            borderRadius: BorderRadius.circular(50),
            boxShadow: [
              BoxShadow(
                color: Colors.black.withAlpha(26),
                blurRadius: 10,
                offset: const Offset(-10, 10),
              ),
            ],
          ),
          child: const UserAvatar(
            url: "https://tdesign.gtimg.com/site/avatar.jpg",
            width: 50,
            height: 50,
          ),
        ),
        const SizedBox(width: 10),
        Expanded(
          flex: 1,
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              const Text(
                "张三",
                style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
              ),
              const SizedBox(height: 2),
              Text(
                "综合管理部：管理员",
                style: TextStyle(
                    color: GlobalData.themeData!.secondaryHeaderColor),
              ),
            ],
          ),
        ),
        const SizedBox(width: 10),
        GestureDetector(
          onTap: () {
            Navigator.push(
                context,
                MaterialPageRoute(
                    builder: (context) => const SettingPage())).then((r) {});
          },
          child: const Icon(
            Icons.settings,
            size: 30,
          ),
        )
      ],
    );
  }

  Row buildCard() {
    return Row(crossAxisAlignment: CrossAxisAlignment.start, children: [
      Expanded(
          flex: 4,
          child: AspectRatio(
            aspectRatio: 0.6,
            child: Padding(
              padding: const EdgeInsets.only(left: 20),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                mainAxisSize: MainAxisSize.max,
                children: [
                  const SizedBox(
                    height: 30,
                  ),
                  Column(
                    mainAxisSize: MainAxisSize.min,
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      const Text(
                        "HI，任务完成啦！",
                        style: TextStyle(
                            fontSize: 16, fontWeight: FontWeight.bold),
                      ),
                      Text(
                        "今日完成任务6项目",
                        style: TextStyle(
                            fontSize: 14,
                            color: GlobalData.themeData!.secondaryHeaderColor),
                      ),
                    ],
                  ),
                  Expanded(
                      flex: 1,
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          Row(
                            children: [
                              const Text(
                                "99",
                                style: TextStyle(
                                    fontSize: 22, fontWeight: FontWeight.bold),
                              ),
                              const SizedBox(width: 2),
                              Padding(
                                padding: const EdgeInsets.only(top: 6),
                                child: Text(
                                  "项",
                                  style: TextStyle(
                                      fontSize: 14,
                                      color: GlobalData
                                          .themeData!.secondaryHeaderColor),
                                ),
                              ),
                            ],
                          ),
                          Text(
                            "本月完成任务",
                            style: TextStyle(
                                fontSize: 14,
                                color:
                                    GlobalData.themeData!.secondaryHeaderColor),
                          ),
                          const SizedBox(height: 20),
                          Row(
                            children: <Widget>[
                              const Text(
                                "8",
                                style: TextStyle(
                                    fontSize: 22, fontWeight: FontWeight.bold),
                              ),
                              const SizedBox(width: 2),
                              Padding(
                                padding: const EdgeInsets.only(top: 6),
                                child: Text(
                                  "间",
                                  style: TextStyle(
                                      fontSize: 14,
                                      color: GlobalData
                                          .themeData!.secondaryHeaderColor),
                                ),
                              ),
                            ],
                          ),
                          Text(
                            "负责房间",
                            style: TextStyle(
                                fontSize: 14,
                                color:
                                    GlobalData.themeData!.secondaryHeaderColor),
                          ),
                        ],
                      )),
                  Row(
                    children: [
                      Expanded(
                          flex: 1,
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              const Text(
                                "22",
                                style: TextStyle(
                                    fontSize: 22, fontWeight: FontWeight.bold),
                              ),
                              Text(
                                "本月出勤",
                                style: TextStyle(
                                    fontSize: 14,
                                    color: GlobalData
                                        .themeData!.secondaryHeaderColor),
                              ),
                            ],
                          )),
                      Expanded(
                          flex: 1,
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              const Text(
                                "0",
                                style: TextStyle(
                                    fontSize: 22, fontWeight: FontWeight.bold),
                              ),
                              Text(
                                "本月投诉",
                                style: TextStyle(
                                    fontSize: 14,
                                    color: GlobalData
                                        .themeData!.secondaryHeaderColor),
                              ),
                            ],
                          )),
                    ],
                  )
                ],
              ),
            ),
          )),
      Expanded(
          flex: 5,
          child: AspectRatio(
            aspectRatio: 0.72,
            child: LayoutBuilder(builder: (context, cos) {
              return Selector<DataModel, double>(
                  selector: (content, scrollOpacity) =>
                      _dataModel.scrollOpacity,
                  builder: (context, scrollOpacity, child) {
                    double size = cos.maxHeight - 50;
                    double top = size / 4 + 25;
                    return Stack(
                      children: [
                        Positioned(
                          left: 0,
                          child: Container(
                            margin: const EdgeInsets.all(25),
                            width: size,
                            height: size,
                            decoration: BoxDecoration(
                                color: GlobalData.themeData!.primaryColor
                                    .withAlpha(102),
                                borderRadius: BorderRadius.circular(300),
                                boxShadow: [
                                  BoxShadow(
                                    color: GlobalData.themeData!.primaryColor
                                        .withAlpha(51),
                                    blurRadius: 20,
                                  ),
                                ]),
                            child: Echarts(
                              option: '''
 {
  series: [
    {
      // 第二个刻度盘
      type: 'gauge',
      radius: '85%',
      startAngle: 270,
      endAngle: 90,
      min: 0,
      max: 100,
      progress: {
        show: false,
        width: 8
      },
      pointer: {
        show: false
      },
      axisLine: {
        show:false,
      },
      axisTick: {
        // 刻度线
        distance: -8,
        splitNumber: 3,
        lineStyle: {
          width: 2,
          color: '#aaa'
        }
      },
      splitLine: {
        distance: -14,
        length: 6,
        lineStyle: {
          width: 2,
          color: '#00acfc'
        }
      },
      axisLabel: {
        show: false
      },
      detail: {
        show: false
      }
    },
    {
      // 进度条仪表盘
      type: 'gauge',
      radius: '70%',
      startAngle: 270,
      endAngle: 90,
      min: 0,
      max: 100,
      splitNumber: 12,
      progress: {
        // 进度条
        show: true,
        roundCap: true,
        width: 10,
        itemStyle: {
          color: {
            type: 'linear',
            x: 1,
            y: 0,
            x2: 0,
            y2: 0,
            colorStops: [
              {
                offset: 1,
                color: '#00acfc' // 0% 处的颜色
              },
              {
                offset: 0,
                color: '#00f8d9' // 100% 处的颜色
              }
            ],
            global: false // 缺省为 false
          }
        }
      },
      pointer: {
        //仪表盘指针
        show: false
      },
      axisLine: {
        roundCap: true,
        lineStyle: {
          width: 10,
          color: [[1, '#00ffff30']] // 圆环底色
        }
      },
      axisTick: {
        show: false
      },
      splitLine: {
        show: false
      },
      axisLabel: {
        show: false
      },
      anchor: {
        show: false
      },
      title: {
        show: false
      },
      detail: {
        valueAnimation: true,
        show: false
      },
      data: [
        {
          value: 80
        }
      ]
    }
  ]
}
                  ''',
                            ),
                          ),
                        ),
                        Positioned(
                          top: top,
                          left: top,
                          child: ClipRRect(
                            borderRadius: BorderRadius.circular(300),
                            child: Image.asset(
                              'assets/imgs/bg_home.png',
                              fit: BoxFit.fill,
                              width: size / 2,
                              height: size / 2,
                            ),
                          ),
                        ),
                        Positioned(
                          top: top,
                          left: top,
                          child: Container(
                            width: size / 2,
                            height: size / 2,
                            decoration: BoxDecoration(
                                border: Border.all(
                                    color: Colors.white.withAlpha(
                                        ((scrollOpacity * 0.3 + 0.1) * 255)
                                            .round()),
                                    width: 8),
                                color: GlobalData.themeData!.primaryColor
                                    .withAlpha(
                                        ((0.6 - scrollOpacity * 0.4) * 255)
                                            .round()),
                                borderRadius: BorderRadius.circular(300),
                                boxShadow: [
                                  BoxShadow(
                                    color: GlobalData.themeData!.primaryColor
                                        .withAlpha(51),
                                    blurRadius: 20,
                                  ),
                                ]),
                            child: const Center(
                              child: Column(
                                mainAxisSize: MainAxisSize.min,
                                children: [
                                  Text(
                                    "80",
                                    style: TextStyle(
                                        fontSize: 30,
                                        fontWeight: FontWeight.bold),
                                  ),
                                  Text(
                                    "本月评分",
                                    style: TextStyle(fontSize: 14),
                                  ),
                                ],
                              ),
                            ),
                          ),
                        ),
                      ],
                    );
                  });
            }),
          ))
    ]);
  }

  void getData({bool first = false}) {
    ApiServerOrder.page(
        query: serverOrderQuery,
        success: (r) {
          r['records'].forEach((element) {
            serverOrders.add(ServerOrder.fromJson(element));
          });
          _dataModel.changeServerOrder();
        });
  }
}

class DataModel extends ChangeNotifier {
  bool disposed = false;
  double scrollOpacity = 1.0;
  int serverOrder = 0;
  void changeScrollOpacity(val) {
    if (disposed) {
      return;
    }
    scrollOpacity = val;
    notifyListeners();
  }

  void changeServerOrder() {
    if (disposed) {
      return;
    }
    serverOrder++;
    notifyListeners();
  }
}
