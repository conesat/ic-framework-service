// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'server_order.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

ServerOrder _$ServerOrderFromJson(Map<String, dynamic> json) => ServerOrder()
  ..id = json['id'] as String?
  ..serverUserId = json['serverUserId'] as String?
  ..serverUserName = json['serverUserName'] as String?
  ..avatarFileUrl = json['avatarFileUrl'] as String?
  ..targetContent = json['targetContent'] as String?
  ..state = json['state'] == null
      ? null
      : BaseEnum.fromJson(json['state'] as Map<String, dynamic>)
  ..target = json['target'] == null
      ? null
      : BaseEnum.fromJson(json['target'] as Map<String, dynamic>)
  ..targetId = json['targetId'] as String?
  ..createTime = json['createTime'] as String?
  ..finishTime = json['finishTime'] as String?
  ..acceptTime = json['acceptTime'] as String?
  ..doTime = json['doTime'] as String?;

Map<String, dynamic> _$ServerOrderToJson(ServerOrder instance) =>
    <String, dynamic>{
      'id': instance.id,
      'serverUserId': instance.serverUserId,
      'serverUserName': instance.serverUserName,
      'avatarFileUrl': instance.avatarFileUrl,
      'targetContent': instance.targetContent,
      'state': instance.state,
      'target': instance.target,
      'targetId': instance.targetId,
      'createTime': instance.createTime,
      'finishTime': instance.finishTime,
      'acceptTime': instance.acceptTime,
      'doTime': instance.doTime,
    };
