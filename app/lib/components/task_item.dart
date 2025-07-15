import 'package:app/common/entity/server_order.dart';
import 'package:app/common/global.dart';
import 'package:app/common/theme/app_theme.dart';
import 'package:app/components/user_avatar.dart';
import 'package:flutter/material.dart';

class TaskItem extends StatefulWidget {
  final ServerOrder serverOrder;
  const TaskItem(this.serverOrder, {super.key});

  @override
  State<TaskItem> createState() => _TaskItemState();
}

class _TaskItemState extends State<TaskItem> {
  @override
  Widget build(BuildContext context) {
    return Container(
        width: double.maxFinite,
        margin: const EdgeInsets.only(bottom: 10, top: 10),
        padding: const EdgeInsets.all(20),
        decoration: BoxDecoration(
            color: GlobalData.themeData!.cardColor.withAlpha(50),
            borderRadius: BorderRadius.circular(AppTheme.borderRadius)),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                Expanded(
                  flex: 1,
                  child: Text(
                    "${widget.serverOrder.target?.text}单",
                  ),
                ),
                Text("${widget.serverOrder.serverUserName}"),
                const SizedBox(width: 8),
                UserAvatar(
                  url: widget.serverOrder.avatarFileUrl,
                  name: "${widget.serverOrder.serverUserName}",
                  width: 24,
                  height: 24,
                ),
              ],
            ),
            const SizedBox(height: 16),
            Row(
              children: [
                Expanded(
                  flex: 1,
                  child: Align(
                    alignment: Alignment.centerLeft,
                    child: Container(
                      padding: const EdgeInsets.symmetric(
                          horizontal: 10, vertical: 4),
                      decoration: BoxDecoration(
                          color:
                              GlobalData.themeData!.cardColor.withOpacity(0.6),
                          borderRadius: BorderRadius.circular(6)),
                      child: Text("${widget.serverOrder.targetContent}"),
                    ),
                  ),
                ),
              ],
            ),
            const SizedBox(height: 16),
            Row(
              children: [
                Expanded(
                  flex: 1,
                  child: Text(
                    "客房：101",
                    style: TextStyle(
                        fontSize: 12,
                        color: GlobalData.themeData!.secondaryHeaderColor),
                  ),
                ),
                Text(
                  "期望时间：${widget.serverOrder.doTime ?? '未指定'}",
                  style: TextStyle(
                      fontSize: 12,
                      color: GlobalData.themeData!.secondaryHeaderColor),
                ),
              ],
            )
          ],
        ));
  }
}
