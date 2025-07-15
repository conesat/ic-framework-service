import BaseService from '@/api/common/baseService';
import http, {RequestParam, UploadRequestParam} from '@/api/common/http';

import {Register, UploadSingle, UploadSlice} from './types/fileTypes';

const path = '/sys/sys-file';

class ApiSysFile extends BaseService {
  registerUploadSlice(param: RequestParam) {
    http.get({
      url: `${path}/register-upload-slice`,
      ...param
    });
  }

  abortUploadSlice(param: RequestParam) {
    http.put({
      url: `${path}/abort-upload-slice`,
      ...param
    });
  }

  uploadSlice(param: UploadRequestParam) {
    http.upload({
      url: `${path}/upload-slice`,
      ...param
    });
  }

  uploadSingle(param: UploadRequestParam) {
    http.upload({
      url:`${path}/upload-single`,
      ...param
    });
  }
}

export default new ApiSysFile(path);
