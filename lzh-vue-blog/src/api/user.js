import request from "../../utils/request";

export function login(userName, password) {
    return request({
        url: '/user/login',
        method: 'post',
        headers: {
            isToken: false
        },
        data: {'userName': userName, 'password': password}
    })
}

export function logout() {
    return request({
        url: '/user/logout',
        method: 'post',
        headers: {
            isToken: true
        }
    })
}

export function regist(userName, email, password, code) {
    return request({
        url: '/user/regist',
        method: 'post',
        headers: {
            isToken: false
        },
        data: {"userName": userName, "email": email, "password": password, 'code': code}
    })
}

export function getEmailCode(email) {
    return request({
        url: '/user/email/getCode/' + email,
        method: 'post',
        headers: {
            isToken: false
        }
    })
}

export function getUserByArticleId(articleId) {
    return request({
        url: '/user/getUserByArticleId',
        method: 'get',
        headers: {
            isToken: true
        },
        params: {articleId}
    })
}

export function getUserById(userId) {
    return request({
        url: '/user/getUserById',
        method: 'get',
        headers: {
            isToken: true
        },
        params: {userId}
    })
}

export function checkEmail(email) {
    return request({
        url: '/user/email/checkEmail',
        method: 'post',
        headers: {
            isToken: false
        },
        data: {
            "email": email
        }
    })
}

export function checkCode(email, code) {
    return request({
        url: '/user/email/checkRePwdCode',
        method: 'post',
        headers: {
            isToken: false
        },
        data: {
            "email": email,
            "code": code
        }
    })
}

export function rePassword(email, newPassword, conPassword) {
    return request({
        url: '/user/rePassword',
        method: 'put',
        headers: {
            isToken: false
        },
        data: {
            "email": email,
            "newPassword": newPassword,
            "conPassword": conPassword
        }
    })
}

export function getRePwdCode(email) {
    return request({
        url: '/user/email/rePasswordCode',
        method: 'post',
        headers: {
            isToken: false
        },
        data: {
            "email": email,
        }
    })
}

export function showInfo(userId) {
    return request({
        url: '/userStatus/showInfo/' + userId,
        method: 'get',
        headers: {
            isToken: true
        }
    })
}

export function countLikedByUserId(userId) {
    return request({
        url: '/userStatus/countLiked/' + userId,
        method: 'get',
        headers: {
            isToken: false
        }
    })
}
