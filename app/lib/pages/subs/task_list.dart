import 'package:app/common/entity/server_order.dart';
import 'package:app/common/global.dart';
import 'package:app/components/task_item.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

class TaskListPage extends StatefulWidget {
  const TaskListPage({super.key});

  @override
  State<TaskListPage> createState() => _TaskListPageState();
}

class _TaskListPageState extends State<TaskListPage> {
  late DataModel _dataModel;

  final ScrollController _scrollController = ScrollController();

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
  }

  @override
  Widget build(BuildContext context) {
    return ChangeNotifierProvider(
        create: (context) => DataModel(),
        builder: (context, child) {
          _dataModel = context.read<DataModel>();
          return Container(
            color: GlobalData.themeData!.primaryColor,
            child: Stack(
              children: [
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
                          GlobalData.themeData!.primaryColor.withOpacity(0.2),
                          GlobalData.themeData!.primaryColor.withOpacity(0.8),
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
                  child: Scaffold(
                    backgroundColor: Colors.transparent,
                    appBar: AppBar(
                      title: const Text("任务"),
                      forceMaterialTransparency: true,
                    ),
                    body: Container(
                      padding: const EdgeInsets.symmetric(horizontal: 14),
                      child: Column(
                        children: [
                          const SizedBox(height: 10),
                          Selector<DataModel, int>(
                              selector: (content, tab) => _dataModel.tab,
                              builder: (context, tab, child) {
                                return Row(
                                  children: [
                                    buildTabItem(1, "审批"),
                                    buildTabItem(2, "接送"),
                                    buildTabItem(3, "接待"),
                                    buildTabItem(4, "配送"),
                                    buildTabItem(5, "保洁"),
                                    buildTabItem(6, "维修"),
                                  ],
                                );
                              }),
                          const SizedBox(height: 20),
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
                                    color:
                                        GlobalData.themeData!.highlightColor),
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
                          const SizedBox(height: 10),
                          Expanded(
                              flex: 1,
                              child: ListView.builder(
                                  controller: _scrollController,
                                  itemCount: 20,
                                  itemBuilder: (index, context) {
                                    return TaskItem(ServerOrder());
                                  }))
                        ],
                      ),
                    ),
                  ),
                ),
              ],
            ),
          );
        });
  }

  Expanded buildTabItem(index, title) {
    return Expanded(
        flex: 1,
        child: GestureDetector(
          onTap: () {
            _dataModel.changeTab(index);
          },
          child: Column(
            children: [
              Text(
                title,
                style: TextStyle(
                    fontWeight: index == _dataModel.tab
                        ? FontWeight.bold
                        : FontWeight.normal,
                    color: index == _dataModel.tab
                        ? GlobalData.themeData!.highlightColor
                        : GlobalData.themeData!.highlightColor
                            .withOpacity(0.8)),
              ),
              const SizedBox(
                height: 4,
              ),
              Container(
                width: 14,
                height: 4,
                decoration: BoxDecoration(
                    color: index == _dataModel.tab
                        ? GlobalData.themeData!.highlightColor
                        : Colors.transparent,
                    borderRadius: BorderRadius.circular(4)),
              )
            ],
          ),
        ));
  }
}

class DataModel extends ChangeNotifier {
  bool disposed = false;
  int tab = 1;
  double scrollOpacity = 1.0;

  void changeScrollOpacity(val) {
    if (disposed) {
      return;
    }
    scrollOpacity = val;
    notifyListeners();
  }

  void changeTab(val) {
    if (disposed) {
      return;
    }
    tab = val;
    notifyListeners();
  }
}
