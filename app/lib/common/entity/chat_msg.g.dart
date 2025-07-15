// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'chat_msg.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

ChatMsg _$ChatMsgFromJson(Map<String, dynamic> json) => ChatMsg(
      id: json['id'] as String?,
      userId: json['userId'] as String?,
      userName: json['userName'] as String?,
      userPic: json['userPic'] as String?,
      chatId: json['chatId'] as String?,
      message: json['message'] as String?,
      summary: json['summary'] as String?,
      msgType: json['msgType'] == null
          ? null
          : BaseEnum.fromJson(json['msgType'] as Map<String, dynamic>),
      userType: json['userType'] as String?,
      createTime: json['createTime'] as String?,
      createDateTime: json['createDateTime'] == null
          ? null
          : DateTime.parse(json['createDateTime'] as String),
    );

Map<String, dynamic> _$ChatMsgToJson(ChatMsg instance) => <String, dynamic>{
      'id': instance.id,
      'userId': instance.userId,
      'userName': instance.userName,
      'userPic': instance.userPic,
      'chatId': instance.chatId,
      'message': instance.message,
      'summary': instance.summary,
      'msgType': instance.msgType,
      'userType': instance.userType,
      'createTime': instance.createTime,
      'createDateTime': instance.createDateTime?.toIso8601String(),
    };
