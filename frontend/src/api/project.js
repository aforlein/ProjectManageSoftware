import request from '@/utils/request'

export function getProjectPage(params) {
  return request.get('/project/page', { params })
}

export function getProject(id) {
  return request.get(`/project/${id}`)
}

export function createProject(data) {
  return request.post('/project', data)
}

export function updateProject(id, data) {
  return request.put(`/project/${id}`, data)
}

export function deleteProject(id) {
  return request.delete(`/project/${id}`)
}
