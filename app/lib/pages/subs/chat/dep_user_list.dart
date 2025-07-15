import 'package:app/common/api/query.dart';
import 'package:app/pages/subs/chat/chat.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../../../common/api/sys/api_user.dart';
import '../../../common/entity/dep_user.dart';
import '../../../common/global.dart';
import '../../../common/theme/app_theme.dart';
import '../../../components/user_avatar.dart';

class DepUserList extends StatefulWidget {
  const DepUserList({super.key});

  @override
  State<DepUserList> createState() => _DepUserListState();
}

class _DepUserListState extends State<DepUserList> {
  List<DepUser> orgItemList = [];
  Query query = Query(pageIndex: 1, pageSize: 10);
  late DataModel _dataModel;

  @override
  void initState() {
    super.initState();
    getData();
  }

  void reloadData() {
    orgItemList.clear();
    query.pageIndex = 1;
    getData();
  }

  void getData() {
    ApiUser.page(
        query: query,
        success: (r) {
          if (r['records'] == null) {
            return;
          }
          r['records'].forEach((item) {
            orgItemList.add(DepUser.fromJson(item));
          });
          _dataModel.changeUser();
        });
  }

  @override
  Widget build(BuildContext context) {
    return ChangeNotifierProvider(
        create: (context) => DataModel(),
        builder: (context, child) {
          _dataModel = context.read<DataModel>();
          return Column(
            children: [
              Container(
                padding: const EdgeInsets.all(14),
                decoration: BoxDecoration(
                    color: GlobalData.themeData!.cardColor
                        .withAlpha((0.2 * 255).round()),
                    borderRadius: BorderRadius.circular(20)),
                child: Row(
                  children: [
                    Icon(
                      Icons.search,
                      size: 24,
                      color: GlobalData.themeData!.highlightColor,
                    ),
                    const SizedBox(width: 10),
                    Expanded(
                      flex: 1,
                      child: TextField(
                        style: const TextStyle(fontSize: 14),
                        decoration: const InputDecoration(
                            hintText: "查找用户",
                            border: InputBorder.none,
                            isCollapsed: true,
                            contentPadding: EdgeInsets.all(0),
                            fillColor: Colors.transparent,
                            filled: true),
                        onSubmitted: (v) {
                          query.searchKey = v;
                          reloadData();
                        },
                      ),
                    ),
                  ],
                ),
              ),
              const SizedBox(height: 10),
              Expanded(
                  flex: 1,
                  child: RefreshIndicator(
                    onRefresh: () async {
                      reloadData();
                    },
                    child: buildListContent(),
                  )),
            ],
          );
        });
  }

  Selector<DataModel, int> buildListContent() {
    return Selector<DataModel, int>(
        selector: (content, user) => _dataModel.user,
        builder: (context, user, child) {
          return ListView.builder(
              itemCount: orgItemList.length,
              itemBuilder: (context, index) {
                return Padding(
                  padding: EdgeInsets.only(
                      bottom: index == orgItemList.length - 1 ? 80 : 0),
                  child: GestureDetector(
                      onTap: () {
                        toChatPage(context, index);
                      },
                      child: buildChatItem(orgItemList[index])),
                );
              });
        });
  }

  void toChatPage(BuildContext context, int index) {
    Navigator.push(
        context,
        MaterialPageRoute(
            builder: (context) => ChatPage(
                userId: orgItemList[index].id!,
                chatName: orgItemList[index].name))).then((r) {});
  }

  Widget buildChatItem(DepUser item) {
    return Container(
      padding: const EdgeInsets.only(bottom: 10),
      child: Row(
        children: [
          Padding(
            padding: const EdgeInsets.only(top: 10, right: 14),
            child: buildPic(
                item.avatarFileUrl ?? "", item.name ?? "", item.online),
          ),
          Expanded(
            flex: 1,
            child: Padding(
              padding: const EdgeInsets.only(top: 10),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    item.name ?? '',
                    style: const TextStyle(
                        fontSize: 16, fontWeight: FontWeight.bold),
                  ),
                  const SizedBox(height: 4),
                  Row(
                    children: [
                      Expanded(
                        flex: 1,
                        child: Text(
                          (item.deps ?? []).join(","),
                          overflow: TextOverflow.ellipsis,
                          style: TextStyle(
                              fontSize: 14,
                              color:
                                  GlobalData.themeData!.secondaryHeaderColor),
                        ),
                      ),
                      const SizedBox(width: 10),
                      Text(
                        item.online ?? false ? "在线" : "离线",
                        style: TextStyle(
                            fontSize: 12,
                            color: GlobalData.themeData!.secondaryHeaderColor
                                .withAlpha((0.6 * 255).round())),
                      ),
                    ],
                  ),
                ],
              ),
            ),
          )
        ],
      ),
    );
  }

  UserAvatar buildPic(pic, name, online) {
    return UserAvatar(
      url: pic,
      name: name,
      width: 46,
      height: 46,
      borderWidth: 2,
      borderColor: online ?? false
          ? AppTheme.primaryColor
          : GlobalData.themeData!.cardColor,
    );
  }

  String formatDateTime(DateTime dateTime) {
    final now = DateTime.now();
    final today = DateTime(now.year, now.month, now.day);
    final thisYear = DateTime(now.year, 1, 1);

    if (dateTime.isAfter(today.subtract(const Duration(days: 1))) &&
        dateTime.isBefore(today.add(const Duration(days: 1)))) {
      return "${dateTime.hour.toString().padLeft(2, '0')}:${dateTime.minute.toString().padLeft(2, '0')}";
    } else if (dateTime.isAfter(thisYear.subtract(const Duration(days: 1))) &&
        dateTime.isBefore(thisYear.add(const Duration(days: 365)))) {
      return "${dateTime.month.toString().padLeft(2, '0')}-${dateTime.day.toString().padLeft(2, '0')} ${dateTime.hour.toString().padLeft(2, '0')}:${dateTime.minute.toString().padLeft(2, '0')}";
    } else {
      return "${dateTime.year}-${dateTime.month.toString().padLeft(2, '0')}-${dateTime.day.toString().padLeft(2, '0')} ${dateTime.hour.toString().padLeft(2, '0')}:${dateTime.minute.toString().padLeft(2, '0')}";
    }
  }
}

class DataModel extends ChangeNotifier {
  bool disposed = false;
  int user = 0;

  void changeUser() {
    if (disposed) {
      return;
    }
    user++;
    notifyListeners();
  }
}
