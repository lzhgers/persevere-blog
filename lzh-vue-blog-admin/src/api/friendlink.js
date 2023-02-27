import request from '@/utils/request'

export function pageList(data) {
  return request({
    url: '/friendlink/pageList',
    method: 'post',
    data: data
  })
}

export function deleteFriendLink(id) {
  return request({
    url: `/friendlink/delete/${id}`,
    method: 'delete'
  })
}

/* 置顶友链 */
export function topFriendLink(id, listorder) {
  return request({
    url: '/friendlink/top',
    method: 'put',
    params: { id, listorder }
  })
}

/* 添加或编辑友链 */
export function addOrEditFriendLink(data) {
  return request({
    url: '/friendlink/addOrEdit',
    method: 'post',
    data: data
  })
}

/* 添加或编辑友链 */
export function deleteBatchFriendLink(data) {
  return request({
    url: '/friendlink/deleteBatch',
    method: 'delete',
    data: data
  })
}
