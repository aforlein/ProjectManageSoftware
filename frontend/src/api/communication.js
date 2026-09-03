import request from '@/utils/request'
const base = (projectId) => `/project/${projectId}/communication`
export const getCommunications = (projectId) => request.get(base(projectId))
export const createCommunication = (projectId, data) => request.post(base(projectId), data)
export const updateCommunication = (projectId, id, data) => request.put(`${base(projectId)}/${id}`, data)
export const deleteCommunication = (projectId, id) => request.delete(`${base(projectId)}/${id}`)
