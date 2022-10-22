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

/*获取用户收藏文章*/
export function getUserCollectArticle(userId, pageNum, pageSize) {
    return request({
        url: `/collect/pageArticleByUserId/${userId}`,
        headers: {
            isToken: true
        },
        method: 'get',
        params: {
            pageNum,
            pageSize
        }
    })
}
