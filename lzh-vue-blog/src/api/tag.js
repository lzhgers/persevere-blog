import request from "../../utils/request";

/*查询所有标签*/
export function listAllTag() {
    return request({
        url: '/tag/listAll',
        method: 'get'
    })
}

export function getTagsByArticleId(articleId) {
    return request({
        url: '/tag/getTagsByArticleId/' + articleId,
        method: 'get',
        headers: {
            isToken: false
        }
    })
}
