// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'user.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

User _$UserFromJson(Map<String, dynamic> json) => User()
  ..id = json['id'] as String?
  ..name = json['name'] as String?
  ..username = json['username'] as String?
  ..avatarFileUrl = json['avatarFileUrl'] as String?
  ..phone = json['phone'] as String?
  ..email = json['email'] as String?
  ..createTime = json['createTime'] as String?
  ..deps = (json['deps'] as List<dynamic>?)?.map((e) => e as String).toList()
  ..pos = (json['pos'] as List<dynamic>?)?.map((e) => e as String).toList()
  ..sex = json['sex'] == null
      ? null
      : BaseEnum.fromJson(json['sex'] as Map<String, dynamic>);

Map<String, dynamic> _$UserToJson(User instance) => <String, dynamic>{
      'id': instance.id,
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
