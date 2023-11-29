<template>
  <div class="tag">
    <div class="condition" style="margin: 10px">
      <el-input v-model="listQuery.userBehavior" placeholder="请输入用户行为" style="max-width: 260px;margin-right: 5px"
                class="filter-item"
      />

      <el-date-picker
          v-model="dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          @change="changeDateRange()"
      >
      </el-date-picker>

      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="reset"
                 style="margin-left: 10px"
      >
        重置
      </el-button>

      <el-button class="filter-item" type="danger" icon="el-icon-search" @click="searchUserBehaviorLog"
                 style="margin-left: 10px"
      >
        查找
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
    >
      <el-table-column
          type="selection"
          width="55"
      >
      </el-table-column>
      <el-table-column
          label="序号"
          width="80"
      >
        <template slot-scope="scope">
          {{ scope.$index + 1 + (listQuery.pageNum - 1) * listQuery.pageSize }}
        </template>
      </el-table-column>
      <el-table-column
          prop="ip"
          label="IP"
          max-width="120"
      >
      </el-table-column>
      <el-table-column
          prop="ipSource"
          label="IP来源"
          min-width="120"
      >
      </el-table-column>
      <el-table-column
          prop="platform"
          label="平台"
          max-width="100"
      >
      </el-table-column>
      <el-table-column
          prop="browser"
          label="浏览器"
          max-width="120"
      >
      </el-table-column>
      <el-table-column
          prop="userBehavior"
          label="用户行为"
          min-width="130"
      >
      </el-table-column>
      <el-table-column
          prop="createTime"
          label="请求时间"
          width="165"
          :sortable="'custom'"
      >
      </el-table-column>
      <el-table-column
          prop="status"
          label="状态"
          max-width="165"
      >
        <template v-slot="scope">
          <el-tag v-show="scope.row.status != null && scope.row.status === 0" type="normal">
            正常
          </el-tag>
          <el-tag v-show="scope.row.status != null && scope.row.status === 1" type="danger">
            异常
          </el-tag>
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
import {pageList} from '@/api/log'
import {showFullScreenLoading, hideFullScreenLoading} from '@/utils/loading'
import {formatTime} from '@/utils/time.js'

export default {
  data() {
    return {
      listQuery: {
        userBehavior: '',
        beginDate: '',
        endDate: '',
        pageNum: 1,
        pageSize: 10,
        sortArr: []
      },
      dateRange: [],
      total: 0,
      tableData: [],
      selectedRowIds: [],
    }
  },
  created() {
    this.getList()
  },
  methods: {
    changeDateRange() {
      if (this.dateRange === null || this.dateRange === []) {
        this.listQuery.beginDate = '';
        this.listQuery.endDate = '';
      } else {
        this.listQuery.beginDate = formatTime(this.dateRange[0], 'yyyy-MM-dd')
        this.listQuery.endDate = formatTime(this.dateRange[1], 'yyyy-MM-dd')
      }
    },
    reset() {
      this.listQuery.userBehavior = ''
      this.listQuery.beginDate = ''
      this.listQuery.endDate = ''
      this.dateRange = []
      this.getList()
    },
    searchUserBehaviorLog() {
      console.log(this.listQuery)
      this.getList();
    },
    handleSizeChange(pageSize) {
      this.listQuery.pageSize = pageSize
      this.getList()
    },
    handleCurrentChange(pageNum) {
      this.listQuery.pageNum = pageNum
      this.getList()
    },
    getList() {
      showFullScreenLoading()
      pageList(this.listQuery).then(res => {
        let data = res.data
        this.tableData = data.rows
        this.total = parseInt(data.total)
        hideFullScreenLoading()
      }).catch(() => {
        hideFullScreenLoading()
      })
    },
    handleSelectionChange() {
      this.selectedRowIds = this.$refs.multipleTable.selection.map((item) => item.id)
    }

  }

}
</script>

<style scoped>

</style>
