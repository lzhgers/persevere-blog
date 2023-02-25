import request from '@/utils/request'

//查询所有标签
export function getTags() {
  return request({
    url: '/tag/list',
    method: 'get'
  })
}

// 条件分页查询标签
export function pageList(listQuery) {
  return request({
    url: '/tag/pageList',
    method: 'post',
    data: listQuery
  })
}

//添加标签
export function addTag(name, sort) {
  return request({
    url: '/tag/add',
    method: 'post',
    params: { name, sort }
  })
}

//删除标签
export function deleteTag(id) {
  return request({
    url: `/tag/delete/${id}`,
    method: 'delete'
  })
}

//更新标签
export function editTag(data) {
  return request({
    url: '/tag/edit',
    method: 'put',
    data: data
  })
}

//批量删除标签
export function deleteBatch(ids) {
  return request({
    url: '/tag/deleteBatch',
    method: 'delete',
    data: ids
  })
}
