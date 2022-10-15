<template>
  <div class="home">
    <el-menu
        :default-active="activeIndex2"
        class="el-menu-demo"
        mode="horizontal"
        @select="handleSelect"
        background-color="#e41b14"
        text-color="#fff"
        active-text-color="#ffd04b">
      <el-menu-item index="1">
        <router-link to="/">
          <img src="../assets/logo-lzh.png" alt="" style="width: 30px">
          PERSEVERE
        </router-link>
      </el-menu-item>
      <el-menu-item index="3">
        <router-link to="/home">首页</router-link>
      </el-menu-item>
      <el-menu-item index="4">分类</el-menu-item>
      <el-menu-item index="5"><a href="#" target="_blank">关于</a></el-menu-item>
      <el-submenu index="2">
        <template slot="title">更多</template>
        <el-menu-item index="2-1">选项1</el-menu-item>
        <el-menu-item index="2-2">选项2</el-menu-item>
        <el-menu-item index="2-3">选项3</el-menu-item>
      </el-submenu>
      <el-menu-item index="6">
        <div style="display: inline-block;float: right">
          <el-input @keyup.enter.native="searchArticleByKeyword" class="inputBox" v-model="keyword"
                    placeholder="Please input your thinking">
          </el-input>
          <i @click="searchArticleByKeyword" class="el-icon-search" style="color: #fff;padding-left: 15px"></i>
        </div>
      </el-menu-item>

      <el-menu-item v-if="!isLogin" style="display: inline-block;float: right" index="7">
        <router-link to="/login">登陆</router-link>
      </el-menu-item>
      <el-menu-item v-if="!isLogin" style="display: inline-block;float: right" index="8">
        <router-link to="/regist">注册</router-link>
      </el-menu-item>

      <el-menu-item v-if="isLogin" style="display: inline-block;float: right">
        <el-dropdown @command="handleCommand">
          <span class="el-dropdown-link" style="color: #fff">
          <el-avatar :src="userInfo.avatar" style="margin-right: 5px;"></el-avatar>
          {{ userInfo.userName }}<i style="color: #fff" class="el-icon-arrow-down el-icon--right"></i>
        </span>
          <el-dropdown-menu slot="dropdown">
            <router-link to="/personalCenter">
              <el-dropdown-item command="a">个人中心</el-dropdown-item>
            </router-link>
            <el-dropdown-item command="b">我的博客</el-dropdown-item>
            <el-dropdown-item command="c">退出登陆</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </el-menu-item>

      <el-menu-item style="margin-left: 100px" index="9" @click="writeBlog">
        创作<i class="el-icon-edit" style="color: #fff"/>
      </el-menu-item>
      <el-menu-item style="" index="10">提问<i class="el-icon-question" style="color: #fff"/></el-menu-item>
    </el-menu>

    <p1 class="articleNum">共找到{{ total }}篇文章</p1>

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
            <el-tag @click="searchArticleByTag(tag.id)" :type="tagTypes[index]"
                    style="cursor:pointer; margin-left: 5px;margin-bottom: 5px"
                    v-for="(tag, index) in tags">
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
            <el-card style="margin-bottom: 10px;width: 408px;cursor:pointer;"
                     v-for="viewCountArticle in viewCountTop4Article" shadow="hover">
              <div style="width: 110px;float:left;" @click="getDetailArticle(viewCountArticle.id)">
                <img :src="viewCountArticle.thumbnail" style="width: 110px;height: 75px">
              </div>
              <h3 style="width: 200px;height: 80px;float: left;font-size: 15px;margin-left: 10px"
                  @click="getDetailArticle(viewCountArticle.id)">
                {{ viewCountArticle.title }}
              </h3>
              <span style="float: right;font-size: 10px;margin-bottom: 10px;">
                <i class="el-icon-time"></i>{{ viewCountArticle.createTime }}
              </span>
              <span style="float: left;font-size: 10px;margin-bottom: 10px;">
                <i class="el-icon-user-solid"></i>{{ viewCountArticle.viewCount }}人看过
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
              <li><i class="el-icon-tickets"></i> 文章总数：{{ blogInfo.articleCount }}篇</li>
              <li><i class="el-icon-collection-tag"></i> 标签总数：{{ blogInfo.tagCount }}个</li>
              <li><i class="el-icon-folder-opened"></i> 分类总数：{{ blogInfo.categoryCount }}个</li>
              <li><i class="el-icon-view"></i> 总访问量：{{ blogInfo.viewCount }}次</li>
              <li><i class="el-icon-chat-line-square"></i> 评论数量：{{ blogInfo.commentCount }}条</li>
              <li><i class="el-icon-date"></i> 运行天数：{{ blogInfo.runningTime }}天</li>
            </ul>
          </div>
        </el-card>
      </div>


    </div>

    <Footer></Footer>
  </div>
</template>

<script>
import {logout} from "@/api/user";
import {getToken, removeToken} from "../../utils/auth";
import {getUserById} from "@/api/user";
import {getBlogInfo, getViewCountTop4Article, pageAllArticles} from "@/api/article";
import {listAllTag} from "@/api/tag";
import {addUserLikeArticle} from "@/api/like";
import {pageArticlesByTag} from "@/api/tag";

