<template>
  <div class="mycollect" style="height: 870px;border-radius:10px">
    <el-card v-for="article in allData" class="box-card" shadow="hover"
             style="border-radius: 10px;margin-bottom: 10px;cursor:pointer;">
      <h3 @click="getDetailArticle(article.id)">{{ article.title }}</h3>
      <h4 @click="getDetailArticle(article.id)" style="font-weight: lighter">{{ article.summary }}</h4>
      <br>
      <div @click="getDetailArticle(article.id)">
      <span class="me-pull-right me-article-count">
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
          </span>&nbsp;
        <span style="" class="me-pull-right me-article-count">
        <i class="el-icon-time"></i>&nbsp;{{ article.createTime }}
      </span>
        <span style="display: block;width: 200px;float:right;margin-top: -40px">
          <el-button type="primary" @click.stop="updateArticle(article.id)"><i class="el-icon-edit-outline"></i> 修改</el-button>
          <el-button type="danger" @click.stop="deleteArticle(article.id)"><i class="el-icon-remove-outline"></i> 删除</el-button>
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
        description="暂未发布任何文章额"
    ></el-empty>

  </div>
</template>

<script>
import {pageUserPublishArticle} from "@/api/article";
import {deleteArticle} from "@/api/article";

export default {
  name: "mypublish",
  data() {
    return {
      allData: [],

      pageNum: 1,
      pageSize: 6,
      total: 0,

      uid: -1,

    };
  },
  created() {
    let userInfo = JSON.parse(localStorage.getItem("userInfo"));
    if (!userInfo) {
      this.$router.push("/login")
    }
    this.uid = userInfo.id
    pageUserPublishArticle(userInfo.id, this.pageNum, this.pageSize).then(res => {
      this.allData = res.data.rows
      this.total = parseInt(res.data.total)
    })
  },
  mounted() {
  },
  methods: {
    updateArticle(articleId) {
      let routeData = this.$router.resolve({path: '/publishview/' + articleId});
      window.open(routeData.href, '_blank');
    },
    deleteArticle(articleId) {
      this.$confirm('是否要删除该文章?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteArticle(articleId).then(res => {
          this.$message({
            type: 'success',
            message: '删除成功!'
          });
        })
        pageUserPublishArticle(this.uid, this.pageNum, this.pageSize).then(res => {
          this.allData = res.data.rows
          this.total = parseInt(res.data.total)
          if (this.total <= this.pageSize) {
            this.pageNum = 1;
            pageUserPublishArticle(this.uid, this.pageNum, this.pageSize).then(res => {
              this.allData = res.data.rows
              this.total = parseInt(res.data.total)
            })
          } else {
            if (this.total % this.pageSize === 0) {
              this.pageNum -= 1
            }
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      let userInfo = JSON.parse(localStorage.getItem("userInfo"));
      pageUserPublishArticle(userInfo.id, this.pageNum, this.pageSize).then(res => {
        this.allData = res.data.rows
        this.total = parseInt(res.data.total)
      })
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      let userInfo = JSON.parse(localStorage.getItem("userInfo"));
      pageUserPublishArticle(userInfo.id, this.pageNum, this.pageSize).then(res => {
        this.allData = res.data.rows
        this.total = parseInt(res.data.total)
      })
    },
    getDetailArticle(id) {
      let routeData = this.$router.resolve({path: '/article/detail/' + id});
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
