<template>
  <div>
    <el-menu
        :default-active="activeIndex2"
        class="el-menu-demo"
        mode="horizontal"
        @select="handleSelect"
        background-color="#e41b14"
        text-color="#fff"
        style="line-height: 0"
        active-text-color="#ffd04b">
      <el-menu-item index="1" @click="$router.push('/')">
        <img src="../assets/logo-lzh.png" alt="" style="width: 30px">
        PERSEVERE
      </el-menu-item>
      <el-menu-item index="3" @click="$router.push('/')">
        首页
        <router-link to="/home"></router-link>
      </el-menu-item>
      <el-menu-item index="4" @click="$router.push('/category')">
        分类
      </el-menu-item>
      <el-menu-item index="5" @click="$router.push('/sort')">
        归档
      </el-menu-item>
      <el-submenu index="2">
        <template slot="title">更多</template>
        <el-menu-item index="2-1" @click="$router.push('/friendlink')">友链</el-menu-item>
        <el-menu-item index="2-2" @click="intoChatComm">聊天室</el-menu-item>
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

      <el-menu-item v-if="!isLogin" style="display: inline-block;float: right" index="7"
                    @click="$router.push('/login')">
        登陆
      </el-menu-item>
      <el-menu-item v-if="!isLogin" style="display: inline-block;float: right" index="8"
                    @click="$router.push('/regist')">
        注册
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
            <router-link to="/myBlog/myinitblog">
              <el-dropdown-item command="b">我的博客</el-dropdown-item>
            </router-link>
            <el-dropdown-item command="c">退出登陆</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </el-menu-item>

      <el-menu-item style="margin-left: 100px" index="9" @click="writeBlog">
        创作<i class="el-icon-edit" style="color: #fff"/>
      </el-menu-item>
      <el-menu-item style="" index="10" @click="$router.push('/chat')">
        问答<i class="el-icon-question" style="color: #fff"/>
      </el-menu-item>
    </el-menu>


  </div>
</template>

<script>
import {logout} from "@/api/user";
import {removeToken} from "../../utils/auth";
import {getUserById} from "@/api/user";
import {pageAllArticles} from "@/api/article";

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
    intoChatComm() {
      if (this.isLogin) {
        this.$router.push('/im');
      } else {
        this.$confirm('登录后即可进入聊天室，是否前往登录页面?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {//确定，跳转至登录页面
          this.$router.push({path: '/login?type=ch'});
        })
      }
    },
    searchArticleByKeyword() {
      this.$router.push('/search?keyword=' + this.keyword);
      var userInfo = JSON.parse(localStorage.getItem("userInfo"))
      if (userInfo) {
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
        this.$confirm('是否要退出登陆?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.userLogout()
          this.$message({
            type: 'success',
            message: '退出成功!'
          });
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消'
          });
        });

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

        if (this.$route.fullPath === '/im') {
          this.$router.push('/')
        }

        // this.init()
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

.el-menu.el-menu--horizontal {
  border-bottom: 0;
}
</style>
