import request from "../../utils/request";

/*查询所有文章不同发布时间*/
export function listDiffDate() {
    return request({
        url: '/article/listDiffDate',
        method: 'get',
        headers: {
            isToken: false
        }
    })
}
/*根据不同时间查询文章*/
export function listArticleByDate(date) {
    return request({
        url: '/article/listArticleByDate',
        method: 'get',
        headers: {
            isToken: false
        },
        params: {
            date
        }
    })
}
