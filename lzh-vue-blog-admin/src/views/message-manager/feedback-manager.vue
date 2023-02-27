<template>
  <div class="feedback">
    <div style="margin: 10px">
      <el-input v-model="listQuery.name" placeholder="请输入友链名" style="width: 180px;margin-right: 5px;margin-left: 10px"
                class="filter-item"
      />
      <el-button class="filter-item" type="danger" icon="el-icon-search" @click="searchFriendLink"
                 style="margin-bottom: 5px;"
      >
        查找
      </el-button>


      <el-button class="filter-item" type="danger" icon="el-icon-circle-plus-outline" @click="addFriendLink"
                 style="margin-bottom: 5px;margin-left: 10px"
      >
        添加友链
      </el-button>

      <el-button class="filter-item" type="danger" icon="el-icon-delete" @click="deleteFriendLinkBatch"
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

    <!--   添加友链   -->
    <el-dialog title="添加友链" :visible.sync="addDialogFormVisible"
               @close="closeAddDialog"
    >
      <el-form :model="addForm" :rules="rules">
        <el-form-item label="友链名" :label-width="formLabelWidth" prop="name">
          <el-input v-model="addForm.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="友链介绍" :label-width="formLabelWidth" prop="description">
          <el-input v-model="addForm.description" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="排序" :label-width="formLabelWidth" prop="sort">
          <el-input v-model="addForm.sort" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="addDialogFormVisible = false">取 消</el-button>
        <el-button type="danger" @click="submitAddFriendLink">确 定</el-button>
      </div>
    </el-dialog>

    <!--   编辑友链   -->
    <el-dialog title="添加友链" :visible.sync="editDialogFormVisible"
               @close="closeEditDialog"
    >
      <el-form :model="editForm" :rules="rules">
        <el-form-item label="友链名" :label-width="formLabelWidth" prop="name">
          <el-input v-model="editForm.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="友链介绍" :label-width="formLabelWidth" prop="description">
          <el-input v-model="editForm.description" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="排序" :label-width="formLabelWidth" prop="sort">
          <el-input v-model="editForm.sort" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="editDialogFormVisible = false">取 消</el-button>
        <el-button type="danger" @click="submitEditFriendLink">确 定</el-button>
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
        prop="avatar"
        label="网站图标"
        min-width="120"
      >
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
        width="110"
      >
      </el-table-column>
      <el-table-column
        prop="listorder"
        label="排序"
        width="110"
      >
      </el-table-column>
      <el-table-column
        prop="createTime"
        label="创建时间"
        width="165"
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
export default {
  name: 'feedback-manager',
  data() {
    return {

      listQuery: {
        pageNum: 1,
        pageSize: 10,
      },
      total: 0
    }
  },
  created() {

  },
  methods: {
    searchFriendLink() {

    },
    handleTop() {

    },
    handleDelete() {

    },
    handleEdit() {

    },
    handleSizeChange(pageSize) {
      this.listQuery.pageSize = pageSize
      this.getList()
    },
    handleCurrentChange(pageNum) {
      this.listQuery.pageNum = pageNum
      this.getList()
    }

  }
}
</script>

<style scoped>

</style>
