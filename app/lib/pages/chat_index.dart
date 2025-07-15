import 'package:app/common/global.dart';
import 'package:app/common/theme/app_theme.dart';
import 'package:app/pages/subs/chat/chat_list.dart';
import 'package:app/pages/subs/chat/dep_user_list.dart';
import 'package:flutter/material.dart';

class ChatIndexPage extends StatefulWidget {
  const ChatIndexPage({super.key});

  @override
  State<ChatIndexPage> createState() => _ChatIndexPageState();
}

class _ChatIndexPageState extends State<ChatIndexPage>
    with SingleTickerProviderStateMixin {
  late TabController _tabController;

  @override
  void initState() {
    super.initState();
    _tabController = TabController(length: 2, vsync: this);
  }

  @override
  void dispose() {
    _tabController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: SafeArea(
        bottom: false,
        child: Column(
          children: [
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 10),
              child: TabBar(
                tabAlignment: TabAlignment.start,
                controller: _tabController,
                isScrollable: true,
                labelColor: GlobalData.themeData!.highlightColor,
                unselectedLabelColor:
                    GlobalData.themeData!.secondaryHeaderColor,
                labelStyle: const TextStyle(
                  fontSize: 18,
                  fontWeight: FontWeight.bold,
                ),
                unselectedLabelStyle: const TextStyle(
                  fontSize: 18,
                ),
                dividerColor: Colors.transparent,
                padding: const EdgeInsets.all(0),
                labelPadding: const EdgeInsets.symmetric(horizontal: 10),
                indicatorPadding: const EdgeInsets.symmetric(horizontal: 10),
                overlayColor: WidgetStateProperty.all(Colors.transparent),
                indicator: UnderlineTabIndicator(
                  borderRadius: BorderRadius.circular(6),
                  borderSide: const BorderSide(
                    width: 3,
                    color: AppTheme.primaryColor,
                  ),
                  insets: const EdgeInsets.only(top: 2),
                ),
                tabs: const [
                  Tab(text: '消息'),
                  Tab(text: '同事'),
                ],
              ),
            ),
            const SizedBox(height: 10),
            Expanded(
              flex: 1,
              child: TabBarView(
                controller: _tabController,
                children: const [
                  Padding(
                    padding: EdgeInsets.symmetric(horizontal: 10),
                    child: ChatList(),
                  ),
                  Padding(
                    padding: EdgeInsets.symmetric(horizontal: 10),
                    child: DepUserList(),
                  ),
                ],
              ),
            )
          ],
        ),
      ),
    );
  }
}
