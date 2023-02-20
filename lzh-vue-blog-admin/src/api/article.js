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

/* 删除文章 */
export function saveArticle(data) {
  return request({
    url: '/article/save',
    method: 'post',
    data: data
  })
}
