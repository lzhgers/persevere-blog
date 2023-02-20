import request from '@/utils/request'

export function uploadMulImg(formData) {
  return request({
    url: '/upload/mulPic',
    method: 'post',
    headers: { 'Content-Type': 'multipart/form-data' },
    data: formData
  })
}

export function uploadSingleImg(formData) {
  return request({
    url: '/upload/singlePic',
    method: 'post',
    headers: { 'Content-Type': 'multipart/form-data' },
    data: formData
  })
}

export function uploadSingleMd(formData) {
  return request({
    url: '/upload/singleMd',
    method: 'post',
    headers: { 'Content-Type': 'multipart/form-data' },
    data: formData
  })
}
