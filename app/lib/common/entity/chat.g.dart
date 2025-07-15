// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'chat.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

Chat _$ChatFromJson(Map<String, dynamic> json) => Chat()
  ..id = json['id'] as String?
  ..message = json['message'] as String?
  ..name = json['name'] as String?
  ..summary = json['summary'] as String?
  ..temporary = json['temporary'] as bool?
  ..msgType = json['msgType'] == null
      ? null
      : BaseEnum.fromJson(json['msgType'] as Map<String, dynamic>)
  ..chatType = json['chatType'] == null
      ? null
      : BaseEnum.fromJson(json['chatType'] as Map<String, dynamic>)
  ..lastMsgTime = json['lastMsgTime'] as String?
  ..users = (json['users'] as List<dynamic>?)
      ?.map((e) => User.fromJson(e as Map<String, dynamic>))
      .toList()
  ..unreadCount = (json['unreadCount'] as num).toInt();

Map<String, dynamic> _$ChatToJson(Chat instance) => <String, dynamic>{
      'id': instance.id,
      'message': instance.message,
      'name': instance.name,
      'summary': instance.summary,
      'temporary': instance.temporary,
      'msgType': instance.msgType,
      'chatType': instance.chatType,
      'lastMsgTime': instance.lastMsgTime,
      'users': instance.users,
      'unreadCount': instance.unreadCount,
    };
