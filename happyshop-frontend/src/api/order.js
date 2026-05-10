import request from '../utils/request'

export function createOrder(data) {
  return request.post('/api/orders', data)
}

export function getOrders(params) {
  return request.get('/api/orders', { params })
}

export function getOrderDetail(id) {
  return request.get(`/api/orders/${id}`)
}

export function payOrder(id) {
  return request.put(`/api/orders/${id}/pay`)
}

export function cancelOrder(id) {
  return request.put(`/api/orders/${id}/cancel`)
}

export function confirmReceive(id) {
  return request.put(`/api/orders/${id}/receive`)
}
