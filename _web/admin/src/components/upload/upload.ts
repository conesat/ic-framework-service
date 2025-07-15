import ApiSysFile from "@/api/sys/ApiSysFile";
import {FileUseType} from "@/constants/file-user-types";
import {Register, UploadSingle, UploadSlice} from "@/api/sys/types/fileTypes";


// 每个切片的大小，例如200KB
const chunkSize = 200 * 1024;
// 切片文件
const fileSlice: Array<Blob> = []
const cutFile = (file: File) => {
  let start = 0;
  let end = chunkSize;
  let count = 0;
  // 分片
  while (start < file.size) {
    fileSlice[count++] = file.slice(start, end);
    start = end;
    end = start + chunkSize;
    if (end > file.size) {
      end = file.size;
    }
  }
}


const uploadSlice = (uploadId: string, index: number,
                     onprogress?: (progress: number) => void,
                     success?: (response: any | null) => void,
                     fail?: (res: any | null) => void) => {
  ApiSysFile.uploadSlice({
    data: <UploadSlice>{
      uploadId: uploadId,
      file: fileSlice[index],
      partNumber: index + 1
    },
    success: (res: any) => {
      if (index < fileSlice.length - 1) {
        uploadSlice(uploadId, index + 1, onprogress, success, fail)
      } else {
        if (success) {
          success(res)
        }
      }
    },
    onprogress: (res: any) => {
      if (onprogress) {
        // 一共 fileSlice.length 份切片
        // 每份完成的进度是 1/fileSlice.length
        // 已完成的进度 index / fileSlice.length
        // 当前进度是 (index / fileSlice.length + (res/100) / fileSlice.length) * 100
        onprogress((index / fileSlice.length + (res / 100) / fileSlice.length) * 100)
      }
    },
    fail: fail
  })
}

export async function uploadFile(file: File, useType: FileUseType,
                                 onprogress?: (progress: number) => void,
                                 success?: (response: any | null) => void,
                                 fail?: (res: any | null) => void) {
  // 文件大于1M 使用分片，否则单个上传
  if (file.size > 1024 * 1024) {
    cutFile(file);
    ApiSysFile.registerUploadSlice({
      data: <Register>{
        useType: useType.id,
        fileName: file.name,
        totalParts: fileSlice.length
      },
      success: (uploadId: string) => {
        uploadSlice(uploadId, 0, onprogress, success, fail)
      }
    })
  } else {
    ApiSysFile.uploadSingle({
      data: <UploadSingle>{file: file, useType: useType.id},
      success: success,
      onprogress: onprogress, fail: fail
    })
  }
}
