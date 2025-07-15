import 'package:json_annotation/json_annotation.dart';

import 'enums/base_enum.dart';

part 'ad.g.dart';

@JsonSerializable()
class Ad {
  Ad({this.appAdType, this.appAdUrl, this.appAdFileUrl});

  String? appAdFileUrl;
  String? appAdUrl;
  BaseEnum? appAdType;

  //反序列化
  factory Ad.fromJson(Map<String, dynamic> json) {
    return _$AdFromJson(json);
  }

  //序列化
  Map<String, dynamic> toJson() => _$AdToJson(this);
}
