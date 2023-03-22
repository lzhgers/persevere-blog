<template>
  <div class="app-container" style="background-color: #fff">

    <Header></Header>

    <el-form ref="form" :model="form" label-width="90px" style="margin-top: 10px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="文章标题" prop="title">
            <el-input
                v-model="form.title"
                placeholder="请输入文章标题"
                maxlength="30"
            />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="分类">
            <el-select v-model="form.categoryId" placeholder="请选择">
              <el-option @click.native="selectCategoryId(category.id)"
                         v-for="category in categoryList"
                         :key="category.id"
                         :label="category.name"
                         :value="category.id"
              />
              <!-- <el-option :key="'1'" label="女" :value="'1'" /> -->
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="标签">
            <el-select v-model="form.tagIds" placeholder="请选择" multiple>
              <el-option
                  v-for="tag in tagList"
                  :key="tag.id"
                  :label="tag.name"
                  :value="tag.id"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="文章摘要">
            <el-input v-model="form.summary" type="textarea"/>
          </el-form-item>
        </el-col>
        <!--        <el-col :span="6">-->
        <!--          <el-form-item label="允许评论">-->
        <!--            <el-radio-group v-model="form.isComment">-->
        <!--              <el-radio :key="'0'" :label="'0'">正常</el-radio>-->
        <!--              <el-radio :key="'1'" :label="'1'">停用</el-radio>-->
        <!--            </el-radio-group>-->
        <!--          </el-form-item>-->
        <!--        </el-col>-->
        <!--        <el-col :span="6">-->
        <!--          <el-form-item label="是否置顶">-->
        <!--            <el-radio-group v-model="form.isTop">-->
        <!--              <el-radio :key="'0'" :label="'0'">是</el-radio>-->
        <!--              <el-radio :key="'1'" :label="'1'">否</el-radio>-->
        <!--            </el-radio-group>-->
        <!--          </el-form-item>-->
        <!--        </el-col>-->

      </el-row>
      <el-row :gutter="20"/>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="缩略图">
            <el-upload
                :file-list="fileList"
                class="upload-demo"
                list-type="picture"
                drag
                name="img"
                action="upload"
                :on-remove="fileRemove"
                :limit="1"
                :http-request="handleUpload"
                :on-exceed="onExceed"
            >
              <i class="el-icon-upload"/>
              <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
              <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>
            </el-upload>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item>
            <el-button type="primary" size="medium" @click="handleSubmit">保存</el-button>
          </el-form-item>

        </el-col>
      </el-row>
      <el-row>
        <mavon-editor ref="md" v-model="form.content" @imgAdd="addImg"/>
      </el-row>
    </el-form>
  </div>
</template>

<script>
import {listAllCategory} from '@/api/category'
import {uploadImg} from '@/api/upload'
import {getArticle, updateArticle} from '@/api/article'
import {listAllTag} from '@/api/tag'
import {getTagsByArticleId} from "@/api/tag";

import Header from "@/components/Header";

export default {
  name: 'publishview',
  components: {
    Header
  },
  data() {
    return {
      form: {
        title: '',
        thumbnail: '',
        isTop: '1',
        isComment: '0',
        content: '',
        createBy: -1,
        categoryId: '',
        tagIds: []
      },
      categoryList: [],
      tagList: [],
      aId: -1,
      fileList: []
    }
  },
  watch: {
    $route: {
      handler: function (route) {
        this.aId = route.query && route.query.id
      },
      immediate: true
    }
  },
  created() {
    this.aId = this.$route.params.id

    let userInfo = JSON.parse(localStorage.getItem("userInfo"));
    if (userInfo) {

      getTagsByArticleId(this.aId).then(res => {
        this.tagList = res.data
      })

      this.form.createBy = userInfo.id
      this.getCategoryAndTag();
      if (this.aId) {
        this.getArticle()
      }
    } else {
      this.$message({
        message: '请先登录',
        type: 'error'
      });
      this.$router.push("/login?type=w")
    }
  },
  methods: {
    selectCategoryId(categoryId) {
      this.form.categoryId = categoryId
    },

    getArticle() {
      getArticle(this.aId).then(response => {
        this.form = response.data
        this.fileList.push({name: '缩略图', url: response.data.thumbnail})
      })
    },

    handleSubmit() {
      if (localStorage.getItem("userInfo")) {
        this.form.status = '0';
        updateArticle(this.form).then(response => {
          this.$message({
            message: '博客修改成功',
            type: 'success'
          });
          window.close()
          this.$router.push({path: '/myBlog/mypublish'})
        });
      } else {
        this.$message({
          message: '请先登录',
          type: 'error'
        });
        this.$router.push("/login?type=w")
      }

      // window.opener = null;
      // window.open("about:blank", "_self");
      // window.close();
    },
    onExceed() {
      this.$message.error('只能上传一个图片')
    },
    handleUpload(img) {
      uploadImg(img.file).then(response => {
        this.form.thumbnail = response.data
        this.fileList.push({name: img.file.name, url: response.data})
      }).catch(error => {
        this.$message.error(error.msg)
      })
    },
    fileRemove(file, fileList) {
      this.fileList.pop()
    },
    // 绑定@imgAdd event
    addImg(pos, file) {
      // 第一步.将图片上传到服务器.
      uploadImg(file).then(response => {
        this.$refs.md.$img2Url(pos, response.data)
      }).catch(error => {
        this.$message.error(error.msg)
      })
    },
    getCategoryAndTag() {
      listAllCategory().then((response) => {
        this.categoryList = response.data
      })
      listAllTag().then(response => {
        this.tagList = response.data
      })
    },
    beforeAvatarUpload(file) {
      const isJPG = file.type === 'image/jpeg'
      const isLt2M = file.size / 1024 / 1024 < 2

      if (!isJPG) {
        this.$message.error('上传头像图片只能是 JPG 格式!')
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!')
      }
      return isJPG && isLt2M
    }
  }
}
</script>
<style scoped>
div .upload-demo {
  /* padding-left: 80px; */
}

.el-col .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;

  overflow: hidden;
}

.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}

.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>
