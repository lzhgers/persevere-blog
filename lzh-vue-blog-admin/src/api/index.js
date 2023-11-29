import request from '@/utils/request'

export function queryStatisticsNum() {
    return request({
        url: '/dashboard/queryStatisticsNum',
        method: 'get'
    })
}

export function getVisitByWeek() {
    return request({
        url: process.env.ADMIN_API + '/index/getVisitByWeek',
        method: 'get'
    })
}

export function getBlogCountByTag() {
    return request({
        url: '/dashboard/queryArticleCountByTag',
        method: 'get'
    })
}

export function getBlogCountByCategory() {
    return request({
        url: '/dashboard/queryBlogNumByCategory',
        method: 'get'
    })
}

export function getBlogContributeCount() {
    return request({
        url: process.env.ADMIN_API + '/index/getBlogContributeCount',
        method: 'get'
    })
}


/**
 * @param {Function} func
 * @param {number} wait
 * @return {*}
 */
export function debounce(func, wait) {
    let timeout, args, context, timestamp, result

    const later = function () {
        // 据上一次触发时间间隔
        const last = +new Date() - timestamp

        // 上次被包装函数被调用时间间隔 last 小于设定时间间隔 wait
        if (last < wait && last > 0) {
            timeout = setTimeout(later, wait - last)
        } else {
            timeout = null
            // 如果设定为immediate===true，因为开始边界已经调用过了此处无需调用
            if (!immediate) {
                result = func.apply(context, args)
                if (!timeout) context = args = null
            }
        }
    }

    return function (...args) {
        context = this
        timestamp = +new Date()
        const callNow = immediate && !timeout
        // 如果延时不存在，重新设定延时
        if (!timeout) timeout = setTimeout(later, wait)
        if (callNow) {
            result = func.apply(context, args)
            context = args = null
        }

        return result
    }
}