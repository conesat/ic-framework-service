package cn.icframework.system.enums;

import java.util.Set;

/**
 * 私有图片类型，如果不添加到这里的FileUseType，均视为公开
 * @author hzl
 * @since 2023/6/21
 */
public interface PrivateFileUseType {
    Set<FileUseType> PRIVATE_FILE_USE_TYPE = Set.of(FileUseType.PRIVATE);
}
