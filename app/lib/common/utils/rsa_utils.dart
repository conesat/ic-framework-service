import 'dart:convert';
import 'dart:typed_data';

import 'package:encrypt/encrypt.dart';
import 'package:pointycastle/export.dart';

String encryptRsa(String publicKey, String base64PlainText) {
  var parser = RSAKeyParser();
  String rsaPublic =
      "-----BEGIN PUBLIC KEY-----\n$publicKey\n-----END PUBLIC KEY-----";
  RSAPublicKey rsaPublicKey = parser.parse(rsaPublic) as RSAPublicKey;
  return Encrypter(RSA(publicKey: rsaPublicKey))
      .encrypt(base64PlainText)
      .base64;
}

Uint8List createUint8ListFromString(String s) {
  final codec = utf8.fuse(base64);
  final stringToBase64 = codec.encode(s);
  return base64.decode(stringToBase64);
}
