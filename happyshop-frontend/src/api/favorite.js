import request from '../utils/request'

export function getFavorites(params) {
  return request.get('/api/favorites', { params })
}

export function addFavorite(productId) {
  return request.post(`/api/favorites/${productId}`)
}

export function removeFavorite(productId) {
  return request.delete(`/api/favorites/${productId}`)
}

export function checkFavorite(productId) {
  return request.get(`/api/favorites/check/${productId}`)
}
