import request from '@/utils/request'

/* 查询所有分类 */
export function getCategorys() {
  return request({
    url: '/category/list',
    method: 'get'
  })
}

/* 分类条件查询 */
export function pageListCategory(data) {
  return request({
    url: '/category/pageList',
    method: 'post',
    data: data
  })
}

/* 删除分类 */
export function deleteCategory(id) {
  return request({
    url: `/category/delete/${id}`,
    method: 'delete'
  })
}

/* 批量删除分类 */
export function deleteBatchCategory(categoryIds) {
  return request({
    url: '/category/deleteBatch',
    method: 'delete',
    data: categoryIds
  })
}

/* 添加分类 */
export function saveCategory(data) {
  return request({
    url: '/category/saveCategory',
    method: 'post',
    data: data
  })
}

/* 编辑分类 */
export function editCategory(data) {
  return request({
    url: '/category/editCategory',
    method: 'put',
    data: data
  })
}

/* 置顶分类 */
export function topCategory(id, sort) {
  return request({
    url: `/category/top/${id}`,
    method: 'put',
    params: {sort}
  })
}
