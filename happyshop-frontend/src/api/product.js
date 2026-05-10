import request from '../utils/request'

export function getProducts(params) {
  return request.get('/api/products', { params })
}

export function getProductDetail(id) {
  return request.get(`/api/products/${id}`)
}

export function searchProducts(params) {
  return request.get('/api/products/search', { params })
}

export function getHotProducts() {
  return request.get('/api/products/hot')
}

export function getRecommendProducts() {
  return request.get('/api/products/recommend')
}
