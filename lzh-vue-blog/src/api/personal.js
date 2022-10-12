import request from "../../utils/request";

/*点赞*/
export function getUserInfo(userId) {
    return request({
        url: '/user/getUserById/',
        headers: {
            isToken: true
        },
        params: {
            userId
        },
        method: 'get'
    })
}

export function updateUserAvatar(userId, avatar) {
    return request({
        url: '/user/avatar',
        headers: {
            isToken: true
        },
        params: {
            userId,
            avatar
        },
        method: 'post'
    })
}

