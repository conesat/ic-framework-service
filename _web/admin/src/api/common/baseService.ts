import http, {
  ChangeEnableRequestParam,
  DeleteRequestParam,
  DetailRequestParam,
  PageRequestParam,
  RequestParam
} from './http.js';

export default class BaseService {
  path = '';

  constructor(path: any) {
    this.path = path;
  }

  create(param: RequestParam) {
    http.post({
      url: `${this.path}`,
      ...param
    });
  }

  edit(param: RequestParam) {
    if (!param.data.id) {
      http.post(
        {
          url: `${this.path}`,
          ...param
        });
      return;
    }
    http.put({
      url: `${this.path}`,
      ...param
    });
  }

  all(param: RequestParam) {
    http.post({
      url: `${this.path}/all`,
      ...param
    });
  }

  page(param: PageRequestParam) {
    if (param.pagination) {
      param.data.pageIndex = param.pagination.current;
      param.data.pageSize = param.pagination.pageSize;
    }
    http.post({
      url: `${this.path}/page`,
      ...param
    });
  }

  detail(param: DetailRequestParam) {
    if (!param.id) {
      throw Error('id不能为空')
    }
    http.get({
      url: `${this.path}/item/${param.id}`,
      ...param
    });
  }

  delete(param: DeleteRequestParam) {
    if (!param.ids) {
      throw Error('ids不能为空')
    }
    param.data = {
      ids: param.ids
    };
    http.delete(
      {
        url: `${this.path}`,
        ...param
      }
    );
  }

  changeEnable(param: ChangeEnableRequestParam) {
    param.data = {
      ids: param.ids,
      enable: param.enable
    };
    http.post(
      {
        url: `${this.path}/change-enable`,
        ...param
      }
    );
  }
}
