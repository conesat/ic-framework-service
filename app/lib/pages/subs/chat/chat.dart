import 'package:app/common/api/sys/api_chat.dart';
import 'package:app/common/constant.dart';
import 'package:app/common/entity/chat_msg.dart';
import 'package:app/common/global.dart';
import 'package:app/common/utils/date_format_util.dart';
import 'package:app/components/user_avatar.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../../../common/api/query.dart';
import '../../../common/api/sys/api_chat_msg.dart';
import '../../../common/api/sys/api_chat_user.dart';
import '../../../common/entity/chat.dart';
import '../../../common/entity/enums/chat_msg_type.dart';
import '../../../common/entity/user.dart';

class ChatPage extends StatefulWidget {
  final String? userId;
  final String? chatId;
  final String? chatName;

  const ChatPage({this.chatId, this.chatName, this.userId, super.key});

  @override
  State<ChatPage> createState() => _ChatPageState();
}

class _ChatPageState extends State<ChatPage>
    with SingleTickerProviderStateMixin, WidgetsBindingObserver {
  final List<ChatMsg> messages = [];
  final ScrollController _scrollController = ScrollController();
  final TextEditingController _textController = TextEditingController();
  final FocusNode _focusNode = FocusNode();
  late DataModel _dataModel;
  bool keepHeight = false;
  final GlobalKey _widgetKey = GlobalKey();
  List<User> chatUserList = [];
  Query msgQuery = Query(pageIndex: 1, pageSize: 30);
  String? userId;
  String? chatId;
  String? chatName;

  @override
  void initState() {
    super.initState();
    userId = widget.userId;
    chatId = widget.chatId;
    chatName = widget.chatName;
    _focusNode.addListener(_handleFocusChange);
    _scrollController.addListener(_onScroll);
    if (chatId == null) {
      ApiChat.createSysSingle(userId, success: (r) {
        chatId = Chat.fromJson(r).id;
        getChatUserList(chatId!);
      });
    } else {
      getChatUserList(chatId!);
    }
    WidgetsBinding.instance.addObserver(this);
  }

  void _onScroll() {
    if (_scrollController.position.atEdge &&
        _scrollController.position.pixels ==
            _scrollController.position.maxScrollExtent) {
      _loadMoreMessages();
    }
  }

  void _loadMoreMessages() {
    // 保存当前的滚动位置
    _dataModel.changeMsg();
    // 恢复滚动位置
    WidgetsBinding.instance.addPostFrameCallback((_) {
      // _scrollController.jumpTo(currentScrollPosition + 100); // 根据需要调整偏移量
    });
    msgQuery.pageIndex++;
    getChatMsg(chatId);
  }

  @override
  void didChangeMetrics() {
    super.didChangeMetrics();
    double bottom = MediaQuery.of(context).viewInsets.bottom;
    if (bottom > _dataModel.keyboardHeight && _dataModel.onInputText) {
      _dataModel.changeKeyboardHeight(bottom);
    } else if (bottom == _dataModel.keyboardHeight && _dataModel.onInputText) {
      _scrollToBottom();
    } else if (bottom == 0) {
      _dataModel.changeKeyboardHeight(0.0);
    }
  }

  @override
  void dispose() {
    _dataModel.disposed = true;
    _focusNode.removeListener(_handleFocusChange);
    _focusNode.dispose();
    _textController.dispose();
    _scrollController.dispose();
    WidgetsBinding.instance.removeObserver(this);
    super.dispose();
  }

  void _handleFocusChange() {
    _dataModel.changeOnInputText(_focusNode.hasFocus);
    if (!_focusNode.hasFocus) {
      _dataModel.changeKeyboardHeight(0.0);
    }
  }

  void _scrollToBottom() {
    if (_scrollController.hasClients) {
      _scrollController.animateTo(
        0,
        duration: const Duration(milliseconds: 300),
        curve: Curves.easeOut,
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return ChangeNotifierProvider(
        create: (context) => DataModel(),
        builder: (context, child) {
          _dataModel = context.read<DataModel>();
          return Scaffold(
            appBar: AppBar(
              title: Text(chatName ?? "聊天"),
            ),
            resizeToAvoidBottomInset: false,
            body: GestureDetector(
              onTap: () {
                FocusScope.of(context).unfocus();
              },
              child: SafeArea(
                top: false,
                child: Column(
                  children: [
                    Expanded(
                      flex: 1,
                      child: buildMsgList(),
                    ),
                    buildBottom(),
                    Selector<DataModel, double>(
                        selector: (content, keyboardHeight) =>
                            _dataModel.keyboardHeight,
                        builder: (context, keyboardHeight, child) {
                          return Container(
                              color: GlobalData.themeData!.cardColor,
                              height: keyboardHeight);
                        })
                  ],
                ),
              ),
            ),
          );
        });
  }

  // 嵌套的customScrollView
  Widget buildMsgList() {
    return Selector<DataModel, int>(
        selector: (content, msgChange) => _dataModel.msgChange,
        builder: (context, msgChange, child) {
          return LayoutBuilder(
              builder: (BuildContext lbContext, BoxConstraints constraints) {
            double layoutHeight = constraints.biggest.height;
            return CustomScrollView(
              slivers: <Widget>[
                SliverPadding(
                  padding: const EdgeInsets.all(0.0),
                  sliver: SliverToBoxAdapter(
                    child: Container(
                      alignment: Alignment.topCenter,
                      height: layoutHeight,
                      child: buildMsgListContent(constraints),
                    ),
                  ),
                ),
              ],
            );
          });
        });
  }

  Widget buildMsgListContent(BoxConstraints cos) {
    return ListView.builder(
      physics: const AlwaysScrollableScrollPhysics(),
      key: const ValueKey("chat-list"),
      shrinkWrap: true,
      addRepaintBoundaries: false,
      padding:
          const EdgeInsets.only(left: 0.0, right: 0.0, bottom: 0.0, top: 0.0),
      reverse: true,
      clipBehavior: Clip.none,
      controller: _scrollController,
      itemCount: messages.length,
      itemBuilder: (context, index) {
        List<String> fs = [];
        var dateTime = DateTime.now();
        if (messages[index].createDateTime!.day != dateTime.day) {
          if (messages[index].createDateTime!.year != dateTime.year) {
            fs = YYYY_MM_DD_HH_MM_SS;
          } else {
            fs = MM_DD_HH_MM_SS;
          }
        } else {
          fs = HH_MM_SS;
        }

        String time = "";
        if (index < messages.length - 1) {
          int differenceInMinutes = messages[index]
              .createDateTime!
              .difference(messages[index + 1].createDateTime!)
              .inMinutes;
          if (differenceInMinutes > 5) {
            time = dateFormat(messages[index].createDateTime!, fs);
          }
        } else {
          time = dateFormat(messages[index].createDateTime!, fs);
        }
        late Padding item;
        bool showAvatar = false;
        if (messages[index].userId == GlobalData.userId) {
          showAvatar = time.isNotEmpty ||
              index == messages.length - 1 ||
              messages[index + 1].userId != GlobalData.userId;
          item = buildRightItem(showAvatar, cos, messages[index]);
        } else {
          showAvatar = time.isNotEmpty ||
              index == messages.length - 1 ||
              messages[index + 1].userId == GlobalData.userId;
          item = buildLeftItem(showAvatar, cos, messages[index]);
        }

        return Column(
          children: <Widget>[
            if (time.isNotEmpty)
              Padding(
                padding: EdgeInsets.only(top: index == 0 ? 0 : 10, bottom: 10),
                child: Text(
                  time,
                  style: TextStyle(
                      fontSize: 14,
                      color: GlobalData.themeData!.secondaryHeaderColor),
                ),
              ),
            if (showAvatar && time.isEmpty) const SizedBox(height: 30),
            item,
          ],
        );
      },
    );
  }

  Padding buildLeftItem(bool showAvatar, BoxConstraints cos, ChatMsg chatMsg) {
    return Padding(
      padding: const EdgeInsets.only(left: 10, right: 10, bottom: 4),
      child: Align(
        alignment: Alignment.centerLeft,
        child: Row(
            mainAxisSize: MainAxisSize.min,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Builder(builder: (context) {
                if (showAvatar) {
                  return UserAvatar(
                    url: chatMsg.userPic,
                    width: 40,
                    height: 40,
                  );
                } else {
                  return const SizedBox(width: 40);
                }
              }),
              const SizedBox(width: 10),
              Container(
                constraints: BoxConstraints(maxWidth: cos.maxWidth * 0.6),
                decoration: BoxDecoration(
                    color: GlobalData.themeData!.cardColor.withAlpha(100),
                    borderRadius: BorderRadius.circular(10)),
                padding:
                    const EdgeInsets.symmetric(horizontal: 20, vertical: 10),
                child: Text(
                  chatMsg.message!,
                  style: const TextStyle(fontSize: 14, height: 1.5),
                ),
              ),
            ]),
      ),
    );
  }

  Padding buildRightItem(bool showAvatar, BoxConstraints cos, ChatMsg chatMsg) {
    return Padding(
      padding: const EdgeInsets.only(left: 10, right: 10, bottom: 4),
      child: Align(
        alignment: Alignment.centerRight,
        child: Row(
            mainAxisSize: MainAxisSize.min,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Container(
                constraints: BoxConstraints(maxWidth: cos.maxWidth * 0.6),
                decoration: BoxDecoration(
                    color: GlobalData.themeData!.cardColor.withAlpha(100),
                    borderRadius: BorderRadius.circular(10)),
                padding:
                    const EdgeInsets.symmetric(horizontal: 20, vertical: 10),
                child: Text(
                  chatMsg.message!,
                  style: const TextStyle(fontSize: 14, height: 1.5),
                ),
              ),
              const SizedBox(width: 10),
              Builder(builder: (context) {
                if (showAvatar) {
                  return UserAvatar(
                    url: Avatar.man,
                    width: 40,
                    height: 40,
                  );
                } else {
                  return const SizedBox(width: 40);
                }
              }),
            ]),
      ),
    );
  }

  Container buildBottom() {
    return Container(
      key: _widgetKey,
      padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 2),
      color: GlobalData.themeData!.primaryColor,
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.end,
        children: [
          Selector<DataModel, bool>(
              selector: (content, hasInputText) => _dataModel.hasInputText,
              builder: (context, hasInputText, child) {
                return Expanded(
                  child: AnimatedContainer(
                    duration: const Duration(milliseconds: 150),
                    curve: Curves.easeInOut,
                    constraints: const BoxConstraints(
                      minHeight: 20,
                    ),
                    decoration: BoxDecoration(
                      color: GlobalData.themeData!.cardColor.withOpacity(0.4),
                      borderRadius: BorderRadius.all(
                          Radius.circular(hasInputText ? 10 : 30)),
                    ),
                    child: Row(
                      children: [
                        Selector<DataModel, bool>(
                            selector: (content, onInputText) =>
                                _dataModel.onInputText,
                            builder: (context, onInputText, child) {
                              return Visibility(
                                visible: !onInputText && !hasInputText,
                                child: Container(
                                  padding: const EdgeInsets.only(left: 10),
                                  child: Icon(
                                    Icons.mic,
                                    size: 24,
                                    color: GlobalData
                                        .themeData!.secondaryHeaderColor,
                                  ),
                                ),
                              );
                            }),
                        Expanded(
                          flex: 1,
                          child: TextField(
                            controller: _textController,
                            focusNode: _focusNode,
                            onChanged: (text) {
                              _dataModel.changeHasInputText(text.isNotEmpty);
                            },
                            textInputAction: TextInputAction.send,
                            onEditingComplete: () {
                              String text = _textController.text;
                              if (text.isNotEmpty) {
                                messages.insert(
                                    0,
                                    ChatMsg(
                                        msgType: ChatMsgType.text,
                                        userId: GlobalData.userId,
                                        userPic:
                                            GlobalData.account.avatarFileUrl,
                                        message: text,
                                        createDateTime: DateTime.now()));
                                _textController.clear();
                                _dataModel.changeMsg();
                                Future.delayed(
                                    const Duration(milliseconds: 100), () {
                                  _scrollToBottom();
                                });
                                ApiChatMsg.create(
                                    chatId: chatId!, message: text, msgType: 1);
                              }
                            },
                            keyboardType: TextInputType.multiline,
                            maxLength: 500,
                            minLines: 1,
                            maxLines: 5,
                            cursorHeight: 14,
                            style: const TextStyle(height: 1.4, fontSize: 16),
                            scrollPadding: const EdgeInsets.symmetric(
                                horizontal: 12, vertical: 10),
                            decoration: const InputDecoration(
                              filled: false,
                              counterText: "",
                              hintText: '请输入内容',
                              border: OutlineInputBorder(
                                  borderSide: BorderSide.none),
                              contentPadding: EdgeInsets.symmetric(
                                  horizontal: 12, vertical: 10),
                            ),
                          ),
                        ),
                      ],
                    ),
                  ),
                );
              }),
          const SizedBox(width: 10),
          SizedBox(
            height: 46,
            width: 40,
            child: Center(
              child: Icon(
                Icons.emoji_emotions_outlined,
                size: 30,
                color: GlobalData.themeData!.secondaryHeaderColor,
              ),
            ),
          ),
          const SizedBox(width: 4),
          AnimatedSwitcher(
            duration: const Duration(milliseconds: 200),
            child: SizedBox(
              key: const ValueKey('imageButton'),
              height: 46,
              width: 40,
              child: Center(
                child: Icon(
                  Icons.add_circle_outline_outlined,
                  size: 30,
                  color: GlobalData.themeData!.secondaryHeaderColor,
                ),
              ),
            ),
          )
        ],
      ),
    );
  }

  void getChatUserList(chatId) {
    ApiChatUser.all(chatId, success: (r) {
      if (r == null) {
        return;
      }
      List<String> names = [];
      r.forEach((item) {
        var user = User.fromJson(item);
        chatUserList.add(user);
        if (user.id != GlobalData.userId &&
            names.length < 4 &&
            user.name != null) {
          names.add(user.name!);
        }
      });
      chatName ??= names.join("、");
      setState(() {});
    });

    getChatMsg(chatId);
  }

  void getChatMsg(chatId) {
    ApiChatMsg.page(chatId, query: msgQuery, success: (r) {
      r['records'].forEach((element) {
        messages.add(ChatMsg.fromJson(element));
      });
      setState(() {});
    });
  }
}

class DataModel extends ChangeNotifier {
  bool disposed = false;
  bool hasInputText = false;
  int msgChange = 0;
  bool onInputText = false;
  bool emojiShowing = false;
  bool moreShowing = false;
  double keyboardHeight = 0; //键盘默认高度

  void changeOnInputText(val) {
    if (disposed) {
      return;
    }
    onInputText = val;
    notifyListeners();
  }

  void changeMsg() {
    if (disposed) {
      return;
    }
    msgChange++;
    notifyListeners();
  }

  void changeKeyboardHeight(val) {
    if (disposed) {
      return;
    }
    keyboardHeight = val;
    notifyListeners();
  }

  void changeHasInputText(val) {
    if (disposed) {
      return;
    }
    hasInputText = val;
    notifyListeners();
  }

  closeBottomAll() {
    if (disposed) {
      return;
    }
    moreShowing = false;
    emojiShowing = false;
    notifyListeners();
  }

  changeEmojiShowing(v) {
    emojiShowing = v;
    moreShowing = !v;
    notifyListeners();
  }
}
