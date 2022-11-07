import request from "../../utils/request";

// 发送文章评论
export function getAllFriendLink() {
    return request({
        url: '/friendlink/listAll',
        method: 'get',
        headers: {
            isToken: false
        }
    })
}
