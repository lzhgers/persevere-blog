<template>
  <div class="chat">
    <Header></Header>

    <div class="tmsgBox" ref="tmsgBox" style="margin-bottom: 0">
      <div class="tmsg-respond" ref="respondBox">
        <h3 style="color: #b03232">既然来了，那就留下些什么吧~~~ <small v-show="isRespond" class="tcolorm" @click="removeRespond">取消回复</small></h3>
        <form class="">
          <el-input
              type="textarea"
              :rows="2"
              placeholder="说点什么呢``"
              v-model="textarea">
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
          <el-row class="tmsg-r-info">
            <el-col :span="24" class="info-submit">
              <p class="tcolors-bg" @click="sendMsg">
                <el-button type="primary">{{ sendTip }}</el-button>
              </p>
            </el-col>
          </el-row>
        </form>
      </div>
      <div class="tmsg-comments" ref="listDom">
        <div class="tmsg-commentshow">
          <ul class="tmsg-commentlist">
            <li class="tmsg-c-item" v-for="(item,index) in commentList" :key="'common'+index">
              <article class="">
                <header>
                  <img :src="item.avatar" :onerror="$store.state.errorImg">
                  <div class="i-name">
                    {{ item.userName }}
                  </div>
                  <div class="i-time">
                    <time>{{ item.createTime }}</time>
                  </div>
                </header>
                <section>
                  <p v-html="analyzeEmoji(item.content)">{{ analyzeEmoji(item.content) }}</p>
                  <div v-if="haslogin" class="tmsg-replay"
                       @click="respondRootMsg(item.id,item.toCommentId)">
                    回复
                  </div>
                </section>
              </article>
              <ul v-show="item.children" class="tmsg-commentlist" style="padding-left:60px;">
                <li class="tmsg-c-item" v-for="(citem,cindex) in item.children" :key="'citem'+cindex">
                  <article class="">
                    <header>
                      <img :src="citem.avatar" :onerror="$store.state.errorImg">
                      <div class="i-name">
                        {{ citem.userName }} <span>回复</span> {{ citem.toCommentUserName }}
                      </div>
                      <div class="i-time">
                        <time>{{ citem.createTime }}</time>
                      </div>
                    </header>
                    <section>
                      <p v-html="analyzeEmoji(citem.content)">{{ citem.content }}</p>
                      <div v-show="haslogin" class="tmsg-replay" @click="respondMsg(item.id,citem.id)">
                        回复
                      </div>
                    </section>
                  </article>
                </li>
              </ul>
            </li>

          </ul>
          <h1 v-show='hasMore' class="tcolors-bg" @click="addMoreFun" style="color: #de4131;font-size: 20px">查看更多</h1>
          <h1 v-show='!hasMore' class="tcolors-bg" style="color: #de4131;font-size: 20px">没有更多</h1>
        </div>
      </div>
    </div>
    <Footer></Footer>

  </div>
</template>

<script>
import Header from "@/components/Header";
import Footer from "@/components/Footer";
import Message from "@/components/Message";
import {getCommentByCommentId} from "@/api/comment";
import {getToken} from "../../utils/auth";
import {getAllChatComment} from "@/api/comment";
import {sendChat} from "@/api/comment";
import {hideFullScreenLoading, showFullScreenLoading} from "../../utils/loading";

