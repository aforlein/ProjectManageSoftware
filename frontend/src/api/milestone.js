import request from '@/utils/request'

const base = (projectId) => `/project/${projectId}/milestone`
export const getMilestones = (projectId) => request.get(base(projectId))
export const createMilestone = (projectId, data) => request.post(base(projectId), data)
export const updateMilestone = (projectId, id, data) => request.put(`${base(projectId)}/${id}`, data)
export const deleteMilestone = (projectId, id) => request.delete(`${base(projectId)}/${id}`)
