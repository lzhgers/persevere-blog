<template>
  <div class="chat_communication">
    <Header></Header>
    <div style="padding: 10px; margin: 10px auto;width: 1250px">
      <el-row>
        <el-col :span="4">
          <el-card style="width: 300px; height: 300px; color: #333">
            <div style="padding-bottom: 10px; border-bottom: 1px solid #ccc">在线用户<span style="font-size: 12px">（点击聊天气泡开始聊天）</span>
            </div>
            <div style="padding: 10px 0" v-for="user in users" :key="user.username">
              <span>{{ user.username }}</span>
              <i class="el-icon-chat-dot-round" style="margin-left: 10px; font-size: 16px; cursor: pointer"
                 @click="selectUserToChat(user.username)"></i>
              <span style="font-size: 12px;color: limegreen; margin-left: 5px" v-if="user.username === chatUser">chatting...</span>
              <span v-show="" style="color: red">·</span>
            </div>
          </el-card>
        </el-col>

        <el-col :span="20">
          <div style="width: 830px; margin: 0 auto; background-color: white;
                    border-radius: 5px; box-shadow: 0 0 10px #ccc">
            <div style="text-align: center; line-height: 50px;">
              聊天室（{{ chatUser }}）
            </div>
            <div ref="box" style="height: 350px; overflow:auto; border-top: 1px solid #ccc" v-html="content"></div>
            <div style="height: 200px;margin-bottom: 50px">
              <textarea v-model="text" style="height: 160px; width: 790px; padding: 20px; border: none; border-top: 1px solid #ccc;
               border-bottom: 1px solid #ccc; outline: none"></textarea>
              <div style="text-align: right; padding-right: 10px;">
                <el-button type="primary" size="mini" @click="send">发送</el-button>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <Footer></Footer>
  </div>
</template>

<script>

import {listChatCommunicationByUsername} from "@/api/chatcommunication";
import {getUserByUserName} from "@/api/user";

import Header from "@/components/Header";
import Footer from "@/components/Footer";

let socket;

