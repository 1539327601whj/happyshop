import request from '../utils/request'

export function getAddresses() {
  return request.get('/api/addresses')
}

export function addAddress(data) {
  return request.post('/api/addresses', data)
}

export function updateAddress(id, data) {
  return request.put(`/api/addresses/${id}`, data)
}

export function deleteAddress(id) {
  return request.delete(`/api/addresses/${id}`)
}

export function setDefaultAddress(id) {
  return request.put(`/api/addresses/${id}/default`)
}
