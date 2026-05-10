import request from '../utils/request'

export function getProductReviews(productId, params) {
  return request.get(`/api/products/${productId}/reviews`, { params })
}

export function addReview(data) {
  return request.post('/api/reviews', data)
}
