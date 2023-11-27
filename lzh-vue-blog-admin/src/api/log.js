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
