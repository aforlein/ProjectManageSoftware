import request from '@/utils/request'

export function getMapProjects() {
  return request.get('/map/projects')
}
