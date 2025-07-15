import 'package:app/common/entity/enums/base_enum.dart';

class ChatMsgType {
  static BaseEnum text = BaseEnum(code: 1, text: "文本");
  static BaseEnum video = BaseEnum(code: 2, text: "视频");
  static BaseEnum voice = BaseEnum(code: 3, text: "语音");
  static BaseEnum pic = BaseEnum(code: 4, text: "图片");
  static BaseEnum file = BaseEnum(code: 5, text: "文件");
  static BaseEnum share = BaseEnum(code: 6, text: "分享");
}
