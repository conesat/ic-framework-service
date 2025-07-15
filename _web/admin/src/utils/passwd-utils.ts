import JSEncrypt from "jsencrypt";
import md5 from 'js-md5';

const rsaEncrypt = new JSEncrypt();

export function encryptPasswd(username: string, passwd: string, publicKey: string): string {
  rsaEncrypt.setPublicKey(publicKey);
  return rsaEncrypt.encrypt(md5(username + md5(username + passwd))).toString();
}
