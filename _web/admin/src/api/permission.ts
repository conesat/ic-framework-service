import http from '@/api/common/http';
import {RouteItem, RouteMeta} from '@/api/model/permissionModel';
import ApiUserMine from '@/api/user/ApiUserMine';
import {MENU_TYPE} from '@/constants';

const {path} = ApiUserMine;

export function getMenuList(): Promise<Array<RouteItem>> {
  return new Promise((resolve, reject) => {
    http.get({
        url: `${path}/menus`,
        success: (res: any) => {
          resolve(buildTree(res));
        },
        fail: (res: any) => {
          reject(res);
        },
      });
  });
}

const buildTree = (data: any) => {
  // 创建一个映射，以ID为键，节点为值
  const nodeMap: Map<number, any> = new Map(data.map((item: any) => [item.id, toMenu(item)]));
  // 定义树形结构的数组
  const tree: Array<RouteItem> = [];
  // 遍历数据，构建树
  data.forEach((node: any) => {
    if ((!node.url && !node.children) || !node.status) {
      return;
    }
    if (!node.parentId) {
      // 如果没有父ID，直接推入树数组
      tree.push(nodeMap.get(node.id));
    } else {
      // 如果有父ID，找到父节点并添加子节点
      const parentNode = nodeMap.get(node.parentId);
      if (parentNode) {
        // 如果父节点存在，添加子节点
        if (!parentNode.children) {
          parentNode.children = [];
        }
        parentNode.children.push(nodeMap.get(node.id));
      }
    }
  });
  // 移除没有实际节点内容
  for (let i = tree.length - 1; i >= 0; i--) {
    const parent = tree[i];
    parent.redirect = getFilterMenu(parent);
    if (parent.redirect === parent.path && parent.component === 'LAYOUT') {
      tree.splice(i, 1);
    }
  }
  return tree;
};

const getFilterMenu = (data: RouteItem): string => {
  if (data.children && data.children.length > 0) {
    return `${data.path}/${getFilterMenu(data.children[0])}`;
  }
  return data.path;
};

const toMenu = (data: any): RouteItem => {
  const component = MENU_TYPE[data.menuType.code];
  const meta: RouteMeta = {
    title: data.name,
    keepAlive: data.keepAlive,
    hidden: data.hidden,
    orderNo: data.orderNo,
    svgIcon: data.iconType && data.iconType.code === 1 ? data.icon : undefined,
    icon: data.iconType && data.iconType.code === 2 ? data.icon : undefined,
  };
  if (component === 'LAYOUT') {
    meta.parent = data.belong;
  } else {
    meta.frameSrc = data.url;
    meta.frameBlank = component === 'BLANK';
  }
  const getComponent = (url: string, component: string): string => {
    if (url) return url;
    return component === 'LAYOUT' ? 'LAYOUT' : 'IFRAME';
  };

  return {
    path: data.path,
    name: data.name,
    component: getComponent(data.url, component),
    meta,
  };
};
