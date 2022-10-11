import request from "../../utils/request";

/*点赞*/
export function addUserLikeArticle(userId, articleId) {
    return request({
        url: `/like/add/${userId}/${articleId}`,
        headers: {
            isToken: true
        },
        method: 'post'
    })
}

/*查询点赞数*/
export function getLikedCountByArticleId(articleId) {
    return request({
        url: `/like/${articleId}`,
        headers: {
            isToken: true
        },
        method: 'get'
    })
}

// /*查询当前登陆用户是否点赞相关文章*/
// export function getLikedCountByArticleId(articleId) {
//     return request({
//         url: `/like/${articleId}`,
//         headers: {
//             isToken: true
//         },
//         method: 'get'
//     })
// }
