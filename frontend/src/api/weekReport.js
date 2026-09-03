import request from '@/utils/request'
const base = (projectId) => `/project/${projectId}/week-report`
export const getWeekReports = (projectId) => request.get(base(projectId))
export const createWeekReport = (projectId, data) => request.post(base(projectId), data)
export const updateWeekReport = (projectId, id, data) => request.put(`${base(projectId)}/${id}`, data)
export const deleteWeekReport = (projectId, id) => request.delete(`${base(projectId)}/${id}`)
