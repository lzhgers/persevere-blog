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
export function pageAllArticles(pageNum, pageSize, userId) {
    return request({
        url: '/article/pageListAll',
        method: 'get',
        params: {
            pageNum,
            pageSize,
            userId
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
        data: data
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

//获取文章访问量top10
export function getViewCountTop4Article() {
    return request({
        url: '/article/viewCount/top4',
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
