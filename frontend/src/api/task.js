import request from '@/utils/request'
const base = (projectId) => `/project/${projectId}/task`
export const getTasks = (projectId) => request.get(base(projectId))
export const createTask = (projectId, data) => request.post(base(projectId), data)
export const updateTask = (projectId, id, data) => request.put(`${base(projectId)}/${id}`, data)
export const deleteTask = (projectId, id) => request.delete(`${base(projectId)}/${id}`)
