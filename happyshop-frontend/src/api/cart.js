import request from '../utils/request'

export function getCartList() {
  return request.get('/api/cart')
}

export function addToCart(data) {
  return request.post('/api/cart', data)
}

export function updateCartItem(id, data) {
  return request.put(`/api/cart/${id}`, data)
}

export function removeCartItem(id) {
  return request.delete(`/api/cart/${id}`)
}

export function clearCart() {
  return request.delete('/api/cart/clear')
}
