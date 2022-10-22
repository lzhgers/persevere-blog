import request from "@/utils/request";

/*条件分页查询文章*/
export function pageListArticle(title, summary, categoryId, pageNum, pageSize) {
    return request({
        url: '/admin/article/pageList',
        method: 'get',
        headers: {
            isToken: false
        },
        params: {
            title,
            summary,
            categoryId,
            pageNum,
            pageSize
        }
    })
}

export function deleteArticleByArticleId(articleId) {
    return request({
        url: `/admin/article/${articleId}`,
        method: 'delete',
        headers: {
            isToken: false
        }
    })
}

export function updateCommentTop(articleId, isComment, isTop) {
    return request({
        url: `/admin/article/updateCommentTop`,
        method: 'put',
        headers: {
            isToken: false
        },
        params: {articleId, isComment, isTop}
    })
}

export function getCommentTopById(articleId) {
    return request({
        url: `/admin/article/getCommentTop`,
        method: 'get',
        headers: {
            isToken: false
        },
        params: {
            articleId
        }
    })
}
