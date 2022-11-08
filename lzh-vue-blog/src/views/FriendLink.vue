<template>
  <div class="frindlink" style="background-color: #00a0e9;">
    <div class="about" style="">
      <h1 class="about_tit">友情链接</h1>
      <img class="about_bgc" src="../assets/friendlink.jpg" alt="">
      <div class='ribbon'>
        <router-link to="/home"><span>Home</span></router-link>
        <router-link to="/friendlink"><span>Link</span></router-link>
      </div>

      <div class="linkcard">
        <el-card v-for="link in friendLink" class="box-card" shadow="hover" @click.native="reachLink(link.url)"
                 style="background-color: #ecf7fe;cursor: pointer">
          <div>
            <el-avatar style="margin-left: 100px" :size="150" class="link_avatar" :src="link.avatar"></el-avatar>
          </div>
          <div class="card_btm" style="width: 350px">
            <h2 style="text-align: center;font-family: 幼圆;color: #00a0e9">
              {{ link.name }}
            </h2>

          </div>
          <div class="remark" style="width: 355px;height: 100px">
            <span style="color: #1d79b3">网站简介：</span>
            <br>
            <span style="color: #3ec4b4">{{ link.remark }}</span>
          </div>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script>
import {getAllFriendLink} from "@/api/friendlink";

export default {
  name: "FriendLink",
  data() {
    return {
      friendLink: []
    };
  },
  created() {
    getAllFriendLink().then(res => {
      this.friendLink = res.data
    })
  },
  methods: {
    reachLink(url) {
      window.open(url,"_blank");
    },

  },

}
</script>

<style scoped>
.card_btm h4 {
  margin-bottom: 5px;
}

.card_btm {

  /*background-color: #00a0e9;*/
}

.linkcard {
  width: 1250px;
  margin: 10px auto;
}

.box-card {
  width: 394px;
  height: 300px;
  margin: 10px;
  float: left;
}

.box-card h1 {
  font-family: 幼圆;
  color: #b31d28;
}

* {
  margin: 0;
  padding: 0;
}

.about {
  width: 100%;
  height: 100%;
  overflow: hidden;
  background-color: #bfd5ec;
}

.about_bgc {
  width: 100%
}

.about_tit {
  font-family: 华文琥珀;
  color: #00a0e9;
  position: absolute;
  top: 280px;
  left: 0;
  right: 0;
  font-size: 50px;
  font-weight: 550;
  bottom: 0;
  width: 800px;
  margin: auto;
  text-align: center;
}

.ribbon {
  display: flex;
  justify-content: center;
  position: absolute;
  right: 300px;
  top: 50px;
  margin: auto;
}

.ribbon:after,
.ribbon:before {
  margin-top: 0.5em;
  content: "";
  display: flex;;
  border: 1.5em solid #fff;
}

.ribbon:after {
  border-right-color: transparent;
}

.ribbon:before {
  border-left-color: transparent;
}

.ribbon a:link,
.ribbon a:visited {
  color: #000;
  text-decoration: none;
  height: 3.5em;
  overflow: hidden;
}

.ribbon span {
  background: #fff;
  display: inline-block;
  line-height: 3em;
  padding: 0 1.5em;
  margin-top: 0.5em;
  position: relative;
  -webkit-transition: background-color 0.2s, margin-top 0.2s;
  /* Saf3.2+, Chrome */
  -moz-transition: background-color 0.2s, margin-top 0.2s;
  /* FF4+ */
  -ms-transition: background-color 0.2s, margin-top 0.2s;
  /* IE10 */
  -o-transition: background-color 0.2s, margin-top 0.2s;
  /* Opera 10.5+ */
  transition: background-color 0.2s, margin-top 0.2s;
}

.ribbon a:hover span {
  background: #FFD204;
  margin-top: 0;
}

.ribbon span:before {
  content: "";
  position: absolute;
  top: 3em;
  left: 0;
  border-right: 0.5em solid #9B8651;
  border-bottom: 0.5em solid #fff;
}

.ribbon span:after {
  content: "";
  position: absolute;
  top: 3em;
  right: 0;
  border-left: 0.5em solid #9B8651;
  border-bottom: 0.5em solid #fff;
}


</style>
