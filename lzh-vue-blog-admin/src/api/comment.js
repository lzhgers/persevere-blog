import request from '@/utils/request'

/* 分页条件查询 */
export function pageList(data) {
  return request({
    url: '/comment/pageList',
    method: 'post',
    data: data
  })
}

/* 删除评论 */
export function deleteComment(id) {
  return request({
    url: `/comment/delete/${id}`,
    method: 'delete'
  })
}
