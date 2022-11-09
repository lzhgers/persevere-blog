import request from "../../utils/request";

/*查询所有分类*/
export function listChatCommunicationByUsername(fromUserName, toUserName) {
    return request({
        url: `/chatCommunication/listAll/${fromUserName}/${toUserName}`,
        method: 'get',
        headers: {
            isToken: true
        }
    })
}

/*查询未读用户信息*/
export function getMsgByFromAndToName(toUserName, users) {
    return request({
        url: `/chatCommunication/getMsg/${toUserName}`,
        method: 'post',
        headers: {
            isToken: true
        },
        data: users
    })
}
/*查询未读用户信息*/
export function updateIsRead(toUserName, fromUserName) {
    return request({
        url: `/chatCommunication/updateIsRead/${toUserName}/${fromUserName}`,
        method: 'put',
        headers: {
            isToken: true
        }
    })
}