import Footer from "@/components/Footer";

export default {
  name: "Header",
  components: {Footer},
  data() {
    return {
      keyword: '',
      articles: [],
      searchArticles: [],
      activeIndex2: '',
      userInfo: {
        avatar: '',
        userName: ''
      },
      isLogin: false,
      viewCountTop4Article: [],
      tags: [],
      tagTypes: ["success", "info", "warning", "danger", "", "success", "info", "warning", "danger", "", "success", "info", "warning", "danger", "", "success", "info", "warning", "danger", "", "success", "info", "warning", "danger", "", "success", "info", "warning", "danger", ""],
      blogInfo: {},
      tagType: '',
      total: 0,
      pageNum: 1,
      pageSize: 7,
      tagId: ''
    }
  },
  created() {
    this.keyword = ''
    this.init()
    this.showUserInfo();
    //获取浏览量top4文章
    getViewCountTop4Article().then(res => {
      console.log(res);
      this.viewCountTop4Article = res.data
    })
    listAllTag().then(res => {
      this.tags = res.data
    })
    getBlogInfo().then(res => {
      console.log(res)
      this.blogInfo = res.data
    });
  },
  mounted() {
    this.keyword = this.$route.query.keyword
    this.tagId = this.$route.query.tagId
    console.log(this.keyword)
    console.log(this.tagId)
    pageArticlesByTag(this.pageNum, this.pageSize, this.tagId).then(res => {
      this.articles = res.data.rows
      this.total = parseInt(res.data.total)
    })

    if (this.keyword !== undefined) {
      this.searchArticleByKeyword();
    }
  },
  methods: {
    searchArticleByTag(tagId) {
      this.$router.push('/search?tagId=' + tagId);
      pageArticlesByTag(this.pageNum, this.pageSize, tagId).then(res => {
        this.articles = res.data.rows;
        this.total = parseInt(res.data.total);
      })
    },
    searchArticleByKeyword() {
      this.$router.push('/search?keyword=' + this.keyword);
      var item = localStorage.getItem("userInfo");
      if (item) {
        var userInfo = JSON.parse(item);
        pageAllArticles(this.pageNum, this.pageSize, userInfo.id, this.keyword).then(res => {
          this.articles = res.data.rows;
          this.total = parseInt(res.data.total)
        });
      } else {
        pageAllArticles(this.pageNum, this.pageSize, -1, this.keyword).then(res => {
          this.articles = res.data.rows;
          this.total = parseInt(res.data.total)
        });
      }

    },
    init() {
      this.userInfo = null
      this.isLogin = false
    },
    handleCommand(command) {
      if (command === 'c') {
        this.userLogout()
      }
    },
    handleSelect() {
    },
    showUserInfo() {
      let userInfo = JSON.parse(localStorage.getItem("userInfo"));
      if (userInfo) {
        getUserById(userInfo.id).then(res => {
          this.userInfo.avatar = res.data.avatar
          this.userInfo.userName = res.data.userName
        })
      }
      // console.log(userInfo)
      if (userInfo) {
        this.isLogin = true;
        this.userInfo = userInfo;
      }
    },
    userLogout() {
      logout().then(res => {
        localStorage.removeItem("userInfo")
        removeToken()
        this.init()
        this.$message({
          message: '退出成功',
          type: 'success'
        });
        location.reload()
      })
    },
    writeBlog() {
      if (this.userInfo && localStorage.getItem("userInfo")) {
        this.$router.push("/article/write");
      } else {
        this.$message({
          message: '请先登录',
          type: 'error'
        });
        this.$router.push("/login?type=w")
      }
    },
    handleCurrentChange(curPage) {
      this.pageNum = curPage
      this.keyword = this.$route.query.keyword
      this.tagId = this.$route.query.tagId

      var item = localStorage.getItem("userInfo");

      if (this.tagId === undefined && this.keyword !== undefined) {
        if (item) {
          var userInfo = JSON.parse(item);
          pageAllArticles(this.pageNum, this.pageSize, userInfo.id, this.keyword).then(res => {
            this.articles = res.data.rows;
            this.total = parseInt(res.data.total)
          });
        } else {
          pageAllArticles(this.pageNum, this.pageSize, -1, this.keyword).then(res => {
            this.articles = res.data.rows;
            this.total = parseInt(res.data.total)
          });
        }
      } else if (this.tagId !== undefined && this.keyword === undefined) {
        pageArticlesByTag(this.pageNum, this.pageSize, this.tagId).then(res => {
          this.articles = res.data.rows
          this.total = parseInt(res.data.total)
        })
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
          pageAllArticles(this.pageNum, this.pageSize, userInfo.id, this.keyword).then(res => {
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
a {
  text-decoration: none;
}

. inputBox {
  width: 250px;
}

.el-menu-demo {

}

.home .articleNum {
  color: red;
  margin-left: 60px;
  font-size: 20px;
  font-weight: bold;
  font-family: 幼圆;
}

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
