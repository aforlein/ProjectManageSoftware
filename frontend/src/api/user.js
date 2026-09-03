import request from '@/utils/request'

export function getUserPage(params) {
  return request.get('/system/user/page', { params })
}

export function createUser(data) {
  return request.post('/system/user', data)
}

export function updateUser(id, data) {
  return request.put(`/system/user/${id}`, data)
}

export function deleteUser(id) {
  return request.delete(`/system/user/${id}`)
}

export function toggleUserStatus(id) {
  return request.put(`/system/user/${id}/status`)
}

export function changePassword(id, data) {
  return request.put(`/system/user/${id}/password`, data)
}
