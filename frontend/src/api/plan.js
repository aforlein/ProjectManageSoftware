import request from '@/utils/request'
const base = (projectId) => `/project/${projectId}/plan`
export const getPlans = (projectId) => request.get(base(projectId))
export const createPlan = (projectId, data) => request.post(base(projectId), data)
export const updatePlan = (projectId, id, data) => request.put(`${base(projectId)}/${id}`, data)
export const deletePlan = (projectId, id) => request.delete(`${base(projectId)}/${id}`)
