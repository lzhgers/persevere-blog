import request from "../../utils/request";

// 发送文章评论
export function sendComment(type, articleId, rootId, toCommentId, toCommentUserId, content) {
    return request({
        url: '/comment',
        method: 'post',
        data: {
            "articleId": articleId,
            "type": type,
            "rootId": rootId,
            "toCommentId": toCommentId,
            "toCommentUserId": toCommentUserId,
            "content": content
        }
    })
}


export function getArticleComment(query) {
    return request({
        url: '/comment/commentList',
        method: 'get',
        headers: {
            isToken: true
        },
        params: query
    })
}


export function getLinkComment(query) {
    return request({
        url: '/comment/linkCommentList',
        method: 'get',
        params: query
    })
}

export function getArticleCommentNum(articleId) {
    return request({
        url: '/article/countCommentsByArticleId/' + articleId,
        headers: {
            isToken: true
        },
        method: 'get'
    })
}

export function getCommentByCommentId(commentId) {
    return request({
        url: '/comment/article/' + commentId,
        headers: {
            isToken: true
        },
        method: 'get'
    })
}
