// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'dep_user.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

DepUser _$DepUserFromJson(Map<String, dynamic> json) => DepUser()
  ..id = json['id'] as String?
  ..online = json['online'] as bool?
  ..name = json['name'] as String?
  ..username = json['username'] as String?
  ..avatarFileUrl = json['avatarFileUrl'] as String?
  ..phone = json['phone'] as String?
  ..email = json['email'] as String?
  ..createTime = json['createTime'] as String?
  ..deps = (json['deps'] as List<dynamic>?)
      ?.map((e) => IdName.fromJson(e as Map<String, dynamic>))
      .toList()
  ..pos = (json['pos'] as List<dynamic>?)
      ?.map((e) => IdName.fromJson(e as Map<String, dynamic>))
      .toList()
  ..sex = json['sex'] == null
      ? null
      : BaseEnum.fromJson(json['sex'] as Map<String, dynamic>);

Map<String, dynamic> _$DepUserToJson(DepUser instance) => <String, dynamic>{
      'id': instance.id,
      'online': instance.online,
      'name': instance.name,
      'username': instance.username,
      'avatarFileUrl': instance.avatarFileUrl,
      'phone': instance.phone,
      'email': instance.email,
      'createTime': instance.createTime,
      'deps': instance.deps,
      'pos': instance.pos,
      'sex': instance.sex,
    };
