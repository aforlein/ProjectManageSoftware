import request from '@/utils/request'

export function getRoleList() {
  return request.get('/system/role/list')
}

export function getRolePage() {
  return request.get('/system/role/page')
}

export function createRole(data) {
  return request.post('/system/role', data)
}

export function updateRole(id, data) {
  return request.put(`/system/role/${id}`, data)
}

export function deleteRole(id) {
  return request.delete(`/system/role/${id}`)
}
