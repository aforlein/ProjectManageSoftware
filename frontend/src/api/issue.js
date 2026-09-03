import request from '@/utils/request'

export function getIssuePage(params) {
  return request.get('/issue/page', { params })
}

export function getIssue(id) {
  return request.get(`/issue/${id}`)
}

export function createIssue(data) {
  return request.post('/issue', data)
}

export function updateIssue(id, data) {
  return request.put(`/issue/${id}`, data)
}

export function deleteIssue(id) {
  return request.delete(`/issue/${id}`)
}