export default {
  name: "Chat",
  components: {
    Header,
    Footer,
    Message
  },
  data() { //选项 / 数据
    return {
      respondBox: '',//评论表单
      listDom: '',//评论列表
      tmsgBox: '',//总评论盒子
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
      },
      isRespond: false,
      textarea: '',//文本框输入内容
      pBody: true,//表情打开控制
      commentList: [],//评论列表数据
      hasMore: false,
      haslogin: true,
      userId: '',//用户id
      type: '2',//回复评论的当前的commentId
      leavePid: '',//赞赏等其他模块的分类id
      pid: '',//回复评论的一级commentId
      rootId: -1,//根评论id，如果是针对文字评论直接用-1表示
      toCommentId: -1,//所回复评论的id
      toCommentUserId: -1,//所评论的用户id
      sendTip: '发送~',
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
    var that = this;
    that.routeChange();
  },
  methods: { //事件处理器
    setData(initData, result) {
      console.log('--------------------------' + this.$store.state)
      console.log(result)
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
      this.textarea += '[' + inner + ']';
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
        // console.log(str);
      }
      return str;
    },
    //发送留言
    sendMsg: function () {//留言
      let userInfo = localStorage.getItem("userInfo")
      if (!userInfo) {
        this.$confirm('登录后即可发表评论，是否前往登录页面?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {//确定，跳转至登录页面
          this.$router.push({path: '/login?type=c'});
        }).catch(() => {

        });
      }
      var that = this;
      if (that.textarea) {
        that.sendTip = '咻~~';
        console.log(localStorage.getItem("userInfo"))
        that.toCommentId = JSON.parse(localStorage.getItem("userInfo")).id
        that.aid = this.$route.params.id
        sendChat(that.rootId, that.toCommentId, that.toCommentUserId, that.textarea).then((response) => {
          that.textarea = '';
          that.rootId = -1;
          that.toCommentId = -1;
          that.toCommentUserId = -1;

          that.routeChange();
          that.removeRespond();
          var timer02 = setTimeout(function () {
            that.sendTip = '发送~';
            clearTimeout(timer02);
          }, 1000)
        })
      } else {
        that.sendTip = '内容不能为空~'
        var timer = setTimeout(function () {
          that.sendTip = '发送~';
          clearTimeout(timer);
        }, 3000)
      }
    },
    respondRootMsg: function (rootId, id) {//回复留言
      console.log("rootId: " + rootId);
      console.log("id: " + id);
      var that = this;
      let userInfo = JSON.parse(localStorage.getItem("userInfo"));
      if (userInfo) {
        getCommentByCommentId(rootId).then(res => {
          console.log(res)
          this.toCommentUserId = res.data.toCommentId
        })

        var dom = event.currentTarget;
        dom = dom.parentNode;
        this.isRespond = true;
        //   this.leavePid = leavePid;
        this.rootId = rootId
        this.toCommentId = id;
        dom.appendChild(this.$refs.respondBox);
      } else {
        that.$confirm('登录后即可点赞和收藏，是否前往登录页面?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {//确定，跳转至登录页面
          //储存当前页面路径，登录成功后跳回来
          localStorage.setItem('logUrl', that.$route.fullPath);
          that.$router.push({path: '/Login?login=1'});
        }).catch(() => {

        });
      }
    },

    respondMsg: function (rootId, id) {//回复留言
      var that = this;
      let userInfo = JSON.parse(localStorage.getItem("userInfo"));
      if (userInfo) {
        getCommentByCommentId(id).then(res => {
          console.log(res)
          this.toCommentUserId = res.data.toCommentId
        })

        var dom = event.currentTarget;
        dom = dom.parentNode;
        this.isRespond = true;
        //   this.leavePid = leavePid;
        this.rootId = rootId
        this.toCommentId = id;
        dom.appendChild(this.$refs.respondBox);
      } else {
        that.$confirm('登录后即可点赞和收藏，是否前往登录页面?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {//确定，跳转至登录页面
          //储存当前页面路径，登录成功后跳回来
          localStorage.setItem('logUrl', that.$route.fullPath);
          that.$router.push({path: '/Login?login=1'});
        }).catch(() => {

        });
      }
    },

    removeRespond: function () {//取消回复留言
      this.isRespond = false;
      this.rootId = -1;
      this.toCommentId = -1;
      this.toCommentUserId = -1;
      this.$refs.tmsgBox.insertBefore(this.$refs.respondBox, this.$refs.listDom);
    },
    showCommentList: function (initData) {//评论列表
      showFullScreenLoading()
      var that = this;

      //公用设置数据方法
      console.log(that.$route)

      that.type = 2;
      getAllChatComment(that.queryParams).then((response) => {
        // console.log('-------------------')
        // console.log(response)
        // console.log('-------------------')
        that.setData(initData, response);
        hideFullScreenLoading()
      })
    },
    addMoreFun: function () {//查看更多
      this.queryParams.pageNum++
      this.showCommentList(false);
    }
    ,
    routeChange: function () {//重新加载
      var that = this;
      this.queryParams.pageNum = 1
      this.showCommentList(true);
    }
  },
}
</script>

<style scoped>
li {
  list-style: none;
}

.tcolorm:hover {
  color: red;
}

</style>
