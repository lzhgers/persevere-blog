<template>
  <div class="app-container">
    <div class="filter-container" style="margin-bottom: 5px;">
      <el-input v-model="listQuery.title" placeholder="请输入文章标题" style="width: 130px;margin-right: 5px"
                class="filter-item"
      />
      <el-input v-model="listQuery.author" placeholder="请输入作者" style="width: 130px;margin-right: 5px"
                class="filter-item"
      />
      <el-select v-model="selectedTagName" placeholder="请选择标签" clearable style="width: 130px;margin-right: 5px"
                 class="filter-item"
                 @clear="clearTag"
      >
        <el-option @click.native="selectTag(item.id)" v-for="item in tags" :key="item.id" :label="item.name"
                   :value="item.name"
        />
      </el-select>
      <!--      <el-select v-model="listQuery.importance" placeholder="是否原创" clearable style="width: 130px;margin-right: 5px" class="filter-item">-->
      <!--        <el-option v-for="item in importanceOptions" :key="item" :label="item" :value="item"/>-->
      <!--      </el-select>-->
      <!--      <el-select v-model="listQuery.importance" placeholder="是否发布" clearable style="width: 130px;margin-right: 5px" class="filter-item">-->
      <!--        <el-option v-for="item in importanceOptions" :key="item" :label="item" :value="item"/>-->
      <!--      </el-select>-->
      <el-select @clear="clearCategory" v-model="selectedCategoryName" placeholder="请选择分类" clearable class="filter-item"
                 style="margin-right: 5px;width: 130px"
      >
        <el-option @click.native="selectCategoryId(item.id)" v-for="item in categorys"
                   :key="item.id"
                   :value="item.name"
        />
      </el-select>
      <el-button class="filter-item" type="danger" icon="el-icon-search" @click="searchArticle"
                 style="margin-bottom: 5px;margin-left: 10px"
      >
        查找
      </el-button>
      <el-button class="filter-item" type="warning" icon="el-icon-refresh" @click="resetCondition"
                 style="margin-bottom: 5px;margin-left: 10px"
      >
        重置
      </el-button>

      <br>
      <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-edit"
                 @click="$router.push('/article-content')"
      >
        添加文章
      </el-button>


      <el-button class="filter-item" type="primary" icon="el-icon-upload2"
                 @click.native="handleUpload" style="margin-right: 10px"
      >
        本地上传
      </el-button>
      <el-dialog
        title="本地博客上传"
        :visible.sync="localUploadVisible"
      >
        <div class="tipBox">
          <div class="tip">导入须知</div>
          <div class="tipItem">1）如果你的Markdown文档里面的图片是本地，需要选择本地图片，然后提交到图片服务器</div>
          <div class="tipItem">2）含有本地图片一定需要提前上传图片，否则会出现图片无法替换的问题</div>
          <div class="tipItem">3）如果你的Markdown文档里面的图片不是本地，直接选择博客文件上传即可</div>
          <div class="tipItem">4）目前支持Markdown文件批量上传，步骤是先提交所有图片，在提交全部的博客文件</div>
          <div class="tipItem">5）因为网络或者服务器性能等不可抗拒的原因，因此不推荐一次上传太多</div>
        </div>

        <!-- 上传图片 -->
        <el-upload
          class="upload-demo2"
          ref="uploadPicture"
          name="filedatas"
          action="post"
          :auto-upload="false"
          multiple
          :on-change="onChange"
          :on-remove="onRemove"
        >
          <el-button slot="trigger" size="small" type="primary">选取本地图片</el-button>
          <el-button style="margin-left: 10px;" size="small" type="success" @click="submitPictureUpload">提交到图片服务器
          </el-button>
        </el-upload>

        <!-- 上传md文件 -->
        <el-upload
          class="upload-demo"
          ref="uploadFile"
          name="filedatas"
          :headers="importHeaders"
          action="post"
          :auto-upload="false"
          :on-change="onChangeMd"
          :on-remove="onRemoveMd"
          multiple
        >
          <el-button slot="trigger" size="small" type="primary">选取博客文件</el-button>
          <el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">提交到服务器</el-button>
        </el-upload>

      </el-dialog>

      <el-button :loading="downloadLoading" class="filter-item" type="primary" icon="el-icon-download"
      @click="importSelected">
        导出选中
      </el-button>

      <el-button :loading="downloadLoading" class="filter-item" type="primary" icon="el-icon-delete"
      @click="deleteBatch">
        批量删除
      </el-button>
    </div>

    <!--  文章列表  -->
    <el-table
      border
      ref="multipleTable"
      :data="tableData"
      tooltip-effect="dark"
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <el-table-column
        type="selection"
        width="55"
      >
      </el-table-column>
      <el-table-column
        prop="id"
        label="ID"
        width="90"
      >
      </el-table-column>
      <el-table-column
        prop="thumbnail"
        label="标题图"
      >
        <template v-slot="scope">
          <img :src="scope.row.thumbnail" alt="" style="width: 130px;height: 90px">
        </template>
      </el-table-column>
      <el-table-column
        prop="title"
        label="标题"
        width="120"
      >
      </el-table-column>
      <el-table-column
        prop="author"
        label="作者"
        show-overflow-tooltip
      >
      </el-table-column>
      <el-table-column
        prop="category"
        label="分类"
        show-overflow-tooltip
      >
        <template v-slot="scope">
          <el-tag v-show="scope.row.category.name != null" type="warning">{{ scope.row.category.name }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="tags"
        label="标签"
        show-overflow-tooltip
      >
        <template v-slot="scope">
          <div v-for="tag in scope.row.tags">
            <el-tag style="margin-left: 5px;margin-top:5px">{{ tag.name }}</el-tag>
          </div>
        </template>
      </el-table-column>
      <el-table-column
        width="150px"
        prop="manager"
        label="操作"
        show-overflow-tooltip
      >
        <template v-slot="scope">
          <el-button
            size="mini"
            @click="handleEdit(scope.$index, scope.row)"
          >编辑
          </el-button>
          <el-button
            size="mini"
            type="danger"
            @click="handleDelete(scope.$index, scope.row)"
          >删除
          </el-button>
        </template>
      </el-table-column>

    </el-table>

    <!--  分页  -->
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="pageNum"
      :page-sizes="[5, 10, 15, 20]"
      :page-size="pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
    >
    </el-pagination>

  </div>
</template>

<script>
import { parseTime } from '@/utils'
import { pageList } from '@/api/article'
import { getTags } from '@/api/tag'
import { getCategorys } from '@/api/category'
import { showFullScreenLoading, hideFullScreenLoading } from '@/utils/loading'
import { deleteArticleById } from '@/api/article'
import { getToken } from '@/utils/auth'
import { uploadMulImg } from '@/api/upload'
import { Loading } from 'element-ui'
import { uploadSingleMd } from '@/api/upload'
import request from '@/utils/request'

export default {
  name: 'articleManager',
  filters: {
    statusFilter(status) {
      const statusMap = {
        published: 'success',
        draft: 'info',
        deleted: 'danger'
      }
      return statusMap[status]
    }
  },
  data() {
    return {
      categorys: [],
      tags: [],

      listQuery: {},
      tableData: [],
      multipleSelection: [],
      dialogPvVisible: false,

      downloadLoading: false,

      total: 0,
      pageSize: 10,
      pageNum: 1,

      localUploadVisible: false,
      fileList: [],
      mdList: [],

      imgUrlMap: null,

      importHeaders: {
        Authorization: getToken()
      },

      selectedRowIds: [],

      uploadLoading: null, //文件上传loading
      //----------
      selectedCategoryName: '',
      selectedTagName: ''
    }
  },
  created() {
    var pageNum = localStorage.getItem('pageNum')
    var pageSize = localStorage.getItem('pageSize')
    if (pageNum !== null) {
      this.pageNum = parseInt(pageNum)
      localStorage.removeItem('pageNum')
    }
    if (pageSize !== null) {
      this.pageSize = parseInt(pageSize)
      localStorage.removeItem('pageSize')
    }

    showFullScreenLoading()
    this.getList()
    this.getTags()
    this.getCategorys()
    hideFullScreenLoading()
  },
  methods: {
    deleteBatch() {

    },
    importSelected() {

    },
    resetCondition() {
      this.listQuery = {}
      this.selectedCategoryName = ''
      this.selectedTagName = ''
    },
    //md文件上传
    submitUpload() {
      this.openLoading()
      const formData = new FormData()
      for (let i = 0; i < this.mdList.length; i++) {
        formData.append('mdFile', this.mdList[i].raw)
      }
      formData.append('imgUrlMap', JSON.stringify(this.imgUrlMap))
      uploadSingleMd(formData).then(response => {
        this.localUploadVisible = false
        this.$message.success('博客上传成功')
        this.closeLoading()
      }).catch(() => {
        this.closeLoading()
      })
    },
    onRemoveMd(file, fileList) {
      this.mdList = fileList
    },
    onChange(file, fileList) {
      this.fileList = fileList
    },
    onRemove(file, fileList) {
      this.fileList = fileList
    },
    onChangeMd(file, fileList) {
      this.mdList = fileList
    },
    //上传图片
    submitPictureUpload() {
      this.openLoading()
      const formData = new FormData()
      for (let i = 0; i < this.fileList.length; i++) {
        formData.append('file', this.fileList[i].raw)
      }
      if (formData.getAll('file').length === 0) {
        this.$message.error('请选择要上传的图片')
        this.closeLoading()
        return
      }
      uploadMulImg(formData).then(response => {
        if (response.code === 200) {
          this.$message.success('图片上传成功')
          this.imgUrlMap = response.data
        } else {
          this.$message.error('图片上传失败')
        }
        this.closeLoading()
      }).catch(() => {
        this.closeLoading()
      })
    },

    selectTag(tagId) {
      this.listQuery.tagId = tagId
    },
    selectCategoryId(categoryId) {
      this.listQuery.categoryId = categoryId
    },
    clearCategory() {
      this.listQuery.categoryId = ''
    },
    clearTag() {
      this.listQuery.tagId = ''
    },
    searchArticle() {
      this.getList(-1)
    },
    getTags() {
      getTags().then(response => {
        this.tags = response.data
      })
    },
    getCategorys() {
      showFullScreenLoading()
      getCategorys().then(response => {
        this.categorys = response.data
      })
      hideFullScreenLoading()
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.getList()
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.getList()
    },
    handleEdit(index, row) {
      this.$router.push({
        path: '/article-update/' + row.id,
        query: { pageNum: this.pageNum, pageSize: this.pageSize }
      })
    },
    handleDelete(index, row) {
      this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteArticleById(row.id + '').then(response => {
          if (response.code === 200) {
            this.$message({
              type: 'success',
              message: '删除成功!'
            })
            setTimeout(() => {
              if (this.pageNum === 1) {
                this.getList()
              } else {
                let more = this.total % this.pageSize
                if (more === 1) {
                  this.pageNum = this.pageNum - 1
                  this.getList()
                } else {
                  this.getList()
                }
              }
            }, 100)
          }
        })
      }).catch(() => {
      })
    },
    handleSelectionChange(val) {
      this.selectedRowIds = this.$refs.multipleTable.selection.map((item) => item.id);
    },
    getList(help = 0) {
      if (help === -1) {
        this.listLoading = true
        this.pageNum = 1
        pageList(this.pageNum, this.pageSize, this.listQuery.title, this.listQuery.author, this.listQuery.tagId, this.listQuery.categoryId).then(response => {
          this.tableData = response.data.rows
          this.total = parseInt(response.data.total)
          setTimeout(() => {
            this.listLoading = false
          }, 1.5 * 1000)
        })
      } else {
        this.listLoading = true
        pageList(this.pageNum, this.pageSize, this.listQuery.title, this.listQuery.author, this.listQuery.tagId, this.listQuery.categoryId).then(response => {
          this.tableData = response.data.rows
          this.total = parseInt(response.data.total)
          setTimeout(() => {
            this.listLoading = false
          }, 1.5 * 1000)
        })
      }
    },

    //文件上传加载提示：开启、关闭
    openLoading() {
      this.uploadLoading = Loading.service({
        lock: true,
        text: '正在努力上传中……'
      })
    },
    closeLoading() {
      this.uploadLoading.close()
    },

    handleUpload: function() {
      this.localUploadVisible = true
    }
  }
}
</script>
<style>
.tipBox {
  margin-bottom: 30px;
}

.tip {
  font-size: 14px;
  font-weight: bold;
  color: #808080;
}

.tipItem {
  line-height: 22px;
  color: #A9A9A9;
}

.upload-demo {
  margin-top: 50px;
}
</style>
