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
