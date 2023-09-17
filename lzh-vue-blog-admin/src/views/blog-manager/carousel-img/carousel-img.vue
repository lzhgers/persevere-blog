<template>
  <div class="friendlink">
    <div style="margin: 10px">
      <el-input v-model="listQuery.title" placeholder="请输入轮播图名"
                style="width: 180px;margin-right: 5px;margin-left: 10px"
                class="filter-item"
      />
      <el-select v-model="selectedStatus" placeholder="轮播图状态" clearable style="width: 130px;margin-right: 5px"
                 class="filter-item"
                 @clear="clearSelectedStatus"
      >
        <el-option @click.native="selectCarouselImg(item.status)" v-for="item in carouselImgStatus"
                   :key="item.status"
                   :label="item.name"
                   :value="item.name"
        />
      </el-select>

      <el-button class="filter-item" type="danger" icon="el-icon-search" @click="searchCarouselImg"
                 style="margin-bottom: 5px;"
      >
        查找
      </el-button>

      <el-button class="filter-item" type="danger" icon="el-icon-circle-plus-outline"
                 @click="addCarouselImg"
                 style="margin-bottom: 5px;margin-left: 10px"
      >
        添加轮播图
      </el-button>

      <el-button class="filter-item" type="danger" icon="el-icon-delete"
                 @click="deleteCarouselImgBatch"
                 style="margin-bottom: 5px;margin-left: 10px"
      >
        批量删除
      </el-button>

      <el-button class="filter-item" type="danger" icon="el-icon-delete"
                 @click="configCarouselImgBatch"
                 style="margin-bottom: 5px;margin-left: 10px"
      >
        轮播图配置
      </el-button>

      <el-dialog title="轮播图配置" :visible.sync="configImgDialogFormVisible">
        <el-form :model="configImgForm" :rules="rules">
          <el-form-item label="轮播图数量" :label-width="formLabelWidth" prop="count">
            <el-input v-model="configImgForm.count" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="轮播图排序" :label-width="formLabelWidth" prop="order">
            <el-select v-model="orderStatus" placeholder="轮播图排序" clearable
                       style="width: 130px;margin-right: 5px"
                       class="filter-item"
                       @clear="clearOrderStatus"
            >
              <el-option @click.native="selectCarouselImgOrder(item.status)" v-for="item in carouselImgOrderStatus"
                         :key="item.status"
                         :label="item.name"
                         :value="item.name"
              />
            </el-select>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="configImgDialogFormVisible = false">取 消</el-button>
          <el-button type="danger" @click="configCarouselImg">确 定</el-button>
        </div>
      </el-dialog>

      <el-tooltip class="item" effect="dark" content="刷新" placement="top" style="float: right">
        <!-- span是button的外层轮播图 -->
        <el-button class="filter-item" type="" icon="el-icon-refresh-left" @click="getList"
                   style="margin-bottom: 5px;margin-left: 10px;"
                   circle
        ></el-button>
      </el-tooltip>
    </div>

    <!--   添加或编辑轮播图   -->
    <el-dialog :title="addOrEdit===true?'添加轮播图':'编辑轮播图'" :visible.sync="addOrEditDialogFormVisible"
               @close="closeAddOrEditDialog"
    >
      <el-form :model="addOrEditForm" :rules="rules">
        <el-form-item label="轮播图" :label-width="formLabelWidth" prop="name">
          <el-upload
              :multiple="false"
              :limit="1"
              class="upload-demo"
              action="#"
              :file-list="fileList"
              list-type="picture"
              :http-request="handleUpload"
          >
            <el-button size="small" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">只能上传一张图片</div>
          </el-upload>
        </el-form-item>
        <el-form-item label="轮播图名" :label-width="formLabelWidth" prop="title">
          <el-input v-model="addOrEditForm.title" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="轮播图简介" :label-width="formLabelWidth" prop="remarks">
          <el-input v-model="addOrEditForm.remarks" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="轮播图URL" :label-width="formLabelWidth" prop="url">
          <el-input v-model="addOrEditForm.url" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="轮播图状态" :label-width="formLabelWidth" prop="status">
          <el-select v-model="selectedAddOrEditStatus" placeholder="轮播图状态" clearable
                     style="width: 130px;margin-right: 5px"
                     class="filter-item"
                     @clear="clearAddOrEditSelectedStatus"
          >
            <el-option @click.native="selectAddCarouselImg(item.status)" v-for="item in carouselImgStatus"
                       :key="item.status"
                       :label="item.name"
                       :value="item.name"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="排序" :label-width="formLabelWidth" prop="sort">
          <el-input v-model="addOrEditForm.sort" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="addOrEditDialogFormVisible = false">取 消</el-button>
        <el-button type="danger" @click="submitAddOrEditCarouselImg">确 定</el-button>
      </div>
    </el-dialog>

    <el-table
        border
        ref="multipleTable"
        :data="tableData"
        style="width: 100%"
        @selection-change="handleSelectionChange"
        @sort-change="changeSort"
        :header-cell-class-name="handleHeadAddClass"
    >
      <el-table-column
          type="selection"
          width="55"
      >
      </el-table-column>
      <el-table-column
          prop="id"
          label="ID"
          min-width="80"
      >
      </el-table-column>
      <el-table-column
          prop="img"
          label="缩略图"
          min-width="145"
      >
        <template slot-scope="scope">
          <img :src="scope.row.url" style="width: 140px;height: 90px"/>
        </template>
      </el-table-column>
      <el-table-column
          prop="title"
          label="轮播图标题"
          min-width="100"
      >
      </el-table-column>
      <el-table-column
          prop="remarks"
          label="轮播图简介"
          width="290"
      >
      </el-table-column>
      <el-table-column
          prop="url"
          label="轮播图URL"
          min-width="270"
      >
      </el-table-column>
      <el-table-column
          prop="sort"
          label="排序"
          width="110"
          :sortable="'custom'"
      >
        <template slot-scope="scope">
          <el-tag>{{ scope.row.sort }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column
          prop="status"
          label="发布状态"
          width="90"
      >
        <template slot-scope="scope">
          <el-tag :type="getStatusType(scope.row.status)">{{ scope.row.status === 0 ? '启用' : '禁用' }}</el-tag>
        </template>
      </el-table-column>

      <el-table-column
          fixed="right"
          label="操作"
          min-width="210"
      >
        <template slot-scope="scope">
          <el-button
              size="mini"
              type="warning"
              @click="handleTop(scope.$index, scope.row)"
          >置顶
          </el-button>
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
        :current-page="listQuery.pageNum"
        :page-sizes="[5, 10, 15, 20]"
        :page-size="listQuery.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
    >
    </el-pagination>
  </div>
</template>

<script>
import {
  addOrEditCarouselImg, configCarouselImg,
  deleteBatchCarouselImg,
  deleteCarouselImg,
  pageList,
  topCarouselImg
} from '@/api/carouselImg'
import {hideFullScreenLoading, showFullScreenLoading} from '@/utils/loading'
import {uploadSingleImg} from '@/api/upload'

export default {
  name: 'carousel-img',
  data() {
    var validatePass = (rule, value, callback) => {
      var n = value.toString()
      var n1 = Math.abs(n), n2 = parseInt(n, 10)
      if (!isNaN(n1) && n2 === n1 && n1.toString() === n) {
        callback()
      } else {
        callback(new Error('排序字段只能输入自然数'))
      }
    }
    return {
      addOrEditDialogFormVisible: false,
      editDialogFormVisible: false,
      configImgDialogFormVisible: false,

      formLabelWidth: '100px',
      selectedRowIds: [],
      tableData: [],

      fileList: [],

      listQuery: {
        pageNum: 1,
        pageSize: 10,
        title: '',
        status: '',
        sortArr: []
      },
      configImgForm: {
        count: 5,
        order: 'desc'
      },
      addOrEditForm: {
        id: '',
        remarks: '',
        title: '',
        url: '',
        img: '',
        status: -1,
        sort: ''
      },
      selectedAddOrEditStatus: '',

      addOrEdit: true,

      selectedStatus: '',
      orderStatus: '降序',

      total: 0,
      carouselImgStatus: [
        {status: 0, name: '启用'},
        {status: 1, name: '禁用'},
      ],
      carouselImgOrderStatus: [
        {status: 0, name: '升序'},
        {status: 1, name: '降序'},
      ],

      rules: {
        name: [
          {required: true, message: '请输入轮播图名', trigger: 'blur'}
        ],
        sort: [
          {validator: validatePass, trigger: 'change', required: true}
        ],
        url: [
          {required: true, message: '请输入轮播图URL', trigger: 'blur'}
        ],
        status: [
          {required: true, message: '请选择轮播图状态', trigger: 'blur'}
        ],
        order: [
          {required: true, message: '请选择轮播图排序', trigger: 'blur'}
        ],
        count: [
          {required: true, message: '请输入轮播图数量', trigger: 'blur'}
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    configCarouselImg() {
      if (this.orderStatus === '降序') {
        this.configImgForm.order = 'desc';
      } else if (this.orderStatus === '升序') {
        this.configImgForm.order = 'asc';
      }
      configCarouselImg(this.configImgForm).then(res => {
        if (res.code === 200) {
          this.$message.success('轮播图配置成功');
        }
      });
    },
    selectCarouselImgOrder(order) {
      if (order === 0) {
        this.orderStatus = '升序'
      } else if (order === 1) {
        this.orderStatus = '降序'
      }
    },
    configCarouselImgBatch() {
      this.configImgDialogFormVisible = true
    },
    handleUpload(img) {
      var formData = new FormData()
      formData.append('img', img.file)
      uploadSingleImg(formData).then(res => {
        this.addOrEditForm.img = res.data
        this.addOrEditForm.url = res.data
        this.$message.success('图片上传成功')
      })
    },

    selectAddCarouselImg(status) {
      this.addOrEditForm.status = status
    },
    clearAddOrEditSelectedStatus() {
      this.selectedAddOrEditStatus = ''
      this.addOrEditForm.status = ''
    },
    getStatusType(status) {
      if (status === 0) {
        return 'success'
      } else if (status === 1) {
        return 'info'
      }
    },
    addCarouselImg() {
      this.addOrEditDialogFormVisible = true
      this.addOrEdit = true
      this.addOrEditForm.title = ''
      this.addOrEditForm.url = ''
      this.addOrEditForm.img = ''
      this.addOrEditForm.remarks = ''
    },
    getList(page = 1) {
      showFullScreenLoading()
      if (page !== 1) {
        this.listQuery.pageNum -= 1
        pageList(this.listQuery).then(res => {
          this.tableData = res.data.rows
          this.total = parseInt(res.data.total)
          hideFullScreenLoading()
        }).catch(() => {
          hideFullScreenLoading()
        })
        return
      }
      pageList(this.listQuery).then(res => {
        this.tableData = res.data.rows
        this.total = parseInt(res.data.total)
        hideFullScreenLoading()
      }).catch(() => {
        hideFullScreenLoading()
      })
    },
    selectCarouselImg(status) {
      this.listQuery.status = status
    },
    submitAddOrEditCarouselImg() {
      if (!this.selectedAddOrEditStatus) {
        this.$message.error('请选择轮播图状态');
        return
      }
      addOrEditCarouselImg(this.addOrEditForm).then(res => {
        this.getList()
        this.addOrEditDialogFormVisible = false
        this.$message.success('操作成功')
      });
    },
    deleteCarouselImgBatch() {
      this.$confirm('此操作将永久删除这些轮播图，是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteBatchCarouselImg(this.selectedRowIds).then(res => {
          this.$message.success('操作成功')
          this.getList()
        })
      }).catch(() => {
      })
    },
    searchCarouselImg() {
      this.getList()
    },
    handleTop(index, row) {
      this.$confirm('你确定要置顶该轮播图吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        topCarouselImg(row.id, row.sort).then(res => {
          this.$message.success('操作成功')
          this.getList()
        })
      }).catch((res) => {
      })
    },
    handleDelete(index, row) {
      this.$confirm('此操作将永久删除这些博客, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteCarouselImg(row.id).then(res => {
          this.$message.success('轮播图删除成功')
          if (this.total % this.listQuery.pageSize === 1) {
            this.getList(-1)
          } else {
            this.getList()
          }
        })
      }).catch(() => {
      })
    },
    handleEdit(index, row) {
      this.addOrEditDialogFormVisible = true
      this.addOrEditForm.id = row.id
      this.addOrEditForm.img = row.img
      this.addOrEditForm.title = row.title
      this.addOrEditForm.remarks = row.remarks
      this.addOrEditForm.url = row.url

      if (row.status === 0) {
        this.addOrEditForm.status = 0
        this.selectedAddOrEditStatus = '启用'
      } else if (row.status === 1) {
        this.addOrEditForm.status = 1
        this.selectedAddOrEditStatus = '禁用'
      }

      this.fileList = [{name: '缩略图', url: row.img}]

      this.addOrEditForm.sort = row.sort
    },
    closeAddOrEditDialog() {
      this.addOrEditForm.id = ''
      this.addOrEditForm.title = ''
      this.addOrEditForm.img = ''
      this.addOrEditForm.url = ''
      this.addOrEditForm.sort = 0
      this.addOrEditForm.status = ''
      this.addOrEditForm.remark = ''
      this.selectedAddOrEditStatus = ''
      this.fileList = []
    },
    clearSelectedStatus() {
      this.selectedStatus = ''
      this.listQuery.status = ''
    },
    clearOrderStatus() {
      this.configImgForm.order = ''
      this.orderStatus = ''
    },
    handleSizeChange(pageSize) {
      this.listQuery.pageSize = pageSize
      this.getList()
    },
    handleCurrentChange(pageNum) {
      this.listQuery.pageNum = pageNum
      this.getList()
    },
    handleHeadAddClass({column}) {
      this.listQuery.sortArr.forEach(item => {
        if (item.prop === column.property) {
          column.order = item.order
        }
      })
    },
    changeSort(column, prop, order) {
      if (column.prop) {
        if (this.listQuery.sortArr.length > 0) {
          this.listQuery.sortArr.forEach((item, index) => {
            if (item.prop === column.prop) {
              this.listQuery.sortArr[index].order = column.order
            }
            let a = this.listQuery.sortArr.some(item => {
              return item.prop === column.prop
            })
            if (!a) {
              this.listQuery.sortArr.push({
                prop: column.prop,
                order: column.order
              })
            }
          })
        } else {
          this.listQuery.sortArr.push({
            prop: column.prop,
            order: column.order
          })
        }
      }
      this.getList()//请求后端获取数据，this.queryParams.sortArr为放多列排序数据的参数，格式为[{prop: 'ptState',order:'descending'}]
    },
    handleSelectionChange(val) {
      this.selectedRowIds = this.$refs.multipleTable.selection.map((item) => item.id)
    }
  }
}
</script>

<style scoped>

</style>
