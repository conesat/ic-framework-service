// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'base_enum.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

BaseEnum _$BaseEnumFromJson(Map<String, dynamic> json) => BaseEnum(
      code: (json['code'] as num?)?.toInt() ?? 0,
      text: json['text'] as String? ?? "",
    );

Map<String, dynamic> _$BaseEnumToJson(BaseEnum instance) => <String, dynamic>{
      'code': instance.code,
      'text': instance.text,
    };
