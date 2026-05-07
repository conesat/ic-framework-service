package cn.icframework.system.module.sysfile.service;

import cn.icframework.system.enums.FileUseType;
import cn.icframework.system.module.sysfile.SysFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DisabledFileHelper implements IFileHelper {
    private static final String MESSAGE = "文件存储能力未启用";

    @Override
    public String registerUploadSlice(FileUseType useType, String fileName, int totalParts, Long userId) {
        throw new IllegalStateException(MESSAGE);
    }

    @Override
    public SysFile uploadSlice(MultipartFile file, String uploadId, int partNumber) {
        throw new IllegalStateException(MESSAGE);
    }

    @Override
    public void abortUploadSlice(String uploadId) {
        throw new IllegalStateException(MESSAGE);
    }

    @Override
    public SysFile uploadSingle(MultipartFile file, FileUseType useType, Long userId) {
        throw new IllegalStateException(MESSAGE);
    }
}
