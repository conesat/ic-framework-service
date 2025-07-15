import 'package:app/common/entity/enums/base_enum.dart';
import 'package:json_annotation/json_annotation.dart';

part 'chat_msg.g.dart';

@JsonSerializable()
class ChatMsg {
  ChatMsg({
    this.id,
    this.userId,
    this.userName,
    this.userPic,
    this.chatId,
    this.message,
    this.summary,
    this.msgType,
    this.userType,
    this.createTime,
    this.createDateTime,
  });

  String? id;

  /// 发送者
  String? userId;
  String? userName;
  String? userPic;

  /// 聊天记录id
  String? chatId;

  /// 内容
  String? message;

  /// 概要
  String? summary;

  /// 类型
  BaseEnum? msgType;

  /// 发送用户类型
  String? userType;

  /// 创建时间
  String? createTime;
  DateTime? createDateTime;

  //反序列化
  factory ChatMsg.fromJson(Map<String, dynamic> json) {
    if (json["createTime"] != null) {
      json["createDateTime"] = json["createTime"];
    }
    return _$ChatMsgFromJson(json);
  }

  //序列化
  Map<String, dynamic> toJson() => _$ChatMsgToJson(this);
}
