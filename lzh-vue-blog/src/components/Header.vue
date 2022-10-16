<template>
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
      <el-menu-item index="5"><a href="#" target="_blank">关于</a></el-menu-item>
      <el-submenu index="2">
        <template slot="title">更多</template>
        <el-menu-item index="2-1">选项1</el-menu-item>
        <el-menu-item index="2-2">选项2</el-menu-item>
        <el-menu-item index="2-3">选项3</el-menu-item>
      </el-submenu>
      <el-menu-item index="6">
        <div style="display: inline-block;float: right">
          <el-input @keyup.enter.native="searchArticleByKeyword" class="inputBox" v-model="keyword" placeholder="Please input your thinking">
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


  </div>
</template>

<script>
import {logout} from "@/api/user";
import {removeToken} from "../../utils/auth";
import {getUserById} from "@/api/user";

export default {
  name: "Header",
  data() {
    return {
      keyword: '',
      // articles: [],
      // searchArticles: [],
      activeIndex2: '',
      userInfo: {
        avatar: '',
        userName: ''
      },
      isLogin: false,
    }
  },
  created() {
    this.keyword = ''
    this.init()
    this.showUserInfo();
  },
  methods: {
    searchArticleByKeyword() {
      this.$router.push("/search?keyword=" + this.keyword);
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
a {
  text-decoration: none;
}

. inputBox {
  width: 250px;
}


</style>
