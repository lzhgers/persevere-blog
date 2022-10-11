﻿// import axios from 'axios'
// import { Notification, MessageBox, Message } from 'element-ui'
// import store from '@/store'
//
//
// axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8'
// // 创建axios实例
// const service = axios.create({
//   // axios中请求配置有baseURL选项，表示请求URL公共部分
//   baseURL: process.env.VUE_APP_BASE_API,
//   // 超时
//   timeout: 10000
// })
// // request拦截器
// service.interceptors.request.use(config => {
//   // 是否需要设置 token
//   const isToken = (config.headers || {}).isToken === false
//   if (getToken() && !isToken) {
//     config.headers['token'] = getToken() // 让每个请求携带自定义token 请根据实际情况自行修改
//   }
//   // get请求映射params参数
//   if (config.method === 'get' && config.params) {
//     let url = config.url + '?'
//     for (const propName of Object.keys(config.params)) {
//       const value = config.params[propName]
//       var part = encodeURIComponent(propName) + '='
//       if (value !== null && typeof (value) !== 'undefined') {
//         if (typeof value === 'object') {
//           for (const key of Object.keys(value)) {
//             if (value[key] !== null && typeof (value[key]) !== 'undefined') {
//               const params = propName + '[' + key + ']'
//               const subPart = encodeURIComponent(params) + '='
//               url += subPart + encodeURIComponent(value[key]) + '&'
//             }
//           }
//         } else {
//           url += part + encodeURIComponent(value) + '&'
//         }
//       }
//     }
//     url = url.slice(0, -1)
//     config.params = {}
//     config.url = url
//   }
//   return config
// }, error => {
//   console.log(error)
//   Promise.reject(error)
// })
//
// // 响应拦截器
// service.interceptors.response.use(res => {
//   // 未设置状态码则默认成功状态
//   const code = res.data.code || 200
//   // 获取错误信息
//   const msg = errorCode[code] || res.data.msg || errorCode['default']
//   if (code === 401) {
//     MessageBox.confirm('登录状态已过期，您可以继续留在该页面，或者重新登录', '系统提示', {
//       confirmButtonText: '重新登录',
//       cancelButtonText: '取消',
//       type: 'warning'
//     }
//     ).then(() => {
//       store.dispatch('FedLogOut').then(() => {
//         location.href = '/index'
//       })
//     }).catch(() => { })
//     return Promise.reject('无效的会话，或者会话已过期，请重新登录。')
//   } else if (code === 500) {
//     Message({
//       message: msg,
//       type: 'error'
//     })
//     return Promise.reject(new Error(msg))
//   } else if (code !== 200) {
//     Notification.error({
//       title: msg
//     })
//     return Promise.reject('error')
//   } else {
//     // 把字符串total 转换成 数字 total
//     if (res.data.data && res.data.data.total) {
//       res.data.data.total = parseInt(res.data.data.total)
//     }
//     return res.data.data
//   }
// },
// error => {
//   console.log('err' + error)
//   let { message } = error
//   if (message === 'Network Error') {
//     message = '后端接口连接异常'
//   } else if (message.includes('timeout')) {
//     message = '系统接口请求超时'
//   } else if (message.includes('Request failed with status code')) {
//     message = '系统接口' + message.substr(message.length - 3) + '异常'
//   }
//   Message({
//     message: message,
//     type: 'error',
//     duration: 5 * 1000
//   })
//   return Promise.reject(error)
// }
// )
//
// export default service


import axios from 'axios'
import {MessageBox, Message} from 'element-ui';
import {getToken, removeToken} from "./auth";

// 创建axios实例
const service = axios.create({
    baseURL: 'http://localhost:9999',
    timeout: 15000 // 请求超时时间
})
// http request 拦截器
service.interceptors.request.use(
    config => {
        // token 先不处理，后续使用时在完善
        // 是否需要设置 token
        const isToken = (config.headers || {}).isToken === false
        if (getToken() && !isToken) {
            config.headers['token'] = getToken() // 让每个请求携带自定义token 请根据实际情况自行修改
        }

        return config
    },
    err => {
        return Promise.reject(err)
    })
// http response 拦截器
service.interceptors.response.use(
    response => {
        if (response.data.code !== 200) {
            if (response.data.code === 310) {
                return response.data
            }
            if (response.data.code === 401) {
                localStorage.removeItem("userInfo")
                removeToken()
                return response.data
            }
            Message({
                message: response.data.msg,
                type: 'error',
                duration: 5 * 1000
            });
            return Promise.reject(response.data);
        } else {
            return response.data
        }
    },
    error => {
        return Promise.reject(error.response)
    })
export default service