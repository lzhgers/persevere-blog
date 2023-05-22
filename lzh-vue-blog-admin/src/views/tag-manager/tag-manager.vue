<template>
  <div class="tag">
    <div class="condition" style="margin: 10px">
      <el-input v-model="listQuery.name" placeholder="请输入标签名" style="width: 180px;margin-right: 5px"
                class="filter-item"
      />
      <el-button class="filter-item" type="danger" icon="el-icon-search" @click="searchTag"
                 style="margin-bottom: 5px;margin-left: 10px"
      >
        查找
      </el-button>


      <el-button class="filter-item" type="danger" icon="el-icon-circle-plus-outline" @click="addTag"
                 style="margin-bottom: 5px;margin-left: 10px"
      >
        添加标签
      </el-button>

      <!--   添加标签   -->
      <el-dialog title="添加标签" :visible.sync="addDialogFormVisible">
        <el-form :model="addForm" :rules="rules">
          <el-form-item label="标签名" :label-width="formLabelWidth" prop="name">
            <el-input v-model="addForm.name" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="排序" :label-width="formLabelWidth" prop="sort">
            <el-input v-model="addForm.sort" autocomplete="off"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="cancelAddTag">取 消</el-button>
          <el-button type="danger" @click="submitAddTag">确 定</el-button>
        </div>
      </el-dialog>

      <!--   编辑标签   -->
      <el-dialog title="添加标签" :visible.sync="editDialogFormVisible">
        <el-form :model="editForm" :rules="rules">
          <el-form-item label="标签名" :label-width="formLabelWidth" prop="name">
            <el-input v-model="editForm.name" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="排序" :label-width="formLabelWidth" prop="sort">
            <el-input v-model="editForm.sort" autocomplete="off"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="editDialogFormVisible = false">取 消</el-button>
          <el-button type="danger" @click="submitEditTag">确 定</el-button>
        </div>
      </el-dialog>

      <el-button class="filter-item" type="danger" icon="el-icon-delete" @click="deleteTagBatch"
                 style="margin-bottom: 5px;margin-left: 10px"
      >
        批量删除
      </el-button>

      <el-tooltip class="item" effect="dark" content="刷新" placement="top" style="float: right">
        <!-- span是button的外层标签 -->
        <el-button class="filter-item" type="" icon="el-icon-refresh-left" @click="getList"
                   style="margin-bottom: 5px;margin-left: 10px;"
                   circle
        ></el-button>
      </el-tooltip>

    </div>
    <el-table
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
          width="80"
      >
      </el-table-column>
      <el-table-column
          prop="name"
          label="标签名"
          width="160"
      >
      </el-table-column>
      <el-table-column
          prop="clickNum"
          label="点击数"
          width="120"
          :sortable="'custom'"
      >
      </el-table-column>
      <el-table-column
          prop="sort"
          label="排序"
          width="120"
          :sortable="'custom'"
      >
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
import {addTag, deleteBatch, deleteTag, editTag, pageList} from '@/api/tag'
import {showFullScreenLoading, hideFullScreenLoading} from '@/utils/loading'
import {topCategory} from '@/api/category'

export default {
  name: 'tag-manager',
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
      listQuery: {
        name: '',
        pageNum: 1,
        pageSize: 10,
        sortArr: []
      },
      tableData: [],

      addForm: {
        name: '',
        sort: 0
      },
      editForm: {
        id: -1,
        name: '',
        sort: 0
      },

      pageNum: 1,
      pageSize: 10,
      total: 0,

      selectedRowIds: [],

      addDialogFormVisible: false,
      editDialogFormVisible: false,
      formLabelWidth: '90px',
      rules: {
        name: [
          {required: true, message: '请输入标签名', trigger: 'blur'}
        ],
        sort: [
          {validator: validatePass, trigger: 'change', required: true}
        ]
      }

    }
  },
  created() {
    this.getList()
  },
  methods: {
    handleTop(index, row) {
      this.$confirm('你确定要将该标签置顶吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        topCategory(row.id, row.sort).then(res => {
          this.getList()
        }).catch(() => {
        })
      }).catch(() => {
      })
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
      console.log(this.listQuery.sortArr)
      this.getList()//请求后端获取数据，this.queryParams.sortArr为放多列排序数据的参数，格式为[{prop: 'ptState',order:'descending'}]
    },

    deleteTagBatch() {
      let ids = this.selectedRowIds
      if (ids === null || ids.length === 0) {
        this.$message.error('请选择要删除的标签')
        return
      }
      this.$confirm('此操作将永久删除这些标签, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteBatch(ids).then(res => {
          this.getList()
          this.$message.success('批量删除成功')
        })
      }).catch(() => {
      })
    },
    submitAddTag() {
      addTag(this.addForm.name, this.addForm.sort).then(res => {
        this.addDialogFormVisible = false
        this.getList()
        this.$message.success('标签添加成功')
      })
    },
    cancelAddTag() {
      this.addDialogFormVisible = false
      this.addForm = {}
    },
    submitEditTag() {
      if (this.editForm.name === '') {
        this.$message.error('标签名不能为空')
        return
      }
      editTag(this.editForm).then(res => {
        this.$message.success('标签更新成功')
        this.editDialogFormVisible = false
        this.getList()
      })
    },
    addTag() {
      this.addDialogFormVisible = true

    },
    handleEdit(index, row) {
      this.editDialogFormVisible = true
      this.editForm.id = row.id
      this.editForm.name = row.name
      this.editForm.sort = row.sort
    },
    handleDelete(index, row) {
      this.$confirm('此操作将永久删除该标签, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteTag(row.id).then(res => {
          this.getList()
          this.$message.success('标签删除成功')
        })
      }).catch(() => {
      })
    },
    searchTag() {
      this.getList()
    },

    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.getList()
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.getList()
    },
    getList() {
      showFullScreenLoading()
      setTimeout(() => {
        this.listQuery.pageNum = this.pageNum
        this.listQuery.pageSize = this.pageSize
        pageList(this.listQuery).then(res => {
          console.log(res)
          let data = res.data
          this.tableData = data.rows
          this.total = parseInt(data.total)
          hideFullScreenLoading()
        }).catch(() => {
          hideFullScreenLoading()
        })
      }, 100)

    },
    handleSelectionChange(val) {
      this.selectedRowIds = this.$refs.multipleTable.selection.map((item) => item.id)
    }

  }

}
</script>

<style scoped>

</style>
