import request from '@/utils/request'
const base = (projectId) => `/project/${projectId}/knowledge`
export const getKnowledgeList = (projectId) => request.get(base(projectId))
export const createKnowledge = (projectId, data) => request.post(base(projectId), data)
export const updateKnowledge = (projectId, id, data) => request.put(`${base(projectId)}/${id}`, data)
export const deleteKnowledge = (projectId, id) => request.delete(`${base(projectId)}/${id}`)
