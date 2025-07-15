import {PaginationProps} from "tdesign-vue-next/es/pagination";

const queryDef = {
  pageIndex: 1,
  pageSize: 10,
  orders: '',
  searchKey: ''
}

const paginationDef: PaginationProps = {
  current: 1,
  pageSize: 10,
}
export { queryDef, paginationDef };
