<template>
  <div class="category">
    <div style="margin: 10px">
      <el-input v-model="listQuery.name" placeholder="请输入分类名" style="width: 180px;margin-right: 5px;margin-left: 10px"
                class="filter-item"
      />
      <el-button class="filter-item" type="danger" icon="el-icon-search" @click="searchCategory"
                 style="margin-bottom: 5px;"
      >
        查找
      </el-button>


      <el-button class="filter-item" type="danger" icon="el-icon-circle-plus-outline" @click="addCategory"
                 style="margin-bottom: 5px;margin-left: 10px"
      >
        添加分类
      </el-button>

      <el-button class="filter-item" type="danger" icon="el-icon-delete" @click="deleteCategoryBatch"
                 style="margin-bottom: 5px;margin-left: 10px"
      >
        批量删除
      </el-button>

      <el-tooltip class="item" effect="dark" content="刷新" placement="top" style="float: right">
        <!-- span是button的外层分类 -->
        <el-button class="filter-item" type="" icon="el-icon-refresh-left" @click="getList"
                   style="margin-bottom: 5px;margin-left: 10px;"
                   circle
        ></el-button>
      </el-tooltip>
    </div>

    <!--   添加分类   -->
    <el-dialog title="添加分类" :visible.sync="addDialogFormVisible"
               @close="closeAddDialog"
    >
      <el-form :model="addForm" :rules="rules">
        <el-form-item label="分类名" :label-width="formLabelWidth" prop="name">
          <el-input v-model="addForm.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="分类介绍" :label-width="formLabelWidth" prop="description">
          <el-input v-model="addForm.description" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="排序" :label-width="formLabelWidth" prop="sort">
          <el-input v-model="addForm.sort" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="addDialogFormVisible = false">取 消</el-button>
        <el-button type="danger" @click="submitAddCategory">确 定</el-button>
      </div>
    </el-dialog>

    <!--   编辑分类   -->
    <el-dialog title="添加分类" :visible.sync="editDialogFormVisible"
               @close="closeEditDialog"
    >
      <el-form :model="editForm" :rules="rules">
        <el-form-item label="分类名" :label-width="formLabelWidth" prop="name">
          <el-input v-model="editForm.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="分类介绍" :label-width="formLabelWidth" prop="description">
          <el-input v-model="editForm.description" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="排序" :label-width="formLabelWidth" prop="sort">
          <el-input v-model="editForm.sort" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="editDialogFormVisible = false">取 消</el-button>
        <el-button type="danger" @click="submitEditCategory">确 定</el-button>
      </div>
    </el-dialog>


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
        min-width="80"
      >
      </el-table-column>
      <el-table-column
        prop="name"
        label="分类名"
        min-width="120"
      >
      </el-table-column>
      <el-table-column
        prop="description"
        label="分类介绍"
        min-width="140"
      >
      </el-table-column>
      <el-table-column
        prop="clickNum"
        label="点击数"
        width="110"
        :sortable="'custom'"
      >
      </el-table-column>
      <el-table-column
        prop="sort"
        label="排序"
        width="110"
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
  deleteBatchCategory,
  deleteCategory,
  editCategory,
  pageListCategory,
  saveCategory,
  topCategory
} from '@/api/category'
import { hideFullScreenLoading, showFullScreenLoading } from '@/utils/loading'
import { deleteTag } from '@/api/tag'

export default {
  name: 'category-manager',
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

      total: 0,
      tableData: [],

      addForm: {
        name: '',
        description: '',
        sort: 0
      },
      editForm: {
        id: -1,
        name: '',
        description: '',
        sort: 0
      },

      selectedRowIds: [],
      formLabelWidth: '90px',

      addDialogFormVisible: false,
      editDialogFormVisible: false,

      rules: {
        name: [
          { required: true, message: '请输入分类名', trigger: 'blur' }
        ],
        sort: [
          { validator: validatePass, trigger: 'change', required: true }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    handleTop(index, row) {
      this.$confirm('你确定要将该分类置顶吗?', '提示', {
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
    submitAddCategory() {
      if (this.addForm.name === '') {
        return
      }
      if (this.addForm.sort === '' || this.addForm.sort === null) {
        return
      }
      saveCategory(this.addForm).then(res => {
        this.addDialogFormVisible = false
        this.getList()
        this.$message.success('分类添加成功')
      }).catch(() => {
      })
    },
    submitEditCategory() {
      if (this.editForm.name === '') {
        return
      }
      if (this.editForm.sort === '' || this.addForm.sort === null) {
        return
      }
      console.log(this.editForm)
      editCategory(this.editForm).then(res => {
        this.$message.success('分类编辑成功')
        this.editDialogFormVisible = false
        this.getList()
      }).catch(() => {
      })
    },
    deleteCategoryBatch() {
      if (this.selectedRowIds === null || this.selectedRowIds.length <= 0) {
        this.$message.error('请选择要删除的分类')
        return
      }
      this.$confirm('此操作将永久删除该分类, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteBatchCategory(this.selectedRowIds).then(res => {
          this.$message.success('批量删除分类成功')
          this.getList()
        }).catch(res => {
        })
      }).catch(() => {
      })
    },
    searchCategory() {
      this.getList()
    },
    addCategory() {
      this.addDialogFormVisible = true
    },
    handleDelete(index, row) {
      this.$confirm('此操作将永久删除该分类, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteCategory(row.id).then(res => {
          this.$message.success('分类删除成功')
          let total = this.total
          let limit = this.listQuery.pageSize
          let extra = total % limit
          if (total > limit) {
            if (extra === 1) {
              this.getList(-1)
            } else {
              this.getList()
            }
          } else {
            this.getList()
          }
        }).catch(res => {
        })
      }).catch(res => {
      })
    },
    handleEdit(index, row) {
      this.editDialogFormVisible = true
      this.editForm.id = row.id
      this.editForm.name = row.name
      this.editForm.description = row.description
      this.editForm.sort = row.sort
    },

    getList(page = 1) {
      showFullScreenLoading()
      if (page === -1) {
        this.listQuery.pageNum -= 1
        pageListCategory(this.listQuery).then(res => {
          this.tableData = res.data.rows
          this.total = parseInt(res.data.total)
          hideFullScreenLoading()
        }).catch(() => {
          hideFullScreenLoading()
        })
        return
      }
      pageListCategory(this.listQuery).then(res => {
        this.tableData = res.data.rows
        this.total = parseInt(res.data.total)
        hideFullScreenLoading()
      }).catch(() => {
        hideFullScreenLoading()
      })
    },

    handleSizeChange(pageSize) {
      this.listQuery.pageSize = pageSize
      this.getList()
    },
    handleCurrentChange(pageNum) {
      this.listQuery.pageNum = pageNum
      this.getList()
    },

    handleSelectionChange(val) {
      this.selectedRowIds = this.$refs.multipleTable.selection.map((item) => item.id)
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
    closeAddDialog() {
      this.addForm.name = ''
      this.addForm.description = ''
      this.addForm.sort = 0
    },
    closeEditDialog() {
      this.editForm.name = ''
      this.editForm.description = ''
      this.editForm.sort = 0
    }
  }
}
</script>

<style scoped>

</style>
