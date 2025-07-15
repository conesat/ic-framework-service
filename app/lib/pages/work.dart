import 'package:app/common/api/query.dart';
import 'package:app/common/api/sys/api_server_order.dart';
import 'package:app/common/entity/server_order.dart';
import 'package:app/common/global.dart';
import 'package:app/components/task_item.dart';
import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:provider/provider.dart';

class WorkPage extends StatefulWidget {
  const WorkPage({super.key});

  @override
  State<WorkPage> createState() => _WorkPageState();
}

class _WorkPageState extends State<WorkPage> {
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
                        GlobalData.themeData!.primaryColor.withAlpha(100),
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
                      padding: const EdgeInsets.only(
                          left: 14, right: 14, bottom: 80, top: 70),
                      child: Column(children: [
                        buildTop(),
                        const SizedBox(height: 30),
                        buildCard(),
                        const SizedBox(height: 30),
                        SizedBox(
                          width: double.maxFinite,
                          child: Column(
                            children: [
                              Row(
                                children: [
                                  const Expanded(
                                    flex: 1,
                                    child: Text(
                                      "待处理任务",
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
                                          color: GlobalData
                                              .themeData!.secondaryHeaderColor)
                                    ],
                                  )
                                ],
                              ),
                              const SizedBox(height: 20),
                              SizedBox(
                                width: double.maxFinite,
                                child: SingleChildScrollView(
                                  scrollDirection: Axis.horizontal,
                                  child: Row(
                                    mainAxisSize: MainAxisSize.min,
                                    mainAxisAlignment: MainAxisAlignment.start,
                                    children: [
                                      Container(
                                        padding: const EdgeInsets.symmetric(
                                            vertical: 4, horizontal: 14),
                                        decoration: BoxDecoration(
                                          color: GlobalData.themeData!.cardColor
                                              .withOpacity(0.6),
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
                                          return TaskItem(serverOrders[index]);
                                        });
                                  })
                            ],
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
        const SizedBox(width: 10),
        SizedBox(
            width: 46,
            height: 46,
            child: SvgPicture.asset(
              "assets/svg/logo.svg",
              colorFilter: ColorFilter.mode(
                  GlobalData.themeData!.highlightColor.withOpacity(0.8),
                  BlendMode.srcIn),
            )),
        const SizedBox(width: 10),
        Expanded(
          flex: 1,
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              const Text(
                "ICE酒店PMS",
                style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
              ),
              const SizedBox(height: 2),
              Text(
                "欢迎使用IceFramework",
                style: TextStyle(
                    color: GlobalData.themeData!.secondaryHeaderColor,
                    fontWeight: FontWeight.bold),
              ),
            ],
          ),
        ),
        const SizedBox(width: 10),
        const Icon(
          Icons.search,
          size: 30,
        )
      ],
    );
  }

