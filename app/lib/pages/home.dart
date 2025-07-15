import 'package:app/common/api/query.dart';
import 'package:app/common/api/sys/api_server_order.dart';
import 'package:app/common/entity/server_order.dart';
import 'package:app/common/global.dart';
import 'package:app/common/theme/app_theme.dart';
import 'package:app/components/task_item.dart';
import 'package:app/components/user_avatar.dart';
import 'package:app/pages/subs/task_list.dart';
import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:provider/provider.dart';

class HomePage extends StatefulWidget {
  const HomePage({super.key});

  @override
  State<HomePage> createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
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
                height: 400,
                right: 0,
                child: Selector<DataModel, double>(
                    selector: (content, scrollOpacity) =>
                        _dataModel.scrollOpacity,
                    builder: (context, scrollOpacity, child) {
                      return Opacity(
                        opacity: scrollOpacity,
                        child: Image.asset(
                          'assets/imgs/bg_home.png',
                          fit: BoxFit.fill,
                          width: double.infinity,
                          height: 400,
                        ),
                      );
                    }),
              ),
              Positioned(
                left: 0,
                top: 0,
                right: 0,
                child: Container(
                  height: 400,
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
                        Container(
                          padding: const EdgeInsets.all(14),
                          decoration: BoxDecoration(
                              color: GlobalData.themeData!.cardColor
                                  .withOpacity(0.2),
                              borderRadius: BorderRadius.circular(20)),
                          child: Row(
                            children: [
                              Icon(Icons.search,
                                  size: 24,
                                  color: GlobalData.themeData!.highlightColor),
                              const SizedBox(width: 10),
                              Text(
                                "请输入客房号/住户号",
                                style: TextStyle(
                                    color: GlobalData
                                        .themeData!.secondaryHeaderColor),
                              ),
                            ],
                          ),
                        ),
                        const SizedBox(height: 30),
                        buildCard(),
                        const SizedBox(height: 30),
                        Container(
                          padding: const EdgeInsets.all(14),
                          decoration: BoxDecoration(
                              color: GlobalData.themeData!.cardColor
                                  .withOpacity(0.2),
                              borderRadius: BorderRadius.circular(20)),
                          child: Row(
                            children: [
                              buildOpr("icon_0", "预定管理"),
                              buildOpr("icon_1", "今日到店"),
                              buildOpr("icon_2", "宾客入住"),
                              buildOpr("icon_3", "服务管理"),
                            ],
                          ),
                        ),
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
                                  GestureDetector(
                                    onTap: () {
                                      Navigator.push(
                                          context,
                                          MaterialPageRoute(
                                              builder: (context) =>
                                                  const TaskListPage()));
                                    },
                                    child: Row(
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
                                    ),
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

  Expanded buildOpr(icon, title) {
    return Expanded(
        flex: 1,
        child: Column(
          children: [
            SizedBox(
              width: 40,
              height: 40,
              child: SvgPicture.asset("assets/svg/icons/$icon.svg"),
            ),
            const SizedBox(height: 6),
            Text(
              title,
              style: const TextStyle(fontSize: 12),
            )
          ],
        ));
  }

  Row buildTop() {
    return Row(
      children: [
        Container(
          decoration: BoxDecoration(
            borderRadius: BorderRadius.circular(50),
            boxShadow: [
              BoxShadow(
                color: Colors.black.withOpacity(0.1),
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
                "Hello，张三",
                style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
              ),
              const SizedBox(height: 4),
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
        Container(
          width: 40,
          height: 40,
          decoration: BoxDecoration(
            borderRadius: BorderRadius.circular(14),
            color: GlobalData.themeData!.cardColor.withOpacity(0.1),
          ),
          child: const Icon(
            Icons.qr_code_scanner,
            size: 28,
          ),
        ),
        const SizedBox(width: 10),
        Container(
          width: 40,
          height: 40,
          decoration: BoxDecoration(
            borderRadius: BorderRadius.circular(14),
            color: GlobalData.themeData!.cardColor.withOpacity(0.1),
          ),
          child: const Icon(
            Icons.notifications_none,
            size: 28,
          ),
        )
      ],
    );
  }

  Row buildCard() {
    return Row(children: [
      Expanded(
          flex: 1,
          child: AspectRatio(
              aspectRatio: 1,
              child: Container(
                  margin: const EdgeInsets.only(right: 10),
                  decoration: BoxDecoration(
                      color: AppTheme.primaryColor,
                      borderRadius: BorderRadius.circular(20)),
                  child: Column(
                    children: [
                      Expanded(
                          flex: 1,
                          child: Center(
                            child: Row(
                              mainAxisSize: MainAxisSize.min,
                              crossAxisAlignment: CrossAxisAlignment.center,
                              children: [
                                Container(
                                  padding: const EdgeInsets.all(10),
                                  decoration: BoxDecoration(
                                      color: Colors.white.withOpacity(0.1),
                                      borderRadius: BorderRadius.circular(12)),
                                  child: const Icon(
                                    Icons.add,
                                    color: Colors.white,
                                  ),
                                ),
                                const SizedBox(width: 10),
                                Column(
                                  mainAxisSize: MainAxisSize.min,
                                  crossAxisAlignment: CrossAxisAlignment.start,
                                  children: [
                                    const Text(
                                      "快速建单",
                                      style: TextStyle(
                                          fontWeight: FontWeight.bold,
                                          color: Colors.white),
                                    ),
                                    const SizedBox(height: 4),
                                    Text(
                                      "消费/入住/退房",
                                      style: TextStyle(
                                          fontSize: 12,
                                          color: Colors.white.withOpacity(0.8)),
                                    )
                                  ],
                                )
                              ],
                            ),
                          )),
                      Expanded(
                          flex: 1,
                          child: Container(
                            padding: const EdgeInsets.symmetric(
                                vertical: 10, horizontal: 20),
                            decoration: BoxDecoration(
                                color: Colors.white.withOpacity(0.1),
                                borderRadius: BorderRadius.circular(20)),
                            child: Column(
                              children: [
                                const Row(
                                  children: [
                                    Icon(
                                      Icons.assignment_outlined,
                                      size: 14,
                                      color: Colors.white,
                                    ),
                                    SizedBox(width: 3),
                                    Text(
                                      "创建记录",
                                      style: TextStyle(
                                          fontWeight: FontWeight.bold,
                                          color: Colors.white,
                                          fontSize: 14),
                                    ),
                                    SizedBox(width: 4),
                                    Text(
                                      "12",
                                      style: TextStyle(
                                          fontWeight: FontWeight.bold,
                                          color: Colors.white,
                                          fontSize: 14),
                                    ),
                                  ],
                                ),
                                const SizedBox(height: 10),
                                Row(children: <Widget>[
                                  Expanded(
                                    flex: 1,
                                    child: Container(
                                      padding: const EdgeInsets.symmetric(
                                          vertical: 4),
                                      decoration: BoxDecoration(
                                        borderRadius: BorderRadius.circular(4),
                                        color: Colors.white.withOpacity(0.1),
                                      ),
                                      child: const Center(
                                        child: Text(
                                          "入住",
                                          style: TextStyle(
                                              color: Colors.white,
                                              fontSize: 12),
                                        ),
                                      ),
                                    ),
                                  ),
                                  const SizedBox(width: 4),
                                  Expanded(
                                    flex: 1,
                                    child: Container(
                                      padding: const EdgeInsets.symmetric(
                                          vertical: 4),
                                      decoration: BoxDecoration(
                                        borderRadius: BorderRadius.circular(4),
                                        color: Colors.white.withOpacity(0.1),
                                      ),
                                      child: const Center(
                                        child: Text(
                                          "消费",
                                          style: TextStyle(
                                              color: Colors.white,
                                              fontSize: 12),
                                        ),
                                      ),
                                    ),
                                  ),
                                  const SizedBox(width: 4),
                                  Expanded(
                                    flex: 1,
                                    child: Container(
                                      padding: const EdgeInsets.symmetric(
                                          vertical: 4),
                                      decoration: BoxDecoration(
                                        borderRadius: BorderRadius.circular(4),
                                        color: Colors.white.withOpacity(0.1),
                                      ),
                                      child: const Center(
                                        child: Text(
                                          "维修",
                                          style: TextStyle(
                                              color: Colors.white,
                                              fontSize: 12),
                                        ),
                                      ),
                                    ),
                                  ),
                                ])
                              ],
                            ),
                          ))
                    ],
                  )))),
      Expanded(
          flex: 1,
          child: Column(
            children: [
              AspectRatio(
                  aspectRatio: 2,
                  child: Container(
                    margin: const EdgeInsets.only(left: 10, bottom: 10),
                    decoration: BoxDecoration(
                      color: GlobalData.themeData!.cardColor.withOpacity(0.2),
                      borderRadius: BorderRadius.circular(20),
                    ),
                    child: Center(
                      child: Row(
                        mainAxisSize: MainAxisSize.min,
                        children: [
                          Container(
                            width: 50,
                            height: 50,
                            padding: const EdgeInsets.all(8),
                            decoration: BoxDecoration(
                                color: GlobalData.themeData!.cardColor
                                    .withOpacity(0.2),
                                borderRadius: BorderRadius.circular(20)),
                            child: SvgPicture.asset(
                                "assets/svg/icons/work_plan.svg"),
                          ),
                          const SizedBox(width: 10),
                          Column(
                            mainAxisSize: MainAxisSize.min,
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              const Text(
                                "工作排班",
                                style: TextStyle(fontWeight: FontWeight.bold),
                              ),
                              const SizedBox(height: 4),
                              Text(
                                "安排工作时间",
                                style: TextStyle(
                                    color: GlobalData
                                        .themeData!.secondaryHeaderColor),
                              ),
                            ],
                          )
                        ],
                      ),
                    ),
                  )),
              AspectRatio(
                  aspectRatio: 2,
                  child: Container(
                    margin: const EdgeInsets.only(left: 10, top: 10),
                    decoration: BoxDecoration(
                        color: GlobalData.themeData!.cardColor.withOpacity(0.2),
                        borderRadius: BorderRadius.circular(20)),
                    child: Center(
                      child: Row(
                        mainAxisSize: MainAxisSize.min,
                        children: [
                          Container(
                            width: 50,
                            height: 50,
                            padding: const EdgeInsets.all(8),
                            decoration: BoxDecoration(
                                color: const Color.fromRGBO(72, 132, 245, 0.1),
                                borderRadius: BorderRadius.circular(18)),
                            child:
                                SvgPicture.asset("assets/svg/icons/charts.svg"),
                          ),
                          const SizedBox(width: 10),
                          Column(
                            mainAxisSize: MainAxisSize.min,
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              const Text(
                                "报表",
                                style: TextStyle(fontWeight: FontWeight.bold),
                              ),
                              const SizedBox(height: 4),
                              Text(
                                "今日任务报表",
                                style: TextStyle(
                                    color: GlobalData
                                        .themeData!.secondaryHeaderColor),
                              ),
                            ],
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
