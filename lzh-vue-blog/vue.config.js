const {defineConfig} = require('@vue/cli-service')
module.exports = defineConfig({
    transpileDependencies: true
})






// module.exports = {
//     devServer: {
//         assetsDir: 'static',
//         parallel: false,
//         publicPath: './',
//         proxy: {
//             '/api': {
//                 target: 'http://1.117.218.230:9999',
//                 changeOrigin: true,
//                 pathRewrite: {
//                     '^/api': ''
//                 }
//             }
//         },
//         host: '1.117.218.230',
//         // port: 8808
//         port: 80
//     }
// }
