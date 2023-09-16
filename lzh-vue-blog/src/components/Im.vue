<template>
  <div class="chat_communication">
    <Header></Header>
    <div style="padding: 10px; margin: 20px auto;width: 1250px">
      <el-row>
        <el-col :span="4">
          <el-card style="width: 300px; height: 300px; color: #333">
            <div style="padding-bottom: 10px; border-bottom: 1px solid #ccc">在线用户<span style="font-size: 12px">（点击聊天气泡开始聊天）</span>
            </div>
            <div style="padding: 10px 0" v-for="user in users" :key="user.username">
              <span>{{ user.username }}</span>
              <el-badge is-dot :hidden="parseInt(user.isRead) === 0" class="item">
                <i class="el-icon-chat-dot-round" style="margin-left: 10px; font-size: 16px; cursor: pointer"
                   @click="selectUserToChat(user.username)"></i>
              </el-badge>
              <span style="font-size: 12px;color: limegreen; margin-left: 5px" v-if="user.username === chatUser">chatting...</span>
            </div>
          </el-card>
        </el-col>

        <el-col :span="20">
          <div style="width: 830px; margin: 0 auto; background-color: #fff;
                    border-radius: 5px; box-shadow: 0 0 10px #ccc">
            <div style="text-align: center; line-height: 50px;">
              聊天室（{{ chatUser }}）
            </div>
            <div ref="box" style="height: 350px; overflow:auto; border-top: 1px solid #ccc" v-html="analyzeEmoji(content)"></div>
            <div style="height: 200px;margin-bottom: 50px">


              <div class="tmsg-respond" ref="respondBox">
                <form class="">
                  <el-input
                      @keyup.enter.native="send"
                      type="textarea"
                      :rows="2"
                      placeholder="说点什么呢``"
                      v-model="text">
                  </el-input>
                  <div :class="pBody?'OwO':'OwO OwO-open'">
                    <div class="OwO-logo" @click="pBody=!pBody">
                      <span>OwO表情</span>
                    </div>
                    <div class="OwO-body">
                      <ul class="OwO-items OwO-items-show">
                        <li class="OwO-item" v-for="(oitem,index) in OwOlist" :key="'oitem'+index"
                            @click="choseEmoji(oitem.title)">
                          <img :src="require('@/assets/img/emot/image/'+oitem.url)" alt="">
                        </li>
                      </ul>
                      <div class="OwO-bar">
                        <ul class="OwO-packages">
                          <li class="OwO-package-active">Emoji</li>
                        </ul>
                      </div>
                    </div>
                  </div>

                </form>
              </div>


<!--                    <textarea @keyup.enter="send" v-model="text" style="height: 160px; width: 790px; padding: 20px; border: none; border-top: 1px solid #ccc;-->
<!--                     border-bottom: 1px solid #ccc; outline: none"></textarea>-->


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
import {updateIsRead} from "@/api/chatcommunication";

import Header from "@/components/Header";
import Footer from "@/components/Footer";