  Row buildCard() {
    return Row(children: [
      Expanded(
          flex: 1,
          child: AspectRatio(
              aspectRatio: 0.8,
              child: Container(
                  margin: const EdgeInsets.only(right: 10),
                  decoration: BoxDecoration(
                      color: GlobalData.themeData!.cardColor.withOpacity(0.2),
                      borderRadius: BorderRadius.circular(20)),
                  child: Stack(
                    children: [
                      Align(
                        alignment: Alignment.bottomCenter,
                        child: SvgPicture.asset(
                          "assets/svg/big/work_check_in.svg",
                          height: 160,
                          fit: BoxFit.fitWidth,
                        ),
                      ),
                      Padding(
                        padding: const EdgeInsets.all(20),
                        child: Column(
                          mainAxisSize: MainAxisSize.min,
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            const Row(
                              children: [
                                Text(
                                  "今日到店",
                                  style: TextStyle(
                                      fontWeight: FontWeight.bold,
                                      fontSize: 18),
                                ),
                                SizedBox(width: 16),
                                Icon(Icons.arrow_circle_right_outlined)
                              ],
                            ),
                            const SizedBox(height: 6),
                            Text(
                              "记得打扫到店客户房间哦",
                              softWrap: true, // 默认值为 true，可以不设置
                              style: TextStyle(
                                  fontSize: 14,
                                  color: GlobalData
                                      .themeData!.secondaryHeaderColor),
                            ),
                          ],
                        ),
                      ),
                    ],
                  )))),
      Expanded(
          flex: 1,
          child: Column(
            children: [
              AspectRatio(
                  aspectRatio: 1.6,
                  child: Container(
                    margin: const EdgeInsets.only(left: 10, bottom: 10),
                    decoration: BoxDecoration(
                      color: GlobalData.themeData!.cardColor.withOpacity(0.2),
                      borderRadius: BorderRadius.circular(20),
                    ),
                    child: ClipRRect(
                      borderRadius: BorderRadius.circular(20),
                      child: Stack(
                        children: [
                          Positioned(
                            right: -60,
                            bottom: -75,
                            child: Container(
                              height: 180,
                              width: 180,
                              decoration: BoxDecoration(
                                  borderRadius: BorderRadius.circular(160),
                                  color: GlobalData.themeData!.cardColor
                                      .withOpacity(0.05)),
                            ),
                          ),
                          Positioned(
                            right: -30,
                            bottom: -45,
                            child: Container(
                              height: 120,
                              width: 120,
                              decoration: BoxDecoration(
                                  borderRadius: BorderRadius.circular(120),
                                  color: GlobalData.themeData!.cardColor
                                      .withOpacity(0.15)),
                            ),
                          ),
                          Positioned(
                            bottom: 16,
                            right: 16,
                            child: SvgPicture.asset(
                              "assets/svg/big/clear.svg",
                              height: 40,
                              width: 40,
                            ),
                          ),
                          Padding(
                            padding: const EdgeInsets.all(20),
                            child: Column(
                              mainAxisSize: MainAxisSize.min,
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                const Text(
                                  "已打扫",
                                  style: TextStyle(
                                      fontWeight: FontWeight.bold,
                                      fontSize: 16),
                                ),
                                const SizedBox(height: 4),
                                Text(
                                  "我清洁的房间",
                                  style: TextStyle(
                                      color: GlobalData
                                          .themeData!.secondaryHeaderColor),
                                ),
                              ],
                            ),
                          )
                        ],
                      ),
                    ),
                  )),
              AspectRatio(
                  aspectRatio: 1.6,
                  child: Container(
                    margin: const EdgeInsets.only(left: 10, top: 10),
                    decoration: BoxDecoration(
                        color: GlobalData.themeData!.cardColor.withOpacity(0.2),
                        borderRadius: BorderRadius.circular(20)),
                    child: ClipRRect(
                      borderRadius: BorderRadius.circular(20),
                      child: Stack(
                        children: [
                          Positioned(
                            right: -60,
                            bottom: -75,
                            child: Container(
                              height: 180,
                              width: 180,
                              decoration: BoxDecoration(
                                  borderRadius: BorderRadius.circular(160),
                                  color: GlobalData.themeData!.cardColor
                                      .withOpacity(0.05)),
                            ),
                          ),
                          Positioned(
                            right: -30,
                            bottom: -45,
                            child: Container(
                              height: 120,
                              width: 120,
                              decoration: BoxDecoration(
                                  borderRadius: BorderRadius.circular(120),
                                  color: GlobalData.themeData!.cardColor
                                      .withOpacity(0.15)),
                            ),
                          ),
                          Positioned(
                            bottom: 16,
                            right: 16,
                            child: SvgPicture.asset(
                              "assets/svg/big/order.svg",
                              height: 40,
                              width: 40,
                            ),
                          ),
                          Padding(
                            padding: const EdgeInsets.all(20),
                            child: Column(
                              mainAxisSize: MainAxisSize.min,
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                const Text(
                                  "已配送",
                                  style: TextStyle(
                                      fontWeight: FontWeight.bold,
                                      fontSize: 16),
                                ),
                                const SizedBox(height: 4),
                                Text(
                                  "我配送的订单",
                                  style: TextStyle(
                                      color: GlobalData
                                          .themeData!.secondaryHeaderColor),
                                ),
                              ],
                            ),
                          )
                        ],
                      ),
                    ),
                  )),
            ],
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
