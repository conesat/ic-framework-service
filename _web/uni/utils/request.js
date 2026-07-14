/**
 * 小程序 API 基础地址：真机调试请改为电脑局域网 IP，例如 http://192.168.1.8:9998/api。
 * H5 本地调试可保持默认地址。
 */
export const API_BASE_URL = 'http://127.0.0.1:9998/api'

export function request({ url, method = 'GET', data = {}, auth = false }) {
  return new Promise((resolve, reject) => {
    const token = uni.getStorageSync('energy_token')
    const header = { 'content-type': 'application/x-www-form-urlencoded' }
    if (auth && token) header.Authorization = token

    uni.request({
      url: `${API_BASE_URL}${url}`,
      method,
      data,
      header,
      success: ({ statusCode, data: response }) => {
        if (statusCode < 200 || statusCode >= 300) {
          reject(new Error(response?.msg || '服务暂时不可用'))
          return
        }
        // IC Framework 的普通响应是 { code, msg, data }，分页接口直接返回分页对象。
        if (response && Object.prototype.hasOwnProperty.call(response, 'code')) {
          if (response.code !== 0) {
            reject(new Error(response.msg || '请求失败'))
            return
          }
          resolve(response.data)
          return
        }
        resolve(response)
      },
      fail: () => reject(new Error('网络连接失败，请检查服务地址'))
    })
  })
}

export function requireLogin() {
  if (uni.getStorageSync('energy_token')) return true
  uni.showModal({
    title: '请先登录',
    content: '登录后可预约电池、查看订单和提交维修工单。',
    success: ({ confirm }) => {
      if (confirm) uni.reLaunch({ url: '/pages/welcome/welcome' })
    }
  })
  return false
}
