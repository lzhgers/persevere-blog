import request from '@/utils/request'

export function pageList(data) {
  return request({
    url: '/carouselImg/pageList',
    method: 'post',
    data: data
  })
}

export function deleteCarouselImg(id) {
  return request({
    url: `/carouselImg/delete/${id}`,
    method: 'delete'
  })
}

/* 置顶友链 */
export function topCarouselImg(id, listorder) {
  return request({
    url: '/carouselImg/top',
    method: 'put',
    params: { id, listorder }
  })
}

/* 添加或编辑友链 */
export function addOrEditCarouselImg(data) {
  return request({
    url: '/carouselImg/addOrEdit',
    method: 'post',
    data: data
  })
}

/* 添加或编辑友链 */
export function deleteBatchCarouselImg(data) {
  return request({
    url: '/carouselImg/deleteBatch',
    method: 'delete',
    data: data
  })
}

/* 添加或编辑友链 */
export function configCarouselImg(data) {
  return request({
    url: '/carouselImg/config',
    method: 'post',
    data: data
  })
}
