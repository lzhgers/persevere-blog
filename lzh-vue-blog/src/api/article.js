import request from "../../utils/request";

/*查询所有文章*/
export function listAllArticles() {
    return request({
        url: '/article/listAll',
        method: 'get',
        headers: {
            isToken: false
        }
    })
}

/*分页查询所有文章*/
export function pageAllArticles(pageNum, pageSize, userId, keyword) {
    return request({
        url: '/article/pageListAll',
        method: 'get',
        params: {
            pageNum,
            pageSize,
            userId,
            keyword
        },
        headers: {
            isToken: false
        }
    })
}

export function getArticle(id, userId) {
    return request({
        url: '/article/' + id,
        method: 'get',
        headers: {
            isToken: false
        },
        params: {
            userId
        }
    })
}

// 修改文章
export function updateArticle(data) {
    return request({
        url: '/article',
        method: 'put',
        data: data
    })
}

// 添加文章
export function addArticle(data) {
    return request({
        url: '/article',
        method: 'post',
        data: data,
        traditional: true
    })
}

//更新访问量
export function updateViewCount(articleId) {
    return request({
        url: '/article/updateViewCount/' + articleId,
        headers: {
            isToken: false
        },
        method: 'put'
    })

}

//获取文章访问量top4
export function getViewCountTop4Article() {
    return request({
        url: '/article/view/top4',
        headers: {
            isToken: false
        },
        method: 'get'
    })

}

//获取网站相关信息
export function getBlogInfo() {
    return request({
        url: '/blog/getInfo',
        headers: {
            isToken: false
        },
        method: 'get'
    })
}

//获取所有轮播图
export function getConfigCarouselImg() {
    return request({
        url: '/carouselImg/listByConfig',
        headers: {
            isToken: false
        },
        method: 'get'
    })
}

//根据文章id查询收藏数
export function getCollectNumByArticleId(articleId) {
    return request({
        url: '/article/countCollect',
        headers: {
            isToken: false
        },
        method: 'get',
        params: {
            articleId
        }
    })
}

//根据文章id查询收藏数
export function getCollectStmt(articleId, userId) {
    return request({
        url: '/article/getCollectStmt',
        headers: {
            isToken: false
        },
        method: 'get',
        params: {
            articleId,
            userId
        }
    })
}

/*获取用户发布文章*/
export function pageUserPublishArticle(userId, pageNum, pageSize) {
    return request({
        url: `/article/pageUserPublishArticle/${userId}`,
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

/*获取用户草稿*/
export function pageUserRoughArticle(userId, pageNum, pageSize) {
    return request({
        url: '/article/pageUserRoughArticle',
        headers: {
            isToken: true
        },
        method: 'get',
        params: {
            userId,
            pageNum,
            pageSize
        }
    })
}

/*获取用户草稿*/
export function getArticleByArticleId(articleId) {
    return request({
        url: '/article/getArticleByArticleId/' + articleId,
        headers: {
            isToken: true
        },
        method: 'get'
    })
}

/*删除文章或草稿*/
export function deleteArticle(articleId) {
    return request({
        url: '/article/' + articleId,
        headers: {
            isToken: true
        },
        method: 'delete'
    })
}
