import request from '@/utils/request'

export function getRiskPage(params) {
  return request.get('/risk/page', { params })
}

export function getRisk(id) {
  return request.get(`/risk/${id}`)
}

export function createRisk(data) {
  return request.post('/risk', data)
}

export function updateRisk(id, data) {
  return request.put(`/risk/${id}`, data)
}

export function deleteRisk(id) {
  return request.delete(`/risk/${id}`)
}
