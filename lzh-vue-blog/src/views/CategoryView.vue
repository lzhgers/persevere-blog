<template>
  <div>

    <!--  Header  -->
    <div>
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
        <el-menu-item index="4">
          <router-link to="/category">分类</router-link>
        </el-menu-item>
        <el-menu-item index="5">
          <router-link to="/sort">归档</router-link>
        </el-menu-item>
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
        <el-menu-item style="" index="10">
          <router-link to="/chat">问答<i class="el-icon-question" style="color: #fff"/></router-link>
        </el-menu-item>
      </el-menu>
    </div>

    <div class="sort">
      <div class="sortOpt">
        <el-timeline>
          <el-timeline-item
              v-for="(category, index) in categories"
              :key="index">
          <span :style="selectedCategoryId === category.id ? selectedStyle : ''"
                @click="listArticleByCategoryId(category.id)" class="categoryName">{{ category.name }}</span>
          </el-timeline-item>
        </el-timeline>
      </div>
      <el-timeline class="timeLine">
        <el-timeline-item :timestamp="article.createTime" placement="top" v-for="article in articles">
          <el-card>
            <h4 class="title" @click="getDetailArticle(article.id)" style="cursor:pointer; margin-bottom: 20px">
              {{ article.title }}</h4>
            <el-tag @click="selectArticleByTagId(tag.id)" :type="tagTypes[index]"
                    style="margin-right: 10px;cursor:pointer;" v-for="(tag, index) in article.tags">
              {{ tag.name }}
            </el-tag>
          </el-card>
        </el-timeline-item>

      </el-timeline>
    </div>

    <Footer></Footer>
  </div>
</template>

<script>
import {listAllCategory} from "@/api/category";
import {getArticleByCategoryId} from "@/api/category";

/*Header*/
import {logout} from "@/api/user";
import {removeToken} from "../../utils/auth";
import {getUserById} from "@/api/user";


import Footer from "@/components/Footer";
import Header from "@/components/Search";

export default {
  name: "CategoryView",
  components: {
    Header,
    Footer,
  },
  data() {
    return {
      categories: [],
      articles: [],
      tagTypes: ["danger", "success", "warning", "", "danger", "info", "warning", "danger", "", "success", "info", "warning", "danger", "", "success", "info", "warning", "danger", "", "success", "info", "warning", "danger", "", "success", "info", "warning", "danger", ""],
      firstFlag: true,
      selectedStyle: '',
      selectedCategoryId: -1,

      /*Header*/
      keyword: '',
      activeIndex2: '',
      userInfo: {
        avatar: '',
        userName: ''
      },
      isLogin: false,
    }
  },
  created() {
    this.init()
    listAllCategory().then(res => {
      if (this.firstFlag) {
        this.selectedCategoryId = res.data[0].id
        this.listArticleByCategoryId(res.data[0].id)
        this.firstFlag = false
      }
      this.categories = res.data;
    })

    /*Header*/
    this.keyword = ''
    this.init()
    this.showUserInfo();
  },
  methods: {
    init() {
      this.firstFlag = true

      /*Header*/
      this.userInfo = null
      this.isLogin = false
    },
    listArticleByCategoryId(categoryId) {
      this.selectedStyle = 'color: red'
      this.selectedCategoryId = categoryId
      getArticleByCategoryId(categoryId).then(res => {
        console.log(res.data)
        this.articles = res.data
      })
    },
    getDetailArticle(id) {
      // this.$router.push('/article/detail/' + id);
      let routeData = this.$router.resolve({path: '/article/detail/' + id, query: {id: 1}});
      window.open(routeData.href, '_blank');
    },
    selectArticleByTagId(tagId) {
      this.$router.push('/search?tagId=' + tagId)
    },


    /*Header*/
    searchArticleByKeyword() {
      this.$router.push("/search?keyword=" + this.keyword);
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
    }
  }
}
</script>

<style scoped>

.title:hover {
  color: red;
}

.sort {
  width: 1200px;
  margin: 10px auto;
}

.sort .categoryName:hover {
  color: #f80231;
  cursor: pointer;
}

.sort .categoryName {
  color: #a64343;
  cursor: pointer;
}

.sort .sortOpt {
  width: 200px;
  height: 600px;
  float: left;
  margin-left: 60px;
  margin-right: 20px;
  overflow-y: scroll;
}

.sort .timeLine {
  width: 870px;
  height: 600px;
  float: left;
  overflow-y: scroll;
}


/* 整个滚动条 */
::-webkit-scrollbar {
  width: 10px;
  height: 10px;
}

/* 滚动条上的滚动滑块 */
::-webkit-scrollbar-thumb {
  background-color: #de4131;
  /* 关键代码 */
  background-image: -webkit-linear-gradient(45deg,
  rgba(255, 255, 255, 0.4) 25%,
  transparent 25%,
  transparent 50%,
  rgba(255, 255, 255, 0.4) 50%,
  rgba(255, 255, 255, 0.4) 75%,
  transparent 75%,
  transparent);
  border-radius: 32px;
}

/* 滚动条轨道 */
::-webkit-scrollbar-track {
  background-color: #f6d9d9;
  border-radius: 32px;
}

/*Header*/
a {
  text-decoration: none;
}

. inputBox {
  width: 250px;
}
</style>
