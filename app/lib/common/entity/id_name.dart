import 'package:json_annotation/json_annotation.dart';

part 'id_name.g.dart';

@JsonSerializable()
class IdName {
  IdName();

  Object? id;
  String? name;

  //反序列化
  factory IdName.fromJson(Map<String, dynamic> json) {
    return _$IdNameFromJson(json);
  }

  //序列化
  Map<String, dynamic> toJson() => _$IdNameToJson(this);
}
