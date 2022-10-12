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

/*更新用户头像*/
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

/*更新用户信息*/
export function updateUserInfo(userForm) {
    return request({
        url: '/user/updateUser',
        headers: {
            isToken: true
        },
        data: userForm,
        method: 'put'
    })
}

