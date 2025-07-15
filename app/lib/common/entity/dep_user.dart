import 'package:app/common/entity/enums/base_enum.dart';
import 'package:app/common/entity/id_name.dart';
import 'package:json_annotation/json_annotation.dart';

part 'dep_user.g.dart';

@JsonSerializable()
class DepUser {
  DepUser();

  String? id;
  bool? online;
  String? name;
  String? username;
  String? avatarFileUrl;
  String? phone;
  String? email;
  String? createTime;
  List<IdName>? deps;
  List<IdName>? pos;
  BaseEnum? sex;

  //反序列化
  factory DepUser.fromJson(Map<String, dynamic> json) {
    return _$DepUserFromJson(json);
  }

  //序列化
  Map<String, dynamic> toJson() => _$DepUserToJson(this);
}
