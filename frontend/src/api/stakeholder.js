import request from '@/utils/request'
const base = (projectId) => `/project/${projectId}/stakeholder`
export const getStakeholders = (projectId) => request.get(base(projectId))
export const createStakeholder = (projectId, data) => request.post(base(projectId), data)
export const updateStakeholder = (projectId, id, data) => request.put(`${base(projectId)}/${id}`, data)
export const deleteStakeholder = (projectId, id) => request.delete(`${base(projectId)}/${id}`)
