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
export function pageAllArticles(pageNum, pageSize) {
    return request({
        url: '/article/pageListAll',
        method: 'get',
        params: {
            pageNum,
            pageSize
        },
        headers: {
            isToken: false
        }
    })
}

export function getArticle(id) {
    return request({
        url: '/article/' + id,
        method: 'get',
        headers: {
            isToken: false
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
