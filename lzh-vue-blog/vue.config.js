const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true
})

// module.exports = { //导出
//   assetsDir: 'static',
//   parallel: false,
//   publicPath: './',
//   devServer: {  //开发环境
//     host: 'localhost',
//     port: 8888,
//   }
// }
