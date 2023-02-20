import request from '@/utils/request'

export function getCategorys() {
  return request({
    url: '/category/list',
    method: 'get',
  })
}