export default {
  name: "Im",
  components: {
    Footer,
    Header
  },
  data() {
    return {
      circleUrl: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png',
      user: {},
      isCollapse: false,
      users: [],
      chatUser: '',
      text: "",
      messages: [],
      content: '',
      avatar: '',
      toAvatar: '',
      fromUserName: ''
    }
  },
  created() {
    let userInfo = JSON.parse(localStorage.getItem("userInfo"));
    if (!userInfo) {
      this.$message.warning('请先登陆')
      this.$router.push('/')
    }
    this.avatar = userInfo.avatar
    this.fromUserName = userInfo.userName
    this.init()
  },
  methods: {
    selectUserToChat(username) {
      getUserByUserName(username).then(res => {
        this.toAvatar = res.data.avatar
      })
      this.content = ''
      this.chatUser = username
      listChatCommunicationByUsername(this.fromUserName, username).then(res => {
        let com = res.data;
        console.log(com)
        for (let i = 0; i < com.length; i++) {
          let html;
          if (com[i].toName === username) {
            html = "<div class=\"el-row\" style=\"padding: 5px 0\">\n" +
                "  <div class=\"el-col el-col-22\" style=\"text-align: right; padding-right: 10px\">\n" +
                "    <div class=\"tip left\">" + com[i].content + "</div>\n" +
                "  </div>\n" +
                "  <div class=\"el-col el-col-2\">\n" +
                "  <span class=\"el-avatar el-avatar--circle\" style=\"height: 40px; width: 40px; line-height: 40px;\">\n" +
                "    <img src=\"" + com[i].fromAvatar + "\" style=\"object-fit: cover;\">\n" +
                "  </span>\n" +
                "  </div>\n" +
                "</div>";
          } else {
            html = "<div class=\"el-row\" style=\"padding: 5px 0\">\n" +
                "  <div class=\"el-col el-col-2\" style=\"text-align: right\">\n" +
                "  <span class=\"el-avatar el-avatar--circle\" style=\"height: 40px; width: 40px; line-height: 40px;\">\n" +
                "    <img src=\"" + com[i].fromAvatar + "\" style=\"object-fit: cover;\">\n" +
                "  </span>\n" +
                "  </div>\n" +
                "  <div class=\"el-col el-col-22\" style=\"text-align: left; padding-left: 10px\">\n" +
                "    <div class=\"tip right\">" + com[i].content + "</div>\n" +
                "  </div>\n" +
                "</div>";
          }
          console.log(html)
          console.log('---------')
          this.content += html
        }
        var div = this.$refs.box;
        //此时必须异步执行滚动条滑动至底部
        setTimeout(() => {
          div.scrollTop = div.scrollHeight;
        }, 10)

      })
    },
    send() {
      if (!this.chatUser) {
        this.$message({type: 'warning', message: "请选择聊天对象"})
        return;
      }
      if (!this.text) {
        this.$message({type: 'warning', message: "请输入内容"})
      } else {
        if (typeof (WebSocket) == "undefined") {
          console.log("您的浏览器不支持WebSocket");
        } else {
          debugger
          console.log("您的浏览器支持WebSocket");
          // 组装待发送的消息 json
          // {"from": "zhang", "to": "admin", "text": "聊天文本"}
          let message = {from: this.user.userName, to: this.chatUser, text: this.text}
          socket.send(JSON.stringify(message));  // 将组装好的json发送给服务端，由服务端进行转发
          this.messages.push({user: this.user.userName, text: this.text})
          // 构建消息内容，本人消息
          this.createContent(null, this.user.userName, this.text)
          this.text = '';
        }

        //使滚动条滑倒最底端
        var div = this.$refs.box;
        //此时必须异步执行滚动条滑动至底部
        setTimeout(() => {
          div.scrollTop = div.scrollHeight;
        }, 0)
      }
    },
    createContent(remoteUser, nowUser, text) {  // 这个方法是用来将 json的聊天消息数据转换成 html的。

      let html
      // 当前用户消息
      if (nowUser) { // nowUser 表示是否显示当前用户发送的聊天消息，绿色气泡
        html = "<div class=\"el-row\" style=\"padding: 5px 0\">\n" +
            "  <div class=\"el-col el-col-22\" style=\"text-align: right; padding-right: 10px\">\n" +
            "    <div class=\"tip left\">" + text + "</div>\n" +
            "  </div>\n" +
            "  <div class=\"el-col el-col-2\">\n" +
            "  <span class=\"el-avatar el-avatar--circle\" style=\"height: 40px; width: 40px; line-height: 40px;\">\n" +
            "    <img src=\"" + this.avatar + "\" style=\"object-fit: cover;\">\n" +
            "  </span>\n" +
            "  </div>\n" +
            "</div>";
      } else if (remoteUser) {   // remoteUser表示远程用户聊天消息，蓝色的气泡
        html = "<div class=\"el-row\" style=\"padding: 5px 0\">\n" +
            "  <div class=\"el-col el-col-2\" style=\"text-align: right\">\n" +
            "  <span class=\"el-avatar el-avatar--circle\" style=\"height: 40px; width: 40px; line-height: 40px;\">\n" +
            "    <img src=\"" + this.toAvatar + "\" style=\"object-fit: cover;\">\n" +
            "  </span>\n" +
            "  </div>\n" +
            "  <div class=\"el-col el-col-22\" style=\"text-align: left; padding-left: 10px\">\n" +
            "    <div class=\"tip right\">" + text + "</div>\n" +
            "  </div>\n" +
            "</div>";
      }
      console.log(html)
      this.content += html;

      var div = this.$refs.box;
      //此时必须异步执行滚动条滑动至底部
      setTimeout(() => {
        div.scrollTop = div.scrollHeight;
      }, 10)
    },
    init() {
      this.user = localStorage.getItem("userInfo") ? JSON.parse(localStorage.getItem("userInfo")) : {}
      let username = this.user.userName;
      let _this = this;
      if (typeof (WebSocket) == "undefined") {
        console.log("您的浏览器不支持WebSocket");
      } else {
        console.log("您的浏览器支持WebSocket");
        let socketUrl = "ws://localhost:9999/api/imserver/" + username;
        if (socket != null) {
          socket.close();
          socket = null;
        }
        // 开启一个websocket服务
        socket = new WebSocket(socketUrl);
        //打开事件
        socket.onopen = function () {
          console.log("websocket已打开");
        };
        //  浏览器端收消息，获得从服务端发送过来的文本消息
        socket.onmessage = function (msg) {
          console.log("收到数据====" + msg.data)
          let data = JSON.parse(msg.data)  // 对收到的json数据进行解析， 类似这样的： {"users": [{"username": "zhang"},{ "username": "admin"}]}
          if (data.users) {  // 获取在线人员信息
            _this.users = data.users.filter(user => user.username !== username)  // 获取当前连接的所有用户信息，并且排除自身，自己不会出现在自己的聊天列表里
          } else {
            // 如果服务器端发送过来的json数据 不包含 users 这个key，那么发送过来的就是聊天文本json数据
            //  // {"from": "zhang", "text": "hello"}
            if (data.from === _this.chatUser) {
              _this.messages.push(data)
              // 构建消息内容
              _this.createContent(data.from, null, data.text)
            }
          }

        };
        //关闭事件
        socket.onclose = function () {
          console.log("websocket已关闭");
        };
        //发生了错误事件
        socket.onerror = function () {
          console.log("websocket发生了错误");
        }
      }
    }

  }
}

</script>

<style>
.tip {
  color: white;
  text-align: center;
  border-radius: 10px;
  font-family: sans-serif;
  padding: 10px;
  width: auto;
  display: inline-block !important;
  display: inline;
}

.right {
  background-color: #b4b0b0;
}

.left {
  background-color: #de4131;
}

/* 整个滚动条 */
::-webkit-scrollbar {
  width: 10px;
  height: 10px;
}

.element {
  scrollbar-width: none
}

/* 滚动条上的滚动滑块 */
::-webkit-scrollbar-thumb {
  background-color: #fff;
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
  background-color: #fff;
  border-radius: 32px;
}
</style>
