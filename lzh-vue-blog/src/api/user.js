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
