import request from '../utils/request'

export function getCategories() {
  return request.get('/api/categories')
}

export function getCategoryTree() {
  return request.get('/api/categories/tree')
}
