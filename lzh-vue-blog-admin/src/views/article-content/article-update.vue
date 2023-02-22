<template>
  <div class="article-content" v-highlightjs>
    <el-button icon="el-icon-back" style="border: none;border-radius: 20px" @click="back">返回</el-button>
    <h2 style="font-family: 幼圆;margin-left: 600px">修改文章</h2>
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm"
             style="margin-left: 150px"
    >
      <el-form-item label="标题" prop="title" style="width: 880px">
        <el-input v-model="ruleForm.title"></el-input>
      </el-form-item>
      <el-form-item label="摘要" prop="summary" style="width: 880px">
        <el-input v-model="ruleForm.summary"></el-input>
      </el-form-item>

      <el-form-item label="分类" prop="categoryId" required style="">
        <el-select v-model="ruleForm.categoryId" placeholder="请选择">
          <el-option
            v-for="item in categorys"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="标签" prop="tagIds" required>
        <el-select v-model="ruleForm.tagIds" multiple placeholder="请选择">
          <el-option
            v-for="(item,index) in tags"
            :key="index"
            :label="item.name"
            :value="item.id"
          >
          </el-option>
        </el-select>
      </el-form-item>


      <el-form-item label="允许评论" prop="isComment">
        <el-switch v-model="ruleForm.isComment"
                   :active-value="1" :inactive-value="0"
        ></el-switch>
      </el-form-item>
      <el-form-item label="是否发布" prop="status">
        <el-switch v-model="ruleForm.status"
                   :active-value="0" :inactive-value="2"
        ></el-switch>
      </el-form-item>
      <el-form-item label="是否置顶" prop="isTop" style="">
        <el-switch v-model="ruleForm.isTop"
                   :active-value="1" :inactive-value="0"
        ></el-switch>
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
        <el-button type="success" @click="resetForm('ruleForm')">重置</el-button>
      </el-form-item>
    </el-form>

    <mavon-editor
      v-model="content"
      ref="md"
      @imgAdd="addImg"
    ></mavon-editor>

    <div class="btn" style="margin-top: 30px;float: right;margin-right: 10px">
      <el-button @click="back">取消</el-button>
      <el-button type="danger" @click="submitForm('ruleForm')">确定</el-button>
    </div>
  </div>
</template>

<script>
import { getTags } from '@/api/tag'
import { getCategorys } from '@/api/category'
import { uploadSingleImg } from '@/api/upload'
import { queryArticleById, saveArticle, updateArticle } from '@/api/article'

export default {
  name: 'article-update',
  data() {
    return {
      rules: {
        title: [
          { required: true, message: '请输入活动名称', trigger: 'blur' }
        ],
        category: [
          { required: true, message: '请选择分类', trigger: 'change' }
        ],
        tagIds: [
          { required: true, message: '请选择标签', trigger: 'change' }
        ]
      },
      content: '',
      ruleForm: {},
      tags: [],
      categorys: [],
      fileList: [],
      imgPath: '',
      tagIds: []
    }
  },
  created() {
    getTags().then(res => {
      this.tags = res.data
    })
    getCategorys().then(res => {
      this.categorys = res.data
    })
    queryArticleById(this.$route.params.id).then(res => {
      let data = res.data
      console.log(data)
      this.ruleForm = data

      this.content = this.ruleForm.content

      this.fileList.push({ name: '缩略图', url: data.thumbnail })

      this.ruleForm.isTop = parseInt(data.isTop)
      this.ruleForm.isComment = parseInt(data.isComment)
      this.ruleForm.status = parseInt(data.status)
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
      this.ruleForm.html = html
      this.ruleForm.content = this.content
      updateArticle(this.ruleForm).then(res => {
        if (res.code === 200) {
          this.savePageToLocalStorage()
          this.$message.success('文章修改成功')
          this.$router.push('/blog-manager')
        }
      })
    },
    savePageToLocalStorage() {
      localStorage.setItem('pageNum', this.$route.query.pageNum)
      localStorage.setItem('pageSize', this.$route.query.pageSize)
    },
    back() {
      this.savePageToLocalStorage()
      this.$router.push({ path: '/blog-manager' })
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
