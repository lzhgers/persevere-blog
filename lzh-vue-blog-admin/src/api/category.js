import request from "@/utils/request";

/*查询所有分类*/
export function listAllCategory() {
    return request({
        url: '/admin/category/listAll',
        method: 'get',
        headers: {
            isToken: false
        }
    })
}
