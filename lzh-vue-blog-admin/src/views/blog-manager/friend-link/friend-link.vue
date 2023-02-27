<template>
  <div class="friendlink">
    <div style="margin: 10px">
      <el-input v-model="listQuery.name" placeholder="请输入友链名" style="width: 180px;margin-right: 5px;margin-left: 10px"
                class="filter-item"
      />
      <el-select v-model="selectedStatus" placeholder="友链状态" clearable style="width: 130px;margin-right: 5px"
                 class="filter-item"
                 @clear="clearSelectedStatus"
      >
        <el-option @click.native="selectFriendLink(item.status)" v-for="item in friendLinkStatus"
                   :key="item.status"
                   :label="item.name"
                   :value="item.name"
        />
      </el-select>

      <el-button class="filter-item" type="danger" icon="el-icon-search" @click="searchFriendLink"
                 style="margin-bottom: 5px;"
      >
        查找
      </el-button>


      <el-button class="filter-item" type="danger" icon="el-icon-circle-plus-outline"
                 @click="addFriendLink"
                 style="margin-bottom: 5px;margin-left: 10px"
      >
        添加友链
      </el-button>

      <el-button class="filter-item" type="danger" icon="el-icon-delete"
                 @click="deleteFriendLinkBatch"
                 style="margin-bottom: 5px;margin-left: 10px"
      >
        批量删除
      </el-button>

      <el-tooltip class="item" effect="dark" content="刷新" placement="top" style="float: right">
        <!-- span是button的外层友链 -->
        <el-button class="filter-item" type="" icon="el-icon-refresh-left" @click="getList"
                   style="margin-bottom: 5px;margin-left: 10px;"
                   circle
        ></el-button>
      </el-tooltip>
    </div>

    <!--   添加或编辑友链   -->
    <el-dialog :title="addOrEdit===true?'添加友链':'编辑友链'" :visible.sync="addOrEditDialogFormVisible"
               @close="closeAddOrEditDialog"
    >
      <el-form :model="addOrEditForm" :rules="rules">
        <el-form-item label="网站图标" :label-width="formLabelWidth" prop="name">
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
        <el-form-item label="友链名" :label-width="formLabelWidth" prop="name">
          <el-input v-model="addOrEditForm.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="友链简介" :label-width="formLabelWidth" prop="remark">
          <el-input v-model="addOrEditForm.remark" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="友链URL" :label-width="formLabelWidth" prop="url">
          <el-input v-model="addOrEditForm.url" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="友链状态" :label-width="formLabelWidth" prop="status">
          <el-select v-model="selectedAddOrEditStatus" placeholder="友链状态" clearable
                     style="width: 130px;margin-right: 5px"
                     class="filter-item"
                     @clear="clearAddOrEditSelectedStatus"
          >
            <el-option @click.native="selectAddFriendLink(item.status)" v-for="item in friendLinkStatus"
                       :key="item.status"
                       :label="item.name"
                       :value="item.name"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="排序" :label-width="formLabelWidth" prop="listorder">
          <el-input v-model="addOrEditForm.listorder" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="addOrEditDialogFormVisible = false">取 消</el-button>
        <el-button type="danger" @click="submitAddOrEditFriendLink">确 定</el-button>
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
        prop="avatar"
        label="网站图标"
        min-width="130"
      >
        <template slot-scope="scope">
          <img :src="scope.row.avatar" style="width: 100px;height: 100px"/>
        </template>
      </el-table-column>
      <el-table-column
        prop="name"
        label="友链名"
        min-width="140"
      >
      </el-table-column>
      <el-table-column
        prop="remark"
        label="友链简介"
        width="110"
      >
      </el-table-column>
      <el-table-column
        prop="url"
        label="友链URL"
        min-width="110"
      >
      </el-table-column>
      <el-table-column
        prop="listorder"
        label="排序"
        width="110"
        :sortable="'custom'"
      >
        <template slot-scope="scope">
          <el-tag>{{ scope.row.listorder }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="status"
        label="发布状态"
        width="90"
      >
        <template slot-scope="scope">
          <el-tag :type="getStatusType(scope.row.status)">{{ scope.row.status }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="createTime"
        label="创建时间"
        width="165"
        :sortable="'custom'"
      >
      </el-table-column>
      <el-table-column
        prop="updateTime"
        label="更新时间"
        width="165"
        :sortable="'custom'"
      >
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
import { addOrEditFriendLink, deleteBatchFriendLink, deleteFriendLink, pageList, topFriendLink } from '@/api/friendlink'
import { hideFullScreenLoading, showFullScreenLoading } from '@/utils/loading'
import { uploadSingleImg } from '@/api/upload'

export default {
  name: 'friend-link',
  data() {
    var validatePass = (rule, value, callback) => {
      var n = value.toString() // force the value incase it is not
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

      formLabelWidth: '90px',
      selectedRowIds: [],
      tableData: [],

      fileList: [],

      listQuery: {
        pageNum: 1,
        pageSize: 10,
        name: '',
        status: '',
        sortArr: []
      },
      addOrEditForm: {
        id: '',
        avatar: '',
        remark: '',
        name: '',
        url: '',
        status: '',
        listorder: ''
      },
      selectedAddOrEditStatus: '',

      addOrEdit: true,

      selectedStatus: '',
      total: 0,
      friendLinkStatus: [
        { status: 0, name: '下架' },
        { status: 1, name: '上架' },
        { status: 2, name: '申请' }
      ],

      rules: {
        name: [
          { required: true, message: '请输入友链名', trigger: 'blur' }
        ],
        listorder: [
          { validator: validatePass, trigger: 'change', required: true }
        ],
        url: [
          { required: true, message: '请输入友链URL', trigger: 'blur' }
        ],
        status: [
          { required: true, message: '请选择友链状态', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    handleUpload(img) {
      var formData = new FormData()
      formData.append('img', img.file)
      uploadSingleImg(formData).then(res => {
        this.addOrEditForm.avatar = res.data
        this.$message.success('图片上传成功')
      })
    },

    selectAddFriendLink(status) {
      this.addOrEditForm.status = status
    },
    clearAddOrEditSelectedStatus() {
      this.selectedAddOrEditStatus = ''
      this.addOrEditForm.status = ''
    },
    getStatusType(status) {
      if (status === '下架') {
        return 'info'
      } else if (status === '上架') {
        return 'success'
      } else if (status === '申请') {
        return 'danger'
      }
    },
    addFriendLink() {
      this.addOrEditDialogFormVisible = true
      this.addOrEdit = true
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
    selectFriendLink(status) {
      this.listQuery.status = status
    },
    submitAddOrEditFriendLink() {
      addOrEditFriendLink(this.addOrEditForm).then(res => {
        this.getList()
        this.addOrEditDialogFormVisible = false
        this.$message.success('操作成功')
      })
    },
    deleteFriendLinkBatch() {
      this.$confirm('此操作将永久删除这些友链，是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteBatchFriendLink(this.selectedRowIds).then(res => {
          this.$message.success('操作成功')
          this.getList()
        })
      }).catch(() => {
      })
    },
    searchFriendLink() {
      this.getList()
    },
    handleTop(index, row) {
      this.$confirm('你确定要置顶该友链吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        topFriendLink(row.id, row.listorder).then(res => {
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
        deleteFriendLink(row.id).then(res => {
          this.$message.success('友链删除成功')
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
      this.addOrEditForm.avatar = row.avatar
      this.addOrEditForm.name = row.name
      this.addOrEditForm.remark = row.remark
      this.addOrEditForm.url = row.url

      if (row.status === '上架') {
        this.addOrEditForm.status = '0'
        this.selectedAddOrEditStatus = '上架'
      } else if (row.status === '下架') {
        this.addOrEditForm.status = '1'
        this.selectedAddOrEditStatus = '下架'
      } else if (row.status === '申请') {
        this.addOrEditForm.status = '2'
        this.selectedAddOrEditStatus = '申请'
      }

      this.fileList = [{ name: '缩略图', url: row.avatar }]

      this.addOrEditForm.listorder = row.listorder
    },
    closeAddOrEditDialog() {
      this.addOrEditForm.id = ''
      this.addOrEditForm.name = ''
      this.addOrEditForm.avatar = ''
      this.addOrEditForm.url = ''
      this.addOrEditForm.listorder = 0
      this.addOrEditForm.status = ''
      this.addOrEditForm.remark = ''
      this.selectedAddOrEditStatus = ''
      this.fileList = []
    },
    clearSelectedStatus() {
      this.selectedStatus = ''
      this.listQuery.status = ''
    },
    handleSizeChange(pageSize) {
      this.listQuery.pageSize = pageSize
      this.getList()
    },
    handleCurrentChange(pageNum) {
      this.listQuery.pageNum = pageNum
      this.getList()
    },
    handleHeadAddClass({ column }) {
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
