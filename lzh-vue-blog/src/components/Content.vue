<template>
  <div>
    <div class="body" style="height: 1250px">
      <div class="article">
        <el-card class="box-card" v-for="article in articles" shadow="hover" style="margin-bottom: 10px">
          <div class="articleImg" style="position: relative;cursor: pointer" @click="getDetailArticle(article.id)">
            <img :src="article.thumbnail" style="width: 200px;height: 150px;position: absolute;margin-top: 15px;"/>
          </div>
          <ul class="articleInfo" style="position: relative">
            <li><h2 @click="getDetailArticle(article.id)" style="cursor: pointer">{{ article.title }}</h2></li>
            <li style="margin-top: 10px">
              <span style="font-weight: normal;color: #bab5b5">{{ article.summary }}</span>
            </li>
            <li style="position: absolute;top: 140px" class="tip">
              <span><i class="el-icon-time"></i>{{ article.createTime.substring(0, 10) }}</span>
              <span style="cursor: pointer" @click="getDetailArticle(article.id)"><i
                  class="el-icon-user"></i>浏览({{ article.viewCount }})</span>
              <span style="cursor:pointer;" @click="getDetailArticle(article.id)"><i
                  class="el-icon-chat-dot-square"></i>评论({{ article.commentCount }})</span>

              <!--              <el-tag style="cursor: pointer"   @click="addLike(article.id,article.likedStatus)" :color="article.likedStatus === 1?'red':''">点赞{{article.likedCount}}</el-tag>-->
              <span v-show="article.likedStatus === 0" style="cursor: pointer;"
                    @click="addLike(article.id,article.likedStatus)"><i
                  class="el-icon-thumb"></i>点赞({{ article.likedCount }})</span>

              <span v-show="article.likedStatus === 1" style="cursor: pointer"
                    @click="addLike(article.id,article.likedStatus)"><i class="el-icon-thumb" style="color: red"></i>点赞({{
                  article.likedCount
                }})</span>

              <span v-show="article.likedStatus === -1" style="cursor: pointer"
                    @click="addLike(article.id,article.likedStatus)"><i
                  class="el-icon-thumb"></i>点赞({{ article.likedCount }})</span>
              <span style="cursor: pointer" @click="getDetailArticle(article.id)">阅读全文>></span>
            </li>
          </ul>
        </el-card>

        <!--   分页   -->
        <div style="float: left;width: 500px">
          <el-pagination
              background
              @current-change="handleCurrentChange"
              layout="prev, pager, next"
              :current-page="pageNum"
              :page-size="pageSize"
              :total="total">
          </el-pagination>
        </div>
      </div>

      <div class="sideAll">
        <!--    关于我    -->
        <el-card class="sideIntro" shadow="hover">
          <div slot="header" class="clearfix" style="font-size: 20px">
            <router-link to="/aboutMe" style="color: black;text-decoration: none">
              <span style="cursor: pointer"><i class="el-icon-office-building"></i> | 关于我</span>
            </router-link>
          </div>
          <div class="aboutImg" style="margin: 0 auto;width: 100px">
            <img style="width: 100px" src="../assets/wxinfo.png">
          </div>
          <div class="intro">
            <div>PERSEVERE开源博客</div>
            <div>Author: LZH</div>
            <div>寄语：前方的道路注定不平凡</div>
          </div>
        </el-card>
        <!--    标签    -->
        <el-card class="sideIntro" shadow="hover" style="margin-top: 10px;height: 250px">
          <div slot="header" class="clearfix" style="font-size: 20px">
            <span style="cursor: pointer"><i class="el-icon-price-tag"></i> | 标签</span>
          </div>
          <div class="sideTag">
            <el-tag :type="tagTypes[index]" style="margin-left: 5px;margin-bottom: 5px" v-for="(tag, index) in tags">
              {{ tag.name }}
            </el-tag>
          </div>
        </el-card>
        <!--    排行榜    -->
        <el-card class="sideIntro" shadow="hover" style="margin-top: 10px;height: 650px">
          <div slot="header" class="clearfix" style="font-size: 20px">
            <span style="cursor: pointer"><i class="el-icon-s-data"></i> | 排行榜</span>
          </div>
          <div class="sideSort">
            <el-card style="margin-bottom: 10px;width: 408px;cursor:pointer;" v-for="viewCountArticle in viewCountTop4Article" shadow="hover">
              <div style="width: 110px;float:left;" @click="getDetailArticle(viewCountArticle.id)">
                <img :src="viewCountArticle.thumbnail" style="width: 110px;height: 75px">
              </div>
              <h3 style="width: 200px;height: 80px;float: left;font-size: 15px;margin-left: 10px"
                  @click="getDetailArticle(viewCountArticle.id)">
                {{viewCountArticle.title}}
              </h3>
              <span style="float: right;font-size: 10px;margin-bottom: 10px;">
                <i class="el-icon-time"></i>{{viewCountArticle.createTime}}
              </span>
              <span style="float: left;font-size: 10px;margin-bottom: 10px;">
                <i class="el-icon-user-solid"></i>{{viewCountArticle.viewCount}}人看过
              </span>
            </el-card>
          </div>
        </el-card>

        <!--   本站信息   -->
        <el-card class="sideIntro" shadow="hover" style="margin: 10px 0;height: 280px">
          <div slot="header" class="clearfix" style="font-size: 20px">
            <span style="cursor: pointer"><i class="el-icon-info"></i> | 网站信息</span>
          </div>
          <div class="info">
            <ul>
              <li><i class="el-icon-tickets"></i> 文章总数：{{blogInfo.articleCount}}篇</li>
              <li><i class="el-icon-collection-tag"></i> 标签总数：{{blogInfo.tagCount}}个</li>
              <li><i class="el-icon-folder-opened"></i> 分类总数：{{blogInfo.categoryCount}}个</li>
              <li><i class="el-icon-view"></i> 总访问量：{{blogInfo.viewCount}}次</li>
              <li><i class="el-icon-chat-line-square"></i> 评论数量：{{blogInfo.commentCount}}条</li>
              <li><i class="el-icon-date"></i> 运行天数：{{blogInfo.runningTime}}天</li>
            </ul>
          </div>
        </el-card>
      </div>


    </div>
  </div>
