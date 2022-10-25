<template>
  <div class="mycollect" style="height: 870px;border-radius:10px">
    <el-card v-for="article in allData" class="box-card" shadow="hover"
             style="border-radius: 10px;margin-bottom: 10px;cursor:pointer;">
      <h3 @click="getDetailArticle(article.id)">{{ article.title }}</h3>
      <h4 @click="getDetailArticle(article.id)" style="font-weight: lighter">{{ article.summary }}</h4>
      <br>
      <div @click="getDetailArticle(article.id)">
        <span style="float: left" class="me-pull-right me-article-count">
        <i class="el-icon-time"></i>&nbsp;{{ article.createTime }}
      </span>

        <el-button type="danger" style="float: right;margin-top: -50px"
                   @click.stop="deleteDraft(article.id)">
          <i class="el-icon-document-delete"></i> 删除
        </el-button>
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
import {deleteArticle} from "@/api/article";

export default {
  name: "myrough",
  data() {
    return {
      allData: [],

      pageNum: 1,
      pageSize: 7,
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
    pageUserRoughArticle(userInfo.id, this.pageNum, this.pageSize).then(res => {
      this.allData = res.data.rows
      this.total = parseInt(res.data.total)
    })
  },
  mounted() {
  },
  methods: {
    deleteDraft(articleId) {

      this.$confirm('是否要删除该草稿?', '提示', {
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
        pageUserRoughArticle(this.uid, this.pageNum, this.pageSize).then(res => {
          this.allData = res.data.rows
          this.total = parseInt(res.data.total)
          console.log(this.total)
          if (this.total <= this.pageSize) {
            this.pageNum = 1;
            pageUserRoughArticle(this.uid, this.pageNum, this.pageSize).then(res => {
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
      pageUserRoughArticle(this.uid, this.pageNum, this.pageSize).then(res => {
        this.allData = res.data.rows
        this.total = parseInt(res.data.total)
      })
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      pageUserRoughArticle(this.uid, this.pageNum, this.pageSize).then(res => {
        this.allData = res.data.rows
        this.total = parseInt(res.data.total)
      })
    },
    getDetailArticle(id) {
      let routeData = this.$router.resolve({path: '/draft/' + id});
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
