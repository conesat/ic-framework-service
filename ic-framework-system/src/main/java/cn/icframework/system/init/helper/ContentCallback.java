package cn.icframework.system.init.helper;

@FunctionalInterface
public interface ContentCallback {
    void onContentReceived(String content);
}