</template>

<script>
import {listAllArticles, pageAllArticles} from "@/api/article";
import {listAllTag} from "@/api/tag";
import {getToken} from "../../utils/auth";
import {getArticleCommentNum} from "@/api/comment";
import {addUserLikeArticle, getLikedCountByArticleId} from "@/api/like";
import {getViewCountTop4Article} from "@/api/article";
import {getBlogInfo} from "@/api/article";

export default {
  name: "Content",
  inject: ['reload'],
  data() {
    return {
      articles: [],
      viewCountTop4Article: [],
      tags: [],
      tagTypes: ["success", "info", "warning", "danger", "", "success", "info", "warning", "danger", "", "success", "info", "warning", "danger", "", "success", "info", "warning", "danger", "", "success", "info", "warning", "danger", "", "success", "info", "warning", "danger", ""],
      blogInfo: {},
      tagType: '',
      total: 0,
      pageNum: 1,
      pageSize: 7
    }
  },

  created() {
    listAllTag().then(res => {
      this.tags = res.data
    })

    getBlogInfo().then(res => {
      console.log(res)
      this.blogInfo = res.data
    });

    var item = localStorage.getItem("userInfo");
    if (item) {
      var userInfo = JSON.parse(item);
      pageAllArticles(this.pageNum, this.pageSize, userInfo.id).then(res => {
        this.articles = res.data.rows;
        this.total = parseInt(res.data.total)
      });
    } else {
      pageAllArticles(this.pageNum, this.pageSize, -1).then(res => {
        this.articles = res.data.rows;
        this.total = parseInt(res.data.total)
      });
    }
    //获取浏览量top4文章
    getViewCountTop4Article().then(res => {
      console.log(res);
      this.viewCountTop4Article = res.data
    })
  },
  methods: {
    handleCurrentChange(curPage) {
      this.pageNum = curPage
      var item = localStorage.getItem("userInfo");
      if (item) {
        var userInfo = JSON.parse(item);
        pageAllArticles(this.pageNum, this.pageSize, userInfo.id).then(res => {
          this.articles = res.data.rows;
          this.total = parseInt(res.data.total)
        });
      } else {
        pageAllArticles(this.pageNum, this.pageSize).then(res => {
          this.articles = res.data.rows;
          this.total = parseInt(res.data.total)
        });
      }
    },
    getDetailArticle(id) {
      // this.$router.push('/article/detail/' + id);
      let routeData = this.$router.resolve({path: '/article/detail/' + id, query: {id: 1}});
      window.open(routeData.href, '_blank');
    },
    addLike(articleId, likedStatus) {
      let strUserInfo = localStorage.getItem("userInfo");
      let userInfo = JSON.parse(strUserInfo);
      if (getToken() && userInfo) {
        addUserLikeArticle(userInfo.id, articleId).then(res => {
        });

        setTimeout(() => {
          pageAllArticles(this.pageNum, this.pageSize, userInfo.id).then(res => {
            this.articles = res.data.rows;
            this.total = parseInt(res.data.total)
          });
        }, 300);
        if (likedStatus === 0) {
          this.$message({
            message: '点赞成功',
            type: 'success'
          });
        } else if (likedStatus === 1) {
          this.$message({
            message: '取消点赞',
            type: 'warning'
          });
        }
      } else {
        this.$confirm('登录后即可点赞，是否前往登录页面?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {//确定，跳转至登录页面
          this.$router.push({path: '/login'});
        }).catch(() => {

        });
      }
    },

  }
}
</script>

<style scoped>
.info ul li {
  margin: 8px 0;
}
.sideSort img {
  width: 50px;
}

.sideIntro {
  float: right;
  width: 450px;
  height: 300px;
  font-family: '微软雅黑';
}

.body {
  width: 1150px;
  margin: 10px auto;
}

.article {
  width: 670px;
  float: left;
}

.sideAll {
  width: 450px;
  float: right;
}

.box-card {
  width: 670px;
  height: 200px;
}

.articleImg {
  width: 200px;
  height: 200px;
  float: left;
  line-height: 200px;
}

.articleInfo {
  width: 420px;
  height: 200px;
  margin-left: 10px;
  float: left;
  margin-top: 10px;
}

.tip span {
  font-size: 10px;
  margin-right: 15px;
}

.intro div {
  margin-top: 5px;
  font-size: 10px;
}
</style>
