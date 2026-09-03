import request from '@/utils/request'
const base = (projectId) => `/project/${projectId}/task-module`
export const getTaskModules = (projectId) => request.get(base(projectId))
export const createTaskModule = (projectId, data) => request.post(base(projectId), data)
export const updateTaskModule = (projectId, id, data) => request.put(`${base(projectId)}/${id}`, data)
export const deleteTaskModule = (projectId, id) => request.delete(`${base(projectId)}/${id}`)
