<template>
  <el-container style="min-height: 100vh">

    <el-aside :width="sideWidth + 'px'" style="box-shadow: 2px 0 6px rgb(0 21 41 / 35%);">
      <el-menu :default-openeds="['1', '3']" style="min-height: 100%; overflow-x: hidden"
               background-color="rgb(48, 65, 86)"
               text-color="#fff"
               active-text-color="#ffd04b"
               :collapse-transition="false"
               :collapse="isCollapse"
      >
        <div style="height: 60px; line-height: 60px; text-align: center">
          <img src="../assets/logo.png" alt="" style="width: 30px; position: relative; top: 10px; right: 5px">
          <b style="color: white" v-show="logoTextShow">博客后台管理系统</b>
        </div>
        <el-submenu index="1">
          <template slot="title">
            <i class="el-icon-message"></i>
            <span slot="title">导航一</span>
          </template>
          <el-menu-item-group title="分组2">
            <el-menu-item index="1-3">选项3</el-menu-item>
          </el-menu-item-group>
          <el-submenu index="1-4">
            <template slot="title">选项4</template>
            <el-menu-item index="1-4-1">选项4-1</el-menu-item>
          </el-submenu>
        </el-submenu>
        <el-submenu index="2">
          <template slot="title">
            <i class="el-icon-menu"></i>
            <span slot="title">导航二</span>
          </template>
          <el-submenu index="2-4">
            <template slot="title">选项4</template>
            <el-menu-item index="2-4-1">选项4-1</el-menu-item>
          </el-submenu>
        </el-submenu>
        <el-submenu index="3">
          <template slot="title">
            <i class="el-icon-setting"></i>
            <span slot="title">导航三</span>
          </template>
          <el-submenu index="3-4">
            <template slot="title">选项4</template>
            <el-menu-item index="3-4-1">选项4-1</el-menu-item>
          </el-submenu>
        </el-submenu>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header style="font-size: 12px; border-bottom: 1px solid #ccc; line-height: 60px; display: flex">
        <div style="flex: 1; font-size: 20px">
          <span :class="collapseBtnClass" style="cursor: pointer" @click="collapse"></span>
        </div>
        <el-dropdown style="width: 70px; cursor: pointer">
          <span>王小虎</span><i class="el-icon-arrow-down" style="margin-left: 5px"></i>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item style="font-size: 14px; padding: 5px 0">个人信息</el-dropdown-item>
            <el-dropdown-item style="font-size: 14px; padding: 5px 0">退出</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>

      </el-header>

      <el-main>
        <div style="margin-bottom: 30px">
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>用户管理</el-breadcrumb-item>
          </el-breadcrumb>
        </div>

        <div style="margin: 10px 0">
          <el-input v-model="articleOption.title" style="width: 200px" placeholder="请输入文章标题"
                    suffix-icon="el-icon-search"></el-input>
          <el-input v-model="articleOption.summary" style="width: 200px" placeholder="请输入文章摘要"
                    suffix-icon="el-icon-notebook-2" class="ml-5"></el-input>
          <el-select v-model="selectedCategory" slot="prepend" placeholder="--------请选择分类--------">
            <el-option @click.native="articleOption.categoryId = -1" label="--------请选择分类--------"
                       value="-1"></el-option>
            <el-option @click.native="selectCategoryId(category.id)" v-for="category in categories"
                       :label="category.name" :value="category.id"></el-option>
          </el-select>
          <el-button class="ml-5" type="primary" @click="searchArticle">搜索</el-button>
          <el-button class="ml-5" type="warning" @click="clearOption">清空</el-button>
        </div>

        <div style="margin: 10px 0">
          <el-button type="primary">新增 <i class="el-icon-circle-plus-outline"></i></el-button>
          <el-button type="danger">批量删除 <i class="el-icon-remove-outline"></i></el-button>
          <el-button type="primary">导入 <i class="el-icon-bottom"></i></el-button>
          <el-button type="primary">导出 <i class="el-icon-top"></i></el-button>
        </div>

        <el-table :data="article" border stripe :header-cell-class-name="headerBg">
          <el-table-column prop="id" label="文章id" width="140">
          </el-table-column>
          <el-table-column prop="title" label="标题" width="120">
          </el-table-column>
          <el-table-column prop="summary" label="摘要">
          </el-table-column>
          <el-table-column label="分类" width="120">
            <template slot-scope="scope">
              <el-tag size="medium">{{ scope.row.categoryName }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间">
          </el-table-column>
          <el-table-column label="操作" width="200" align="center">
            <template slot-scope="scope">
              <el-button type="success">编辑 <i class="el-icon-edit"></i></el-button>
              <el-button @click="deleteArticle(scope.row.id)" type="danger">删除 <i class="el-icon-remove-outline"></i>
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <div style="padding: 10px 0">
          <el-pagination
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              :current-page="pageNum"
              :page-sizes="[5, 10, 15, 20]"
              :page-size="pageSize"
              layout="total, sizes, prev, pager, next, jumper"
              :total="total">
          </el-pagination>
        </div>
      </el-main>

    </el-container>
  </el-container>
</template>

<script>

import {pageListArticle} from "@/api/article";
import {listAllCategory} from "@/api/category";
import {deleteArticleByArticleId} from "@/api/article";

export default {
  name: 'Article',
  data() {
    return {
      article: [],
      articleOption: {
        title: '',
        summary: '',
        categoryId: -1
      },
      selectedCategory: '',
      categoryName: '',
      categories: [],

      pageNum: 1,
      pageSize: 5,
      total: 0,

      msg: "hello 青哥哥",
      collapseBtnClass: 'el-icon-s-fold',
      isCollapse: false,
      sideWidth: 200,
      logoTextShow: true,
      headerBg: 'headerBg'
    }
  },
  created() {
    this.init()
    this.pageList(this.articleOption.title, this.articleOption.summary, this.articleOption.categoryId, this.pageNum, this.pageSize)
  },
  methods: {
    clearOption() {
      this.init()
      this.selectedCategory = "--------请选择分类--------"
    },
    selectCategoryId(categoryId) {
      this.articleOption.categoryId = categoryId
    },
    deleteArticle(id) {
      this.$confirm('此操作将永久删除该文章, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteArticleByArticleId(id).then(res => {
          this.$message({
            type: 'success',
            message: '文章删除成功!'
          });
        })
        pageListArticle(this.articleOption.title, this.articleOption.summary, this.articleOption.categoryId, this.pageNum, this.pageSize).then(res => {
          debugger
          let pageNum = this.pageNum //保存原先页数
          this.pageNum = 1
          this.article = res.data.rows
          this.total = parseInt(res.data.total)

          if (this.total - (pageNum - 1) * this.pageSize === 1) {
            if (this.total <= this.pageSize) {
              this.pageList(this.articleOption.title, this.articleOption.summary, this.articleOption.categoryId, 1, this.pageSize);
              this.pageNum = 1
            } else {
              this.pageList(this.articleOption.title, this.articleOption.summary, this.articleOption.categoryId, pageNum - 1, this.pageSize);
              this.pageNum = pageNum - 1
            }
          } else {
            this.pageList(this.articleOption.title, this.articleOption.summary, this.articleOption.categoryId, pageNum, this.pageSize);
            this.pageNum = pageNum
          }
        });
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    searchArticle() {
      this.pageNum = 1
      this.pageList(this.articleOption.title, this.articleOption.summary, this.articleOption.categoryId, this.pageNum, this.pageSize);
    },
    init() {
      //获取所有分类
      listAllCategory().then(res => {
        this.categories = res.data
      })

      this.articleOption.title = ''
      this.articleOption.summary = ''
      this.articleOption.categoryId = -1
    },
    pageList(title, summary, categoryId, pageNum, pageSize) {
      pageListArticle(title, summary, categoryId, pageNum, pageSize).then(res => {
        this.article = res.data.rows
        this.total = parseInt(res.data.total)
      })
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.pageList(this.articleOption.title, this.articleOption.summary, this.articleOption.categoryId, 1, this.pageSize)
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum;
      this.pageList(this.articleOption.title, this.articleOption.summary, this.articleOption.categoryId, this.pageNum, this.pageSize)
    },
    collapse() {  // 点击收缩按钮触发
      this.isCollapse = !this.isCollapse
      if (this.isCollapse) {  // 收缩
        this.sideWidth = 64
        this.collapseBtnClass = 'el-icon-s-unfold'
        this.logoTextShow = false
      } else {   // 展开
        this.sideWidth = 200
        this.collapseBtnClass = 'el-icon-s-fold'
        this.logoTextShow = true
      }
    }
  }
}
</script>

<style>
.headerBg {
  background: #eee !important;
}
</style>
