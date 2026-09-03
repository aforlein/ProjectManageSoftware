import request from '@/utils/request'
const base = (projectId) => `/project/${projectId}/deploy-record`
export const getDeployRecords = (projectId) => request.get(base(projectId))
export const createDeployRecord = (projectId, data) => request.post(base(projectId), data)
export const updateDeployRecord = (projectId, id, data) => request.put(`${base(projectId)}/${id}`, data)
export const deleteDeployRecord = (projectId, id) => request.delete(`${base(projectId)}/${id}`)
