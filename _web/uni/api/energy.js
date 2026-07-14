import { request } from '../utils/request'

export const getNearbyStations = (limit = 10) => request({
  url: '/public/swap-station/nearby',
  data: { limit }
})

export const getStationDetail = (id) => request({
  url: `/public/swap-station/item/${id}`
})

export const getAvailableBatteries = (stationId) => request({
  url: '/app/swap-battery/available',
  data: { stationId },
  auth: true
})

export const createSwapOrder = (stationId, orderType, remark = '') => request({
  url: '/app/swap-order/create',
  method: 'POST',
  data: { stationId, orderType, remark },
  auth: true
})

export const getMyOrders = (pageIndex = 1, pageSize = 20, status = '') => request({
  url: '/app/swap-order/page',
  method: 'POST',
  data: { pageIndex, pageSize, ...(status === '' ? {} : { status }) },
  auth: true
})

export const cancelSwapOrder = (id) => request({
  url: `/app/swap-order/cancel/${id}`,
  method: 'POST',
  auth: true
})

export const getMyRepairOrders = (pageIndex = 1, pageSize = 20, status = '') => request({
  url: '/app/repair-order/page',
  method: 'POST',
  data: { pageIndex, pageSize, ...(status === '' ? {} : { status }) },
  auth: true
})

export const createRepairOrder = (payload) => request({
  url: '/app/repair-order/create',
  method: 'POST',
  data: payload,
  auth: true
})
