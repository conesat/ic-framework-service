import 'package:app/common/entity/enums/base_enum.dart';
import 'package:json_annotation/json_annotation.dart';

part 'server_order.g.dart';

@JsonSerializable()
class ServerOrder {
  ServerOrder();
  String? id;
  String? serverUserId;
  String? serverUserName;
  String? avatarFileUrl;
  String? targetContent;
  BaseEnum? state;
  BaseEnum? target;
  String? targetId;
  String? createTime;
  String? finishTime;
  String? acceptTime;
  String? doTime;

  //反序列化
  factory ServerOrder.fromJson(Map<String, dynamic> json) {
    return _$ServerOrderFromJson(json);
  }

  //序列化
  Map<String, dynamic> toJson() => _$ServerOrderToJson(this);
}
