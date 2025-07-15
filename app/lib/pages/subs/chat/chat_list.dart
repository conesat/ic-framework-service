import 'package:app/common/api/query.dart';
import 'package:app/common/global.dart';
import 'package:app/components/user_avatar.dart';
import 'package:flutter/material.dart';

import '../../../common/api/sys/api_chat.dart';
import '../../../common/entity/chat.dart';
import '../../../common/entity/enums/chat_type.dart';
import 'chat.dart';

class ChatList extends StatefulWidget {
  const ChatList({super.key});

  @override
  State<ChatList> createState() => _ChatListState();
}

class _ChatListState extends State<ChatList> {
  List<Chat> chatList = [];
  Query query = Query(pageIndex: 1, pageSize: 20);

  @override
  void initState() {
    super.initState();
    getChatList();
  }

  void getChatList({bool reload = false}) {
    if (reload) {
      chatList.clear();
      query.pageIndex = 1;
    }
    ApiChat.page(
        query: query,
        success: (r) {
          if (r['records'] == null) {
            return;
          }
          r['records'].forEach((element) {
            chatList.add(Chat.fromJson(element));
          });
          setState(() {});
        });
  }

  @override
  Widget build(BuildContext context) {
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
              Text(
                "查找消息内容",
                style: TextStyle(
                  color: GlobalData.themeData!.secondaryHeaderColor,
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
                getChatList(reload: true);
              },
              child: ListView.builder(
                  itemCount: chatList.length,
                  itemBuilder: (context, index) {
                    return Padding(
                      padding: EdgeInsets.only(
                          bottom: index == chatList.length - 1 ? 80 : 0),
                      child: GestureDetector(
                          onTap: () {
                            Navigator.push(
                                context,
                                MaterialPageRoute(
                                    builder: (context) => ChatPage(
                                          chatId: chatList[index].id!,
                                          chatName: chatList[index].name,
                                        ))).then((r) {
                              getChatList(reload: true);
                            });
                          },
                          child: buildChatItem(chatList[index])),
                    );
                  }),
            )),
      ],
    );
  }

  Widget buildChatItem(Chat item) {
    return Container(
      padding: const EdgeInsets.only(bottom: 10),
      child: Row(
        children: [
          Stack(children: [
            Padding(
              padding: const EdgeInsets.only(top: 10, right: 14),
              child: buildPic(item),
            ),
            if (item.unreadCount > 0)
              Positioned(
                  right: 6,
                  top: 4,
                  child: Container(
                    constraints: const BoxConstraints(minWidth: 20),
                    padding: const EdgeInsets.symmetric(horizontal: 4),
                    decoration: BoxDecoration(
                        color: Colors.redAccent,
                        borderRadius: BorderRadius.circular(20)),
                    child: Center(
                      child: Text(
                        item.unreadCount > 99 ? '99+' : "${item.unreadCount}",
                        style:
                            const TextStyle(fontSize: 12, color: Colors.white),
                      ),
                    ),
                  ))
          ]),
          Expanded(
            flex: 1,
            child: Padding(
              padding: const EdgeInsets.only(top: 10),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    getChatName(item),
                    style: const TextStyle(
                        fontSize: 16, fontWeight: FontWeight.bold),
                  ),
                  const SizedBox(height: 4),
                  Row(
                    children: [
                      Expanded(
                        flex: 1,
                        child: Text(
                          item.summary ?? "",
                          overflow: TextOverflow.ellipsis,
                          style: TextStyle(
                              fontSize: 14,
                              color:
                                  GlobalData.themeData!.secondaryHeaderColor),
                        ),
                      ),
                      const SizedBox(width: 10),
                      if (item.lastMsgTime != null)
                        Text(
                          formatDateTime(item.lastMsgTime!),
                          overflow: TextOverflow.ellipsis,
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

  UserAvatar buildPic(Chat item) {
    return UserAvatar(
      url: item.users != null && item.users!.isNotEmpty
          ? item.users![0].avatarFileUrl
          : null,
      name: item.name ??
          (item.chatType!.code == ChatType.group.code
              ? "群组"
              : (item.users != null && item.users!.isNotEmpty
                  ? item.users![0].name
                  : '未知')),
      width: 46,
      height: 46,
    );
  }

  String formatDateTime(String time) {
    DateTime dateTime = DateTime.parse(time);
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

  String getChatName(Chat chat) {
    List<String> names = [];
    for (var value in chat.users!) {
      if (value.id != GlobalData.userId) {
        names.add(value.name!);
      }
    }
    return names.join(",");
  }
}
