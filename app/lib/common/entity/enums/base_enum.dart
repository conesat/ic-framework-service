import 'package:json_annotation/json_annotation.dart';

part 'base_enum.g.dart';

@JsonSerializable()
class BaseEnum {
  BaseEnum({this.code = 0, this.text = ""});

  int code = 0;
  String text = '';

  //反序列化
  factory BaseEnum.fromJson(Map<String, dynamic> json) {
    return _$BaseEnumFromJson(json);
  }

  //序列化
  Map<String, dynamic> toJson() => _$BaseEnumToJson(this);
}
