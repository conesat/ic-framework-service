const LOCAL_ORDERS_KEY = 'energy_local_orders'

function readOrders() {
  const orders = uni.getStorageSync(LOCAL_ORDERS_KEY)
  return Array.isArray(orders) ? orders : []
}

function writeOrders(orders) {
  uni.setStorageSync(LOCAL_ORDERS_KEY, orders.slice(0, 20))
}

export function getLocalOrders() {
  return readOrders()
}

export function addLocalOrder(order) {
  const orders = readOrders()
  const record = {
    id: `local-${Date.now()}`,
    no: `HD${Date.now()}`,
    createdAt: new Date().toLocaleString('zh-CN', { hour12: false }).replace(/\//g, '-'),
    ...order
  }
  orders.unshift(record)
  writeOrders(orders)
  return record
}

export function updateLocalOrder(no, patch) {
  const orders = readOrders().map(item => item.no === no ? { ...item, ...patch } : item)
  writeOrders(orders)
  return orders.find(item => item.no === no)
}
