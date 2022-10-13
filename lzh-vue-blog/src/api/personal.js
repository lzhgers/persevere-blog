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

export function updateUserPassword(pwdForm) {
    return request(({
        url: '/user/updatePassword',
        headers: {
            isToken: true
        },
        data: pwdForm,
        method: 'put'
    }))
}

export function sendEmailCodeToUpdateEmail(emailForm) {
    return request(({
        url: '/user/email/getUpdateEmailCode',
        headers: {
            isToken: true
        },
        data: emailForm,
        method: 'put'
    }))
}

export function checkCode(emailForm) {
    return request(({
        url: '/user/email/checkCode',
        headers: {
            isToken: true
        },
        data: emailForm,
        method: 'post'
    }))
}

export function sendNewEmailCodeToCheckEmail(newEmailForm) {
    return request(({
        url: '/user/email/getNewEmailCode',
        headers: {
            isToken: true
        },
        data: newEmailForm,
        method: 'post'
    }))
}

export function finishEmailUpdate(newEmailForm) {
    return request(({
        url: '/user/email/finishEmailUpdate',
        headers: {
            isToken: true
        },
        data: newEmailForm,
        method: 'post'
    }))
}
