import request from '@/utils/request'

/* 条件分页查询 */
export function pageList(pageNum, pageSize, title, author, tagId, categoryId) {
  return request({
    url: '/article/pageList',
    method: 'get',
    params: {
      pageNum, pageSize, title, author, tagId, categoryId
    }
  })
}

/* 删除文章 */
export function deleteArticleById(articleId) {
  return request({
    url: `/article/delete/${articleId}`,
    method: 'delete'
  })
}

/* 添加文章 */
export function saveArticle(data) {
  return request({
    url: '/article/save',
    method: 'post',
    data: data
  })
}

/* 添加文章 */
export function queryArticleById(articleId) {
  return request({
    url: `/article/query/${articleId}`,
    method: 'get'
  })
}

/* 修改文章 */
export function updateArticle(data) {
  return request({
    url: '/article/update',
    method: 'put',
    data: data
  })
}

/* 导出文章 */
export function importArticle(data) {
  return request({
    url: '/article/import',
    method: 'post',
    data: data
  })
}
