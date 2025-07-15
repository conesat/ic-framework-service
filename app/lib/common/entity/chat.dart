import 'package:app/common/entity/enums/base_enum.dart';
import 'package:app/common/entity/user.dart';
import 'package:json_annotation/json_annotation.dart';

part 'chat.g.dart';

@JsonSerializable()
class Chat {
  Chat();

  String? id;
  String? message;
  String? name;
  String? summary;
  bool? temporary;
  BaseEnum? msgType;
  BaseEnum? chatType;
  String? lastMsgTime;
  List<User>? users;
  int unreadCount = 0;

  //反序列化
  factory Chat.fromJson(Map<String, dynamic> json) {
    if (json['unreadCount'] == null) {
      json['unreadCount'] = 0;
    }
    return _$ChatFromJson(json);
  }

  //序列化
  Map<String, dynamic> toJson() => _$ChatToJson(this);
}
