<template>
  <div class="app-container">
    <div>
      <el-button size="medium" type="success" plain @click="downLoadFile">下载</el-button>
    </div>

    <el-button @click="fun">点击</el-button>
    <mavon-editor v-highlightjs v-html="text"></mavon-editor>
    <mavon-editor
      v-model="content"
      ref="md"
    ></mavon-editor>
    <mavon-editor
      v-model="text"
      ref="md1"
    ></mavon-editor>
  </div>
</template>

<script>

import { default as TurndownService } from '_turndown@7.1.1@turndown'
import { exportArticle } from '@/api/article'

export default {
  data() {
    return {
      text: '',
      content: '## 5.2 案例\n' +
        '\n' +
        '![image-20220711154016490](正则表达式.assets/image-20220711154016490.png)\n' +
        '\n' +
        '4. 请在字符串中检索商品编号,形式如:12321-333999111这样的号码,要求满足前面是一个五位数,然后一个-号,然后是一个九位数,连续的每三位要相同\n' +
        '\n' +
        '   ```java\n' +
        '   public static void main(String[] args) {\n' +
        '   \n' +
        '       String content = "12312-888999444";\n' +
        '   \n' +
        '       String regStr = "\\\\d{5}-(\\\\d)\\\\1{2}(\\\\d)\\\\2{2}(\\\\d)\\\\3{2}";\n' +
        '   \n' +
        '       Pattern pattern = Pattern.compile(regStr);\n' +
        '       Matcher matcher = pattern.matcher(content);\n' +
        '       while (matcher.find()) {\n' +
        '           System.out.println(matcher.group(0));\n' +
        '       }\n' +
        '   }\n' +
        '   ```\n' +
        '\n' +
        '5. 结巴去重案例'
    }
  },
  methods: {
    fun() {
      var html = this.$refs.md.d_render
      console.log(html)

      const TurndownService = require('turndown').default
      var turndownService = new TurndownService()
      //转换成markdown格式
      var markdown = turndownService.turndown(html)
      this.text = markdown
      console.log(this.text)
    },
    downLoadFile() {
      exportArticle('D:\\Typora笔记\\Redis.md', 'aa.md').then(res => {
        debugger
        const blob = new Blob([res.data])
        var fileName = res.headers['content-disposition'];
        fileName = fileName.split(';')[1];
        fileName = fileName.split('filename=')[1]
        if ('download' in document.createElement('a')) {
          const link = document.createElement('a')
          link.download = fileName
          link.style.display = 'none'
          link.href = URL.createObjectURL(blob)
          document.body.appendChild(link)
          link.click()
          URL.revokeObjectURL(link.href)
          document.body.removeChild(link)
        } else {
          navigator.msSaveBlob(blob, fileName)
        }
      })
    }

  }
}

</script>

