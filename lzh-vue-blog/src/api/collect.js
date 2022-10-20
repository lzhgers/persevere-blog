import request from "../../utils/request";


/*收藏*/
export function addUserCollectArticle(userId, articleId) {
    return request({
        url: `/article/addCollect/${userId}/${articleId}`,
        headers: {
            isToken: true
        },
        method: 'post'
    })
}
