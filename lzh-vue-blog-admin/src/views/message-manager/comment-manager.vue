<template>
  <div class="comment">
    <div style="margin: 10px">
      <el-input v-model="listQuery.content" placeholder="请输入评论内容"
                style="width: 180px;margin-right: 5px;margin-left: 10px"
                class="filter-item"
      />
      <el-input v-model="listQuery.commenter" placeholder="请输入评论人" style="width: 180px;margin-right: 5px"
                class="filter-item"
      />
      <el-input v-model="listQuery.isCommented" placeholder="请输入被评论人" style="width: 180px;margin-right: 5px"
                class="filter-item"
      />
      <el-select v-model="selectedType" placeholder="评论来源" clearable style="width: 130px;margin-right: 5px"
                 class="filter-item"
                 @clear="clearTag"
      >
        <el-option @click.native="selectCommentType(item.typeId)" v-for="item in commentType"
                   :key="item.typeId"
                   :label="item.type"
                   :value="item.type"
        />
      </el-select>
      <el-button class="filter-item" type="danger"
                 icon="el-icon-search"
                 @click="searchComment"
                 style="margin-bottom: 5px;"
      >
        查找
      </el-button>

      <el-button class="filter-item" type="danger"
                 icon="el-icon-delete"
                 @click="deleteCommentBatch"
                 style="margin-bottom: 5px;margin-left: 10px"
      >
        批量删除
      </el-button>

      <el-tooltip class="item" effect="dark" content="刷新" placement="top" style="float: right">
        <!-- span是button的外层分类 -->
        <el-button class="filter-item"
                   type="" icon="el-icon-refresh-left"
                   @click="getList"
                   style="margin-bottom: 5px;margin-left: 10px;"
                   circle
        ></el-button>
      </el-tooltip>
    </div>

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
        label="头像"
        min-width="110"
      >
        <template v-slot="scope">
          <img :src="scope.row.avatar" alt="" style="width:100px; height: 100px; border-radius: 50px">
        </template>
      </el-table-column>
      <el-table-column
        prop="commenter"
        label="评论人"
        width="110"
      >
        <template slot-scope="scope">
          <el-tag type="success">{{ scope.row.commenter }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="isCommented"
        label="被评论人"
        width="110"
      >
        <template slot-scope="scope">
          <el-tag>{{ scope.row.isCommented }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="content"
        label="评论内容"
        min-width="110"
      >
      </el-table-column>
      <el-table-column
        prop="type"
        label="来源"
        width="110"
      >
        <template slot-scope="scope">
          <el-tag type="warning">{{ scope.row.type }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="createTime"
        label="评论时间"
        width="165"
        :sortable="'custom'"
      >
      </el-table-column>
      <el-table-column
        fixed="right"
        label="操作"
        min-width="100"
      >
        <template slot-scope="scope">
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
import { deleteComment, pageList } from '@/api/comment'
import { hideFullScreenLoading, showFullScreenLoading } from '@/utils/loading'

export default {
  name: 'comment-manager',
  data() {
    return {
      selectedRowIds: [],
      tableData: [],

      selectedCommentType: '',
      selectedType: '',
      commentType: [
        { 'typeId': '0', 'type': '文章评论' },
        { 'typeId': '1', 'type': '友链评论' },
        { 'typeId': '2', 'type': '问答评论' }
      ],

      listQuery: {
        content: '',
        commenter: '',
        isCommented: '',
        selectedType: '',
        sortArr: [],
        pageNum: 1,
        pageSize: 10
      },
      total: 0
    }
  },
  created() {
    this.getList()
  },
  methods: {
    selectCommentType(typeId) {
      this.selectedCommentType = typeId
      this.listQuery.selectedType = typeId
    },
    getList() {
      showFullScreenLoading()
      pageList(this.listQuery).then(res => {
        this.tableData = res.data.rows
        this.total = parseInt(res.data.total)
        hideFullScreenLoading()
      }).catch(() => {
        hideFullScreenLoading()
      })
    },
    searchComment() {
      this.getList()
    },
    deleteCommentBatch() {

    },
    handleSelectionChange(val) {
      this.selectedRowIds = this.$refs.multipleTable.selection.map((item) => item.id)
    },
    handleDelete(index, row) {
      this.$confirm('此操作将永久删除该评论, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteComment(row.id).then(res => {
          this.$message.success('评论删除成功')
          this.getList()
        }).catch(() => {
        })
      }).catch(() => {
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
    clearTag() {
      this.listQuery.selectedType = ''
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
      this.getList() //请求后端获取数据，this.queryParams.sortArr为放多列排序数据的参数，格式为[{prop: 'ptState',order:'descending'}]
    },
    handleHeadAddClass({ column }) {
      this.listQuery.sortArr.forEach(item => {
        if (item.prop === column.property) {
          column.order = item.order
        }
      })
    }
  }
}
</script>

<style scoped>

</style>
