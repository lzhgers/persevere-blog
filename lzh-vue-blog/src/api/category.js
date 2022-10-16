import request from "../../utils/request";

/*查询所有分类*/
export function listAllCategory() {
    return request({
        url: '/category/listAll',
        method: 'get',
        headers: {
            isToken: false
        }
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

/*根据类别id查询文章*/
export function getArticleByCategoryId(categoryId) {
    return request({
        url: `/category/getArticleByCategoryId/${categoryId}`,
        method: 'get',
        headers: {
            isToken: false
        }
    })
}
