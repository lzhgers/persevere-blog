<template>
  <div class="mycollect" style="height: 870px;border-radius:10px">
    <el-card v-for="article in allData" class="box-card" shadow="hover"
             style="border-radius: 10px;margin-bottom: 10px;cursor:pointer;">
      <h3 @click="getDetailArticle(article.id)">{{ article.title }}</h3>
      <h4 @click="getDetailArticle(article.id)" style="font-weight: lighter">{{ article.summary }}</h4>
      <br>
      <div @click="getDetailArticle(article.id)">
      <span  class="me-pull-right me-article-count">
            <i class="el-icon-chat-dot-round"></i>&nbsp;{{ article.commentCount }}
          </span>&nbsp;
        <span class="me-pull-right me-article-count">
             <i class="el-icon-thumb"></i>&nbsp;{{ article.likedCount }}
          </span>&nbsp;
        <span class="me-pull-right me-article-count">
            <i class="el-icon-view"></i>&nbsp;{{ article.viewCount }}
          </span>&nbsp;
        <span class="me-pull-right me-article-count">
            <i class="el-icon-star-off"></i>&nbsp;{{ article.collectCount }}
          </span>
        <span style="float: right" class="me-pull-right me-article-count">
        <i class="el-icon-time"></i>&nbsp;{{ article.createTime }}
      </span>
      </div>
    </el-card>

    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page.sync="pageNum"
        :page-size="pageSize"
        layout="total, prev, pager, next, jumper"
        :total="total">
    </el-pagination>

    <el-empty
        v-if="allData.length == 0"
        :image-size="250"
        description="暂未保存任何草稿额"
    ></el-empty>

  </div>
</template>

<script>
import {pageUserRoughArticle} from "@/api/article";

export default {
  name: "myrough",
  data() {
    return {
      allData: [],

      pageNum: 1,
      pageSize: 6,
      total: 0
    };
  },
  created() {
    let userInfo = JSON.parse(localStorage.getItem("userInfo"));
    if (!userInfo) {
      this.$router.push("/login")
    }
    pageUserRoughArticle(userInfo.id, this.pageNum, this.pageSize).then(res => {
      this.allData = res.data.rows
      this.total = parseInt(res.data.total)
    })
  },
  mounted() {
  },
  methods: {
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      let userInfo = JSON.parse(localStorage.getItem("userInfo"));
      pageUserRoughArticle(userInfo.id, this.pageNum, this.pageSize).then(res => {
        this.allData = res.data.rows
        this.total = parseInt(res.data.total)
      })
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      let userInfo = JSON.parse(localStorage.getItem("userInfo"));
      pageUserRoughArticle(userInfo.id, this.pageNum, this.pageSize).then(res => {
        this.allData = res.data.rows
        this.total = parseInt(res.data.total)
      })
    },
    getDetailArticle(id) {
      let routeData = this.$router.resolve({path: '/article/detail/' + id, query: {id: 1}});
      window.open(routeData.href, '_blank');
    },
  },
}
</script>

<style scoped>
span {
  font-weight: lighter;
  font-size: 10px;
}
</style>
