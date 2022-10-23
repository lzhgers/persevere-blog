import request from "../../utils/request";

/*关注用户*/
export function subscribeUser(subscribe, beSubscribe) {
    return request({
        url: '/subscribe',
        method: 'post',
        headers: {
            isToken: true
        },
        params: {
            subscribe,
            beSubscribe
        }
    })
}

/*查询关注用户信息*/
export function pageUserSubscribed(userId, pageNum, pageSize) {
    return request({
        url: '/subscribe/pageUserSubscribed/' + userId,
        method: 'get',
        headers: {
            isToken: true
        },
        params: {
            pageNum,
            pageSize
        }
    })
}

/*取消关注*/
export function noSubscribe(subscribe, beSubscribe) {
    return request({
        url: '/subscribe/noSubscribe',
        method: 'put',
        headers: {
            isToken: true
        },
        params: {
            subscribe,
            beSubscribe
        }
    })
}

/*取消关注*/
export function pageUserFan(userId, pageNum, pageSize) {
    return request({
        url: '/fan/pageUserFan',
        method: 'get',
        headers: {
            isToken: true
        },
        params: {
            userId,
            pageNum,
            pageSize
        }
    })
}

/*回关粉丝*/
export function subscribeFan(userId, fanId) {
    return request({
        url: '/fan/subscribeFan',
        method: 'post',
        headers: {
            isToken: true
        },
        params: {
            userId,
            fanId
        }
    })
}

/*取消互相关注*/
export function noSubEach(userId, fanId) {
    return request({
        url: '/fan/noSubEach',
        method: 'put',
        headers: {
            isToken: true
        },
        params: {
            userId,
            fanId
        }
    })
}

/*统计用户粉丝数*/
export function countFans(userId) {
    return request({
        url: '/userStatus/countFans/' + userId,
        method: 'get',
        headers: {
            isToken: false
        }
    })
}

/*统计用户关注数*/
export function countSubscribe(userId) {
    return request({
        url: '/userStatus/countSubscribe/' + userId,
        method: 'get',
        headers: {
            isToken: false
        }
    })
}
