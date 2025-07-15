// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'ad.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

Ad _$AdFromJson(Map<String, dynamic> json) => Ad(
      appAdType: json['appAdType'] == null
          ? null
          : BaseEnum.fromJson(json['appAdType'] as Map<String, dynamic>),
      appAdUrl: json['appAdUrl'] as String?,
      appAdFileUrl: json['appAdFileUrl'] as String?,
    );

Map<String, dynamic> _$AdToJson(Ad instance) => <String, dynamic>{
      'appAdFileUrl': instance.appAdFileUrl,
      'appAdUrl': instance.appAdUrl,
      'appAdType': instance.appAdType,
    };
