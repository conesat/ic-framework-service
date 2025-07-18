import http, {DetailRequestParam, RequestParam} from '@/api/common/http';

const path = '/sys/setting';

const settingService = {
  updateSetting(param: RequestParam) {
    http.put({
      url: `${path}`,
      ...param
    });
  },
  detail(param: DetailRequestParam) {
    http.get({
      url: `${path}`,
      ...param
    });
  },
  ad(param: RequestParam) {
    http.get({
      url: `/public/setting/ad`,
      ...param
    });
  },
  update(param: RequestParam) {
    http.get({
      url: `${path}/update`,
      ...param
    });
  },
  isInit(param: RequestParam) {
    http.get({
      url: `/public/setting/is-init`,
      ...param
    });
  },
  resetCode(resetCode: any, param: RequestParam) {
    http.post({
      url: `/public/setting/update-code?code=${resetCode}`,
      ...param
    });
  },
  init(param: RequestParam) {
    http.post({
      url: `/public/setting/init`,
      ...param
    });
  },
  userTypes(param: RequestParam) {
    http.get({
      url: `${path}/user-types`,
      ...param
    });
  },
};

export default settingService;
