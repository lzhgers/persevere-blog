import request from '@/utils/request'

/**
 * 条件分页查询用户日志
 * @param listQuery
 * @returns {*}
 */
export function pageList(listQuery) {
    return request({
        url: '/log/queryUserLogPage',
        method: 'post',
        data: listQuery
    })
}

/**
 * 条件分页查询操作日志
 * @param listQuery
 * @returns {*}
 */
export function queryOperateLogPage(listQuery) {
    return request({
        url: '/log/queryOperateLogPage',
        method: 'post',
        data: listQuery
    })
}

/**
 * 条件分页查询异常日志
 * @param listQuery
 * @returns {*}
 */
export function queryAbnormalLogPage(listQuery) {
    return request({
        url: '/log/queryAbnormalLogPage',
        method: 'post',
        data: listQuery
    })
}

/**
 * 根据日志id查询异常详情
 * @param id
 * @returns {*}
 */
export function queryAbnormalLogDetailById(id) {
    return request({
        url: `/log/queryAbnormalLogDetail/${id}`,
        method: 'get'
    })
}
