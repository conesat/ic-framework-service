import 'package:app/common/entity/enums/base_enum.dart';
import 'package:json_annotation/json_annotation.dart';

part 'user.g.dart';

@JsonSerializable()
class User {
  User();
  String? id;
  String? name;
  String? username;
  String? avatarFileUrl;
  String? phone;
  String? email;
  String? createTime;
  List<String>? deps;
  List<String>? pos;
  BaseEnum? sex;

  //反序列化
  factory User.fromJson(Map<String, dynamic> json) {
    return _$UserFromJson(json);
  }

  //序列化
  Map<String, dynamic> toJson() => _$UserToJson(this);
}
