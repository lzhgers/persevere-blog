import request from "../../utils/request";

/*查询所有文章*/
export function listAllCategory() {
    return request({
        url: '/category/listAll',
        method: 'get'
    })
}

/*查询所有文章*/
export function getCategoryByArticleId(articleId) {
    return request({
        url: '/category/getCategoryByArticleId',
        method: 'get',
        headers: {
            isToken: false
        },
        params: {articleId}
    })
}