import {getMsgByFromAndToName} from "@/api/chatcommunication";

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
      fromUserName: '',


      respondBox: '',//评论表单
      listDom: '',//评论列表
      tmsgBox: '',//总评论盒子
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
      },
      isRespond: false,
      pBody: true,//表情打开控制
      hasMore: false,
      haslogin: true,
      userId: '',//用户id
      OwOlist: [//表情包和表情路径
        {'title': '微笑', 'url': 'weixiao.gif'},
        {'title': '嘻嘻', 'url': 'xixi.gif'},
        {'title': '哈哈', 'url': 'haha.gif'},
        {'title': '可爱', 'url': 'keai.gif'},
        {'title': '可怜', 'url': 'kelian.gif'},
        {'title': '挖鼻', 'url': 'wabi.gif'},
        {'title': '吃惊', 'url': 'chijing.gif'},
        {'title': '害羞', 'url': 'haixiu.gif'},
        {'title': '挤眼', 'url': 'jiyan.gif'},
        {'title': '闭嘴', 'url': 'bizui.gif'},
        {'title': '鄙视', 'url': 'bishi.gif'},
        {'title': '爱你', 'url': 'aini.gif'},
        {'title': '泪', 'url': 'lei.gif'},
        {'title': '偷笑', 'url': 'touxiao.gif'},
        {'title': '亲亲', 'url': 'qinqin.gif'},
        {'title': '生病', 'url': 'shengbing.gif'},
        {'title': '太开心', 'url': 'taikaixin.gif'},
        {'title': '白眼', 'url': 'baiyan.gif'},
        {'title': '右哼哼', 'url': 'youhengheng.gif'},
        {'title': '左哼哼', 'url': 'zuohengheng.gif'},
        {'title': '嘘', 'url': 'xu.gif'},
        {'title': '衰', 'url': 'shuai.gif'},
        {'title': '吐', 'url': 'tu.gif'},
        {'title': '哈欠', 'url': 'haqian.gif'},
        {'title': '抱抱', 'url': 'baobao.gif'},
        {'title': '怒', 'url': 'nu.gif'},
        {'title': '疑问', 'url': 'yiwen.gif'},
        {'title': '馋嘴', 'url': 'chanzui.gif'},
        {'title': '拜拜', 'url': 'baibai.gif'},
        {'title': '思考', 'url': 'sikao.gif'},
        {'title': '汗', 'url': 'han.gif'},
        {'title': '困', 'url': 'kun.gif'},
        {'title': '睡', 'url': 'shui.gif'},
        {'title': '钱', 'url': 'qian.gif'},
        {'title': '失望', 'url': 'shiwang.gif'},
        {'title': '酷', 'url': 'ku.gif'},
        {'title': '色', 'url': 'se.gif'},
        {'title': '哼', 'url': 'heng.gif'},
        {'title': '鼓掌', 'url': 'guzhang.gif'},
        {'title': '晕', 'url': 'yun.gif'},
        {'title': '悲伤', 'url': 'beishang.gif'},
        {'title': '抓狂', 'url': 'zhuakuang.gif'},
        {'title': '黑线', 'url': 'heixian.gif'},
        {'title': '阴险', 'url': 'yinxian.gif'},
        {'title': '怒骂', 'url': 'numa.gif'},
        {'title': '互粉', 'url': 'hufen.gif'},
        {'title': '书呆子', 'url': 'shudaizi.gif'},
        {'title': '愤怒', 'url': 'fennu.gif'},
        {'title': '感冒', 'url': 'ganmao.gif'},
        {'title': '心', 'url': 'xin.gif'},
        {'title': '伤心', 'url': 'shangxin.gif'},
        {'title': '猪', 'url': 'zhu.gif'},
        {'title': '熊猫', 'url': 'xiongmao.gif'},
        {'title': '兔子', 'url': 'tuzi.gif'},
        {'title': '喔克', 'url': 'ok.gif'},
        {'title': '耶', 'url': 'ye.gif'},
        {'title': '棒棒', 'url': 'good.gif'},
        {'title': '不', 'url': 'no.gif'},
        {'title': '赞', 'url': 'zan.gif'},
        {'title': '来', 'url': 'lai.gif'},
        {'title': '弱', 'url': 'ruo.gif'},
        {'title': '草泥马', 'url': 'caonima.gif'},
        {'title': '神马', 'url': 'shenma.gif'},
        {'title': '囧', 'url': 'jiong.gif'},
        {'title': '浮云', 'url': 'fuyun.gif'},
        {'title': '给力', 'url': 'geili.gif'},
        {'title': '围观', 'url': 'weiguan.gif'},
        {'title': '威武', 'url': 'weiwu.gif'},
        {'title': '话筒', 'url': 'huatong.gif'},
        {'title': '蜡烛', 'url': 'lazhu.gif'},
        {'title': '蛋糕', 'url': 'dangao.gif'},
        {'title': '发红包', 'url': 'fahongbao.gif'}
      ],
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

    setData(initData, result) {
      let msg = result.data.rows;
      if (initData) {
        //刷新列表
        this.commentList = msg;
      } else {
        //加载更多
        this.commentList = this.commentList.concat(msg);
      }

      this.hasMore = result.data.total > this.commentList.length
    },
    //选择表情包
    choseEmoji: function (inner) {
      this.text += '[' + inner + ']';
    },
    analyzeEmoji: function (cont) {//编译表情替换成图片展示出来
      var pattern1 = /\[[\u4e00-\u9fa5]+\]/g;
      var pattern2 = /\[[\u4e00-\u9fa5]+\]/;
      var content = cont.match(pattern1);
      var str = cont;
      if (content) {
        for (var i = 0; i < content.length; i++) {
          for (var j = 0; j < this.OwOlist.length; j++) {
            if ("[" + this.OwOlist[j].title + "]" === content[i]) {
              var src = this.OwOlist[j].url;
              break;
            }
          }
          var s = require("@/assets/img/emot/image/" + src);
          var imoj = "<img src='" + s + "'/>";

          str = str.replace(pattern2, imoj);
        }
      }
      return str;
    },


    selectUserToChat(username) {
      updateIsRead(username, this.fromUserName).then(res => {
        this.init()
      })
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
                "    <div class=\"tip left\">" + "<span>" + this.analyzeEmoji(com[i].content) + "</span>" + "</div>\n" +
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
                "    <div class=\"tip right\">" + "<span>" + this.analyzeEmoji(com[i].content) + "</span>" + "</div>\n" +
                "  </div>\n" +
                "</div>";
          }
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
      if (!this.text.trim()) {
        this.$message({type: 'warning', message: "请输入内容"})
      } else {
        if (typeof (WebSocket) == "undefined") {
          console.log("您的浏览器不支持WebSocket");
        } else {
          console.log("您的浏览器支持WebSocket");
          // 组装待发送的消息 json
          // {"from": "zhang", "to": "admin", "text": "聊天文本"}
          // this.text = this.analyzeEmoji(this.text)
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
        let socketUrl = "ws://118.89.125.143:9999/api/imserver/" + username;
        // let socketUrl = "ws://47.120.9.50:9999/api/imserver/" + username;
        // let socketUrl = "ws://localhost:9999/api/imserver/" + username;
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
          console.log("收到数据====" + msg.data) // 收到数据===={"from":"lzh","text":"测试\n"}

          let data = JSON.parse(msg.data)  // 对收到的json数据进行解析， 类似这样的： {"users": [{"username": "zhang"},{ "username": "admin"}]}
          if (data.users) {  // 获取在线人员信息
            _this.users = data.users.filter(user => user.username !== username);  // 获取当前连接的所有用户信息，并且排除自身，自己不会出现在自己的聊天列表里
            let users = []
            for (let i = 0; i < _this.users.length; i++) {
              users.push(_this.users[i].username)
            }
            getMsgByFromAndToName(username, users).then(res => {
              _this.users = res.data
            })
          } else {
            updateIsRead(username, this.fromUserName).then(res => {
            })
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
  height: auto;
  width: auto;
  max-width: 600px;
  display: inline-block !important;
  display: inline;

  height: auto;
}

.tip img {
  /*width: 500px;*/
  max-width: 600px;
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
  background-color: #eeb8b8;
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
  background-color: #f3e4e4;
  border-radius: 32px;
}


li {
  list-style: none;
}

.tcolorm:hover {
  color: red;
}

.tmsgBox {
  position: relative;
  background: #fff;
  padding: 15px;
  margin-bottom: 20px;
  border-radius: 5px;
}

.tmsg-respond h3 {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 8px;
}

.tmsg-respond h3 small {
  font-size: smaller;
  cursor: pointer;
}

.tmsg-respond textarea {
  background: #f4f6f7;
  height: 100px;
  margin-bottom: 10px;
}

.OwO {
  position: relative;
  z-index: 1;
}

.OwO .OwO-logo {
  position: relative;
  border-radius: 4px;
  color: #444;
  display: inline-block;
  background: #fff;
  border: 1px solid #ddd;
  font-size: 13px;
  padding: 0 6px;
  cursor: pointer;
  height: 30px;
  box-sizing: border-box;
  z-index: 2;
  line-height: 30px;
}

.OwO .OwO-logo:hover {
  animation: a 5s infinite ease-in-out;
  -webkit-animation: a 5s infinite ease-in-out;
}

.OwO .OwO-body {
  position: absolute;
  background: #fff;
  border: 1px solid #ddd;
  z-index: 1;
  top: 29px;
  border-radius: 0 4px 4px 4px;
  display: none;
}

.OwO-open .OwO-body {
  display: block;
}

.OwO-open .OwO-logo {
  border-radius: 4px 4px 0 0;
  border-bottom: none;
}

.OwO-open .OwO-logo:hover {
  animation: none;
  -webkit-animation: none;
}

.OwO .OwO-items {
  max-height: 197px;
  overflow: scroll;
  font-size: 0;
  padding: 10px;
  z-index: 1
}

.OwO .OwO-items .OwO-item {
  background: #f7f7f7;
  padding: 5px 10px;
  border-radius: 5px;
  display: inline-block;
  margin: 0 10px 12px 0;
  transition: 0.3s;
  line-height: 19px;
  font-size: 20px;
  cursor: pointer;
}

.OwO .OwO-items .OwO-item:hover {
  background: #eee;
  box-shadow: 0 2px 2px 0 rgba(0, 0, 0, .14),
  0 3px 1px -2px rgba(0, 0, 0, .2),
  0 1px 5px 0 rgba(0, 0, 0, .12);
  animation: a 5s infinite ease-in-out;
  -webkit-animation: a 5s infinite ease-in-out;
}

.OwO .OwO-body .OwO-bar {
  width: 100%;
  height: 30px;
  border-top: 1px solid #ddd;
  background: #fff;
  border-radius: 0 0 4px 4px;
  color: #444;
}

.OwO .OwO-body .OwO-bar .OwO-packages li {
  display: inline-block;
  line-height: 30px;
  font-size: 14px;
  padding: 0 10px;
  cursor: pointer;
  margin-right: 3px;
  text-align: center;
}

.OwO .OwO-body .OwO-bar .OwO-packages li:first-of-type {
  border-radius: 0 0 0 3px;
}

@-webkit-keyframes a {
  2% {
    -webkit-transform: translateY(1.5px) rotate(1.5deg);
    transform: translateY(1.5px) rotate(1.5deg)
  }
  4% {
    -webkit-transform: translateY(-1.5px) rotate(-.5deg);
    transform: translateY(-1.5px) rotate(-.5deg)
  }
  6% {
    -webkit-transform: translateY(1.5px) rotate(-1.5deg);
    transform: translateY(1.5px) rotate(-1.5deg)
  }
  8% {
    -webkit-transform: translateY(-1.5px) rotate(-1.5deg);
    transform: translateY(-1.5px) rotate(-1.5deg)
  }
  10% {
    -webkit-transform: translateY(2.5px) rotate(1.5deg);
    transform: translateY(2.5px) rotate(1.5deg)
  }
  12% {
    -webkit-transform: translateY(-.5px) rotate(1.5deg);
    transform: translateY(-.5px) rotate(1.5deg)
  }
  14% {
    -webkit-transform: translateY(-1.5px) rotate(1.5deg);
    transform: translateY(-1.5px) rotate(1.5deg)
  }
  16% {
    -webkit-transform: translateY(-.5px) rotate(-1.5deg);
    transform: translateY(-.5px) rotate(-1.5deg)
  }
  18% {
    -webkit-transform: translateY(.5px) rotate(-1.5deg);
    transform: translateY(.5px) rotate(-1.5deg)
  }
  20% {
    -webkit-transform: translateY(-1.5px) rotate(2.5deg);
    transform: translateY(-1.5px) rotate(2.5deg)
  }
  22% {
    -webkit-transform: translateY(.5px) rotate(-1.5deg);
    transform: translateY(.5px) rotate(-1.5deg)
  }
  24% {
    -webkit-transform: translateY(1.5px) rotate(1.5deg);
    transform: translateY(1.5px) rotate(1.5deg)
  }
  26% {
    -webkit-transform: translateY(.5px) rotate(.5deg);
    transform: translateY(.5px) rotate(.5deg)
  }
  28% {
    -webkit-transform: translateY(.5px) rotate(1.5deg);
    transform: translateY(.5px) rotate(1.5deg)
  }
  30% {
    -webkit-transform: translateY(-.5px) rotate(2.5deg);
    transform: translateY(-.5px) rotate(2.5deg)
  }
  32%, 34% {
    -webkit-transform: translateY(1.5px) rotate(-.5deg);
    transform: translateY(1.5px) rotate(-.5deg)
  }
  36% {
    -webkit-transform: translateY(-1.5px) rotate(2.5deg);
    transform: translateY(-1.5px) rotate(2.5deg)
  }
  38% {
    -webkit-transform: translateY(1.5px) rotate(-1.5deg);
    transform: translateY(1.5px) rotate(-1.5deg)
  }
  40% {
    -webkit-transform: translateY(-.5px) rotate(2.5deg);
    transform: translateY(-.5px) rotate(2.5deg)
  }
  42% {
    -webkit-transform: translateY(2.5px) rotate(-1.5deg);
    transform: translateY(2.5px) rotate(-1.5deg)
  }
  44% {
    -webkit-transform: translateY(1.5px) rotate(.5deg);
    transform: translateY(1.5px) rotate(.5deg)
  }
  46% {
    -webkit-transform: translateY(-1.5px) rotate(2.5deg);
    transform: translateY(-1.5px) rotate(2.5deg)
  }
  48% {
    -webkit-transform: translateY(-.5px) rotate(.5deg);
    transform: translateY(-.5px) rotate(.5deg)
  }
  50% {
    -webkit-transform: translateY(.5px) rotate(.5deg);
    transform: translateY(.5px) rotate(.5deg)
  }
  52% {
    -webkit-transform: translateY(2.5px) rotate(2.5deg);
    transform: translateY(2.5px) rotate(2.5deg)
  }
  54% {
    -webkit-transform: translateY(-1.5px) rotate(1.5deg);
    transform: translateY(-1.5px) rotate(1.5deg)
  }
  56% {
    -webkit-transform: translateY(2.5px) rotate(2.5deg);
    transform: translateY(2.5px) rotate(2.5deg)
  }
  58% {
    -webkit-transform: translateY(.5px) rotate(2.5deg);
    transform: translateY(.5px) rotate(2.5deg)
  }
  60% {
    -webkit-transform: translateY(2.5px) rotate(2.5deg);
    transform: translateY(2.5px) rotate(2.5deg)
  }
  62% {
    -webkit-transform: translateY(-.5px) rotate(2.5deg);
    transform: translateY(-.5px) rotate(2.5deg)
  }
  64% {
    -webkit-transform: translateY(-.5px) rotate(1.5deg);
    transform: translateY(-.5px) rotate(1.5deg)
  }
  66% {
    -webkit-transform: translateY(1.5px) rotate(-.5deg);
    transform: translateY(1.5px) rotate(-.5deg)
  }
  68% {
    -webkit-transform: translateY(-1.5px) rotate(-.5deg);
    transform: translateY(-1.5px) rotate(-.5deg)
  }
  70% {
    -webkit-transform: translateY(1.5px) rotate(.5deg);
    transform: translateY(1.5px) rotate(.5deg)
  }
  72% {
    -webkit-transform: translateY(2.5px) rotate(1.5deg);
    transform: translateY(2.5px) rotate(1.5deg)
  }
  74% {
    -webkit-transform: translateY(-.5px) rotate(.5deg);
    transform: translateY(-.5px) rotate(.5deg)
  }
  76% {
    -webkit-transform: translateY(-.5px) rotate(2.5deg);
    transform: translateY(-.5px) rotate(2.5deg)
  }
  78% {
    -webkit-transform: translateY(-.5px) rotate(1.5deg);
    transform: translateY(-.5px) rotate(1.5deg)
  }
  80% {
    -webkit-transform: translateY(1.5px) rotate(1.5deg);
    transform: translateY(1.5px) rotate(1.5deg)
  }
  82% {
    -webkit-transform: translateY(-.5px) rotate(.5deg);
    transform: translateY(-.5px) rotate(.5deg)
  }
  84% {
    -webkit-transform: translateY(1.5px) rotate(2.5deg);
    transform: translateY(1.5px) rotate(2.5deg)
  }
  86% {
    -webkit-transform: translateY(-1.5px) rotate(-1.5deg);
    transform: translateY(-1.5px) rotate(-1.5deg)
  }
  88% {
    -webkit-transform: translateY(-.5px) rotate(2.5deg);
    transform: translateY(-.5px) rotate(2.5deg)
  }
  90% {
    -webkit-transform: translateY(2.5px) rotate(-.5deg);
    transform: translateY(2.5px) rotate(-.5deg)
  }
  92% {
    -webkit-transform: translateY(.5px) rotate(-.5deg);
    transform: translateY(.5px) rotate(-.5deg)
  }
  94% {
    -webkit-transform: translateY(2.5px) rotate(.5deg);
    transform: translateY(2.5px) rotate(.5deg)
  }
  96% {
    -webkit-transform: translateY(-.5px) rotate(1.5deg);
    transform: translateY(-.5px) rotate(1.5deg)
  }
  98% {
    -webkit-transform: translateY(-1.5px) rotate(-.5deg);
    transform: translateY(-1.5px) rotate(-.5deg)
  }
  0%, to {
    -webkit-transform: translate(0) rotate(0deg);
    transform: translate(0) rotate(0deg)
  }
}

@keyframes a {
  2% {
    -webkit-transform: translateY(1.5px) rotate(1.5deg);
    transform: translateY(1.5px) rotate(1.5deg)
  }
  4% {
    -webkit-transform: translateY(-1.5px) rotate(-.5deg);
    transform: translateY(-1.5px) rotate(-.5deg)
  }
  6% {
    -webkit-transform: translateY(1.5px) rotate(-1.5deg);
    transform: translateY(1.5px) rotate(-1.5deg)
  }
  8% {
    -webkit-transform: translateY(-1.5px) rotate(-1.5deg);
    transform: translateY(-1.5px) rotate(-1.5deg)
  }
  10% {
    -webkit-transform: translateY(2.5px) rotate(1.5deg);
    transform: translateY(2.5px) rotate(1.5deg)
  }
  12% {
    -webkit-transform: translateY(-.5px) rotate(1.5deg);
    transform: translateY(-.5px) rotate(1.5deg)
  }
  14% {
    -webkit-transform: translateY(-1.5px) rotate(1.5deg);
    transform: translateY(-1.5px) rotate(1.5deg)
  }
  16% {
    -webkit-transform: translateY(-.5px) rotate(-1.5deg);
    transform: translateY(-.5px) rotate(-1.5deg)
  }
  18% {
    -webkit-transform: translateY(.5px) rotate(-1.5deg);
    transform: translateY(.5px) rotate(-1.5deg)
  }
  20% {
    -webkit-transform: translateY(-1.5px) rotate(2.5deg);
    transform: translateY(-1.5px) rotate(2.5deg)
  }
  22% {
    -webkit-transform: translateY(.5px) rotate(-1.5deg);
    transform: translateY(.5px) rotate(-1.5deg)
  }
  24% {
    -webkit-transform: translateY(1.5px) rotate(1.5deg);
    transform: translateY(1.5px) rotate(1.5deg)
  }
  26% {
    -webkit-transform: translateY(.5px) rotate(.5deg);
    transform: translateY(.5px) rotate(.5deg)
  }
  28% {
    -webkit-transform: translateY(.5px) rotate(1.5deg);
    transform: translateY(.5px) rotate(1.5deg)
  }
  30% {
    -webkit-transform: translateY(-.5px) rotate(2.5deg);
    transform: translateY(-.5px) rotate(2.5deg)
  }
  32%, 34% {
    -webkit-transform: translateY(1.5px) rotate(-.5deg);
    transform: translateY(1.5px) rotate(-.5deg)
  }
  36% {
    -webkit-transform: translateY(-1.5px) rotate(2.5deg);
    transform: translateY(-1.5px) rotate(2.5deg)
  }
  38% {
    -webkit-transform: translateY(1.5px) rotate(-1.5deg);
    transform: translateY(1.5px) rotate(-1.5deg)
  }
  40% {
    -webkit-transform: translateY(-.5px) rotate(2.5deg);
    transform: translateY(-.5px) rotate(2.5deg)
  }
  42% {
    -webkit-transform: translateY(2.5px) rotate(-1.5deg);
    transform: translateY(2.5px) rotate(-1.5deg)
  }
  44% {
    -webkit-transform: translateY(1.5px) rotate(.5deg);
    transform: translateY(1.5px) rotate(.5deg)
  }
  46% {
    -webkit-transform: translateY(-1.5px) rotate(2.5deg);
    transform: translateY(-1.5px) rotate(2.5deg)
  }
  48% {
    -webkit-transform: translateY(-.5px) rotate(.5deg);
    transform: translateY(-.5px) rotate(.5deg)
  }
  50% {
    -webkit-transform: translateY(.5px) rotate(.5deg);
    transform: translateY(.5px) rotate(.5deg)
  }
  52% {
    -webkit-transform: translateY(2.5px) rotate(2.5deg);
    transform: translateY(2.5px) rotate(2.5deg)
  }
  54% {
    -webkit-transform: translateY(-1.5px) rotate(1.5deg);
    transform: translateY(-1.5px) rotate(1.5deg)
  }
  56% {
    -webkit-transform: translateY(2.5px) rotate(2.5deg);
    transform: translateY(2.5px) rotate(2.5deg)
  }
  58% {
    -webkit-transform: translateY(.5px) rotate(2.5deg);
    transform: translateY(.5px) rotate(2.5deg)
  }
  60% {
    -webkit-transform: translateY(2.5px) rotate(2.5deg);
    transform: translateY(2.5px) rotate(2.5deg)
  }
  62% {
    -webkit-transform: translateY(-.5px) rotate(2.5deg);
    transform: translateY(-.5px) rotate(2.5deg)
  }
  64% {
    -webkit-transform: translateY(-.5px) rotate(1.5deg);
    transform: translateY(-.5px) rotate(1.5deg)
  }
  66% {
    -webkit-transform: translateY(1.5px) rotate(-.5deg);
    transform: translateY(1.5px) rotate(-.5deg)
  }
  68% {
    -webkit-transform: translateY(-1.5px) rotate(-.5deg);
    transform: translateY(-1.5px) rotate(-.5deg)
  }
  70% {
    -webkit-transform: translateY(1.5px) rotate(.5deg);
    transform: translateY(1.5px) rotate(.5deg)
  }
  72% {
    -webkit-transform: translateY(2.5px) rotate(1.5deg);
    transform: translateY(2.5px) rotate(1.5deg)
  }
  74% {
    -webkit-transform: translateY(-.5px) rotate(.5deg);
    transform: translateY(-.5px) rotate(.5deg)
  }
  76% {
    -webkit-transform: translateY(-.5px) rotate(2.5deg);
    transform: translateY(-.5px) rotate(2.5deg)
  }
  78% {
    -webkit-transform: translateY(-.5px) rotate(1.5deg);
    transform: translateY(-.5px) rotate(1.5deg)
  }
  80% {
    -webkit-transform: translateY(1.5px) rotate(1.5deg);
    transform: translateY(1.5px) rotate(1.5deg)
  }
  82% {
    -webkit-transform: translateY(-.5px) rotate(.5deg);
    transform: translateY(-.5px) rotate(.5deg)
  }
  84% {
    -webkit-transform: translateY(1.5px) rotate(2.5deg);
    transform: translateY(1.5px) rotate(2.5deg)
  }
  86% {
    -webkit-transform: translateY(-1.5px) rotate(-1.5deg);
    transform: translateY(-1.5px) rotate(-1.5deg)
  }
  88% {
    -webkit-transform: translateY(-.5px) rotate(2.5deg);
    transform: translateY(-.5px) rotate(2.5deg)
  }
  90% {
    -webkit-transform: translateY(2.5px) rotate(-.5deg);
    transform: translateY(2.5px) rotate(-.5deg)
  }
  92% {
    -webkit-transform: translateY(.5px) rotate(-.5deg);
    transform: translateY(.5px) rotate(-.5deg)
  }
  94% {
    -webkit-transform: translateY(2.5px) rotate(.5deg);
    transform: translateY(2.5px) rotate(.5deg)
  }
  96% {
    -webkit-transform: translateY(-.5px) rotate(1.5deg);
    transform: translateY(-.5px) rotate(1.5deg)
  }
  98% {
    -webkit-transform: translateY(-1.5px) rotate(-.5deg);
    transform: translateY(-1.5px) rotate(-.5deg)
  }
  0%, to {
    -webkit-transform: translate(0) rotate(0deg);
    transform: translate(0) rotate(0deg)
  }
}

/*用户输入表单*/
.tmsg-r-info {
  margin: 10px 0;
}

.tmsg-r-info input {
  height: 30px;
  border-radius: 4px;
  background: #f4f6f7;
}

.tmsg-r-info .info-submit {
  margin: 10px 0;
  text-align: center;
}

.tmsg-r-info .info-submit p, .tmsg-commentshow h1 {
  /*background: #97dffd;*/
  color: #fff;
  border-radius: 5px;
  cursor: pointer;
  /*transition: all .3s ease-in-out;*/
  height: 30px;
  line-height: 30px;
  text-align: center;
}

/*.tmsg-r-info .info-submit p:hover{
    background: #47456d;
}*/
/*评论列表*/
.tmsg-comments .tmsg-comments-tip {
  display: block;
  border-left: 2px solid #363d4c;
  padding: 0 10px;
  margin: 40px 0;
  font-size: 20px;
}

.tmsg-commentlist {
  margin-bottom: 20px;

}

.tmsg-commentshow > .tmsg-commentlist {
  border-bottom: 1px solid #e5eaed;
}

.tmsg-c-item {
  border-top: 1px solid #e5eaed;
}

.tmsg-c-item article {
  margin: 20px 0;
}

.tmsg-c-item article header {
  margin-bottom: 10px;
}

.tmsg-c-item article header img {
  width: 65px;
  height: 65px;
  border-radius: 50%;
  float: left;
  transition: all .4s ease-in-out;
  -webkit-transition: all .4s ease-in-out;
  margin-right: 15px;
  object-fit: cover;
}

.tmsg-c-item article header img:hover {
  transform: rotate(360deg);
  -webkit-transform: rotate(360deg);
}

.tmsg-c-item article header .i-name {
  font-size: 14px;
  margin: 5px 8px 7px 0;
  color: #444;
  font-weight: bold;
  display: inline-block;
}

.tmsg-c-item article header .i-class {
  display: inline-block;
  margin-left: 10px;
  background: #dff0d8;
  color: #3c763d;
  border-radius: 5px;
  padding: 3px 6px;
  font-size: 12px;
  font-weight: 400;
}

.tmsg-c-item article header .i-time {
  color: #aaa;
  font-size: 12px;
}

.tmsg-c-item article section {
  margin-left: 80px;
}

.tmsg-c-item article section p img {
  vertical-align: middle;
}

.tmsg-c-item article section .tmsg-replay {
  margin: 10px 0;
  font-size: 12px;
  color: #64609E;
  cursor: pointer;
}
</style>
