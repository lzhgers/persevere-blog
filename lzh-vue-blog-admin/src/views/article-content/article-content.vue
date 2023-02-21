<template>
  <div class="article-content">
    <el-button icon="el-icon-back" style="border: none;border-radius: 20px" @click="back">返回</el-button>
    <h2 style="font-family: 幼圆;margin-left: 600px">添加文章</h2>
    <el-form :model="ruleForm" ref="ruleForm" label-width="100px" class="demo-ruleForm"
             style="margin-left: 150px"
    >
      <el-form-item label="标题" prop="title" required style="width: 880px">
        <el-input v-model="ruleForm.title"></el-input>
      </el-form-item>
      <el-form-item label="摘要" prop="summary" style="width: 880px">
        <el-input v-model="ruleForm.summary"></el-input>
      </el-form-item>

      <el-form-item label="分类" prop="category" required style="">
        <el-select v-model="ruleForm.category" placeholder="请选择">
          <el-option
            v-for="item in categorys"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="标签" prop="tag" required>
        <el-select v-model="ruleForm.tag" multiple placeholder="请选择">
          <el-option
            v-for="item in tags"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          >
          </el-option>
        </el-select>
      </el-form-item>


      <el-form-item label="允许评论" prop="isComment">
        <el-switch v-model="ruleForm.isComment"></el-switch>
      </el-form-item>
      <el-form-item label="是否发布" prop="status">
        <el-switch v-model="ruleForm.status"></el-switch>
      </el-form-item>
      <el-form-item label="是否置顶" prop="isTop" style="">
        <el-switch v-model="ruleForm.isTop"></el-switch>
      </el-form-item>

      <el-form-item label="标题图" prop="thumbnail" style="width: 460px">
        <el-upload
          class="upload-demo"
          drag
          :file-list="fileList"
          list-type="picture"
          action="post"
          :http-request="handleUpload"
          :on-remove="fileRemove"
          :on-exceed="onExceed"
          :limit="1"
        >
          <i class="el-icon-upload"></i>
          <div class="el-upload__text">将图片拖到此处，或<em>点击上传</em></div>
        </el-upload>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="submitForm('ruleForm')">添加文章</el-button>
        <el-button @click="resetForm('ruleForm')">重置</el-button>
      </el-form-item>
    </el-form>

    <mavon-editor
      v-model="content"
      ref="md"
      @imgAdd="addImg"
    ></mavon-editor>
  </div>
</template>

<script>
import { getTags } from '@/api/tag'
import { getCategorys } from '@/api/category'
import { uploadSingleImg } from '@/api/upload'
import { saveArticle } from '@/api/article'

export default {
  name: 'article-content',
  data() {
    return {
      content: '',
      ruleForm: {},
      tags: [],
      categorys: [],
      fileList: [],
      imgPath: ''
    }
  },
  created() {
    getTags().then(res => {
      this.tags = res.data
    })
    getCategorys().then(res => {
      this.categorys = res.data
    })
  },
  methods: {
    // 绑定@imgAdd event
    addImg(pos, file) {
      // 第一步.将图片上传到服务器.
      var formData = new FormData()
      formData.append('img', file)
      uploadSingleImg(formData).then(response => {
        this.$refs.md.$img2Url(pos, response.data)
      }).catch(error => {
        this.$message.error(error.msg)
      })
    },
    resetForm() {
      this.ruleForm = {}
    },
    submitForm() {
      this.ruleForm.thumbnail = this.imgPath
      let html = this.$refs.md.d_render
      this.ruleForm.content = this.content
      this.ruleForm.html = html
      saveArticle(this.ruleForm).then(res => {
        if (res.code === 200) {
          this.$message.success('文章添加成功')
          this.$router.push('/blog-manager')
        }
      })
    },
    back() {
      this.$router.push('/blog-manager')
    },
    fileRemove(file, fileList) {
      this.fileList.pop()
    },
    handleUpload(img) {
      var formData = new FormData()
      formData.append('img', img.file)
      uploadSingleImg(formData).then(res => {
        this.imgPath = res.data
        this.$message.success('图片上传成功')
      })
    },
    onExceed() {
      this.$message.error('只能上传一个图片')
    }
  }
}
</script>

<style scoped>

</style>
