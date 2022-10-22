<template>
  <div>

<!--    Header   -->
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
              <router-link to="/myBlog">
                <el-dropdown-item command="b">我的博客</el-dropdown-item>
              </router-link>
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


    <div class="myBlog">
      <div class="PersonTop">
        <div class="PersonTop_img">
          <img v-image-preview :src="avatar"/>
        </div>
        <div class="PersonTop_text">
          <div class="user_text">
            <div class="user_name">
              <span> {{ nickname }} </span>
            </div>
            <div class="user-v" v-if="v === 3">
              <img src="" class="user-v-img"/>
              <span class="user-v-font">优质媒体作者</span>
            </div>
            <div class="user_qianming">
              <span> {{ design }}</span>
            </div>

          </div>
          <div class="user_num">
            <div style="cursor: pointer" @click="myfan">
              <div class="num_number">{{ fanCounts }}</div>
              <span class="num_text">粉丝</span>
            </div>
            <div style="cursor: pointer" @click="myfollow">
              <div class="num_number">{{ followCounts }}</div>
              <span class="num_text">关注</span>
            </div>
            <div>
              <div class="num_number">{{ likedCounts }}</div>
              <span class="num_text">获赞</span>
            </div>
          </div>
        </div>
      </div>
      <div class="person_body">
        <div class="person_body_left">
          <el-card class="box-card" :body-style="{ padding: '0px' }">
            <div slot="header" class="clearfix">
            <span class="person_body_list" style="border-bottom: none"
            >我的博客</span
            >
            </div>

            <el-menu
                router
                active-text-color="#00c3ff"
                class="el-menu-vertical-demo"
            >
              <el-menu-item
                  index="myarticle"
                  :route="{ name: 'myarticle', params: $route.params.id }"
              >
                <i class="el-icon-edit-outline"></i>
                <span slot="title">草稿</span>
              </el-menu-item>
              <el-menu-item
                  index="myarticle"
                  :route="{ name: 'myarticle', params: $route.params.id }"
              >
                <i class="el-icon-reading"></i>
                <span slot="title">文章</span>
              </el-menu-item>
              <el-menu-item
                  index="mycollect"
                  :route="{ name: 'mycollect', params: $route.params.id }"
              >
                <i class="el-icon-document"></i>
                <span slot="title">收藏</span>
              </el-menu-item>
              <el-menu-item
                  index="mylike"
                  :route="{ name: 'mylike', params: $route.params.id }"
              >
                <i class="el-icon-thumb"></i>
                <span slot="title">喜欢</span>
              </el-menu-item>
              <el-menu-item
                  index="myfan"
                  :route="{ name: 'myfan', params: $route.params.id }"
              >
                <i class="el-icon-tableware"></i>
                <span slot="title">粉丝</span>
              </el-menu-item>
              <el-menu-item
                  index="myfollow"
                  :route="{ name: 'myfollow', params: $route.params.id }"
              >
                <i class="el-icon-circle-plus-outline"></i>
                <span slot="title">关注</span>
              </el-menu-item>
            </el-menu>
          </el-card>
        </div>
        <div class="person_body_right">
          <router-view></router-view>
        </div>
      </div>

    </div>

    <Footer></Footer>
  </div>
</template>

<script>

import Header from "@/components/Search";
import Footer from "@/components/Footer";
import {pageAllArticles} from "@/api/article";
import {getUserById, logout} from "@/api/user";
import {removeToken} from "../../utils/auth";
export default {
  components: {Footer, Header},
  name: "Personal",
  inject: ["reload"],
  data() {
    return {
      avatar: "https://img1.baidu.com/it/u=1817915659,804553856&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500",
      nickname: "LZH",
      v: 1,
      design: "NEVER FORGET WHY YOU STARTED",
      followCounts: "78",
      fanCounts: "10",
      likedCounts: "42",
      isfollow: true,
      followData: {
        fanId: "",
        followId: "",
      },
      isfollowid: [],


      keyword: '',
      activeIndex2: '',
      userInfo: {
        avatar: '',
        userName: ''
      },
      isLogin: false,
    };
  },
  created() {




    //Header
    this.keyword = ''
    this.init()
    this.showUserInfo();
  },
  mounted() {
    this.load();
  },

  methods: {
    load() {

    },
    myfan() {
    },
    myfollow() {
    },
    follow() {

    },
    edit() {
      this.$refs.dia.open();
    },










    //Header
    searchArticleByKeyword() {
      this.$router.push('/search?keyword=' + this.keyword);
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
        this.init()
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
  },
};
</script>

<style scoped>
.me-video-player {
  background-color: transparent;
  width: 100%;
  height: 100%;
  object-fit: fill;
  display: block;
  position: fixed;
  left: 0;
  z-index: 0;
  top: 0;
}

.myBlog {
  height: 1100px;
  background-color: #eeb8b8;
}

.PersonTop {
  width: 1000px;
  height: 140px;
  padding-top: 20px;
  background-color: white;
  margin-top: 30px;
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  border-radius: 5px;
}

.PersonTop_img {
  width: 150px;
  height: 120px;
  background-color: #8c939d;
  margin-right: 24px;
  margin-left: 20px;
  overflow: hidden;
  border-radius: 20px;
}

.PersonTop_img img {
  width: 100%;
  height: 100%;
  border-radius: 20px;
}

.PersonTop_text {
  height: 120px;
  width: 880px;
  display: flex;
}

.user_text {
  width: 60%;
  height: 100%;
  line-height: 30px;
}

.user_name {
  font-weight: bold;
}

.user-v {
  margin-bottom: -5px;
}

.user-v-img {
  width: 15px;
  height: 15px;
}

.user-v-font {
  font-size: 15px;
  color: #00c3ff;
}

.user_qianming {
  font-size: 14px;
  color: #999;
}

.user_num {
  width: 40%;
  height: 100%;
  display: flex;
  align-items: center;
}

.user_num > div {
  text-align: center;
  border-right: 1px dotted #999;
  box-sizing: border-box;
  width: 80px;
  height: 40px;
  line-height: 20px;
}

.num_text {
  color: #999;
}

.num_number {
  font-size: 20px;
  color: #333;
}

.el-menu-item > span {
  font-size: 16px;
  color: #999;
}

/*下面部分样式*/
.person_body {
  width: 1000px;
  margin-top: 210px;
  display: flex;
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
  border-radius: 5px;
}

.person_body_left {
  width: 27%;
  height: 600px;
  border-radius: 5px;
  margin-right: 3%;
  text-align: center;
}

.person_body_list {
  width: 100%;
  height: 50px;
  margin-top: 25px;
  font-size: 22px;
  border-bottom: 1px solid #f0f0f0;
  background-image: -webkit-linear-gradient(
      left,
      rgb(42, 134, 141),
      #e9e625dc 20%,
      #3498db 40%,
      #e74c3c 60%,
      #09ff009a 80%,
      rgba(82, 196, 204, 0.281) 100%
  );
  -webkit-text-fill-color: transparent;
  -webkit-background-clip: text;
  -webkit-background-size: 200% 100%;
  -webkit-animation: masked-animation 4s linear infinite;
}


.person_body_right {
  width: 70%;
  /* height: 500px; */
  border-radius: 5px;
  background-color: white;
}

.box-card {
  height: 500px;
}


a {
  text-decoration: none;
}

. inputBox {
  width: 250px;
}
</style>
