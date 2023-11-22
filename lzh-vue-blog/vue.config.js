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
//                 target: 'http://118.89.125.143:9999',
//                 changeOrigin: true,
//                 pathRewrite: {
//                     '^/api': ''
//                 }
//             }
//         },
//         host: '0.0.0.0',
//         // port: 8808
//         port: 80
//     }
// }
