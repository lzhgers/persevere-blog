<!--<template>-->
<!--  <div class="cancel">-->
<!--    <el-steps :active="active" align-center class="step_bar" finish-status="success">-->
<!--      <el-step title="步骤1" description="请输入密码"></el-step>-->
<!--      <el-step title="步骤2" description="请选择验证方式"></el-step>-->
<!--      <el-step title="步骤3" description="邮箱/手机验证"></el-step>-->
<!--      <el-step title="步骤4" description="确认注销账户"></el-step>-->
<!--    </el-steps>-->
<!--    <div class="method">-->
<!--      <el-input placeholder="请输入密码" style="width: 200px;margin-left: 60px;margin-top: 10px"></el-input>-->
<!--      <el-select value=""-->
<!--    </div>-->
<!--    <hr>-->
<!--    <el-button class="nextbtn" @click="next_step">下一步</el-button>-->
<!--    <el-button class="nextbtn" @click="pre_step">上一步</el-button>-->
<!--  </div>-->
<!--</template>-->

<!--<script>-->
<!--export default {-->
<!--  name: "CancelAccount",-->
<!--  components: {},-->
<!--  data() {-->
<!--    return {-->
<!--      active: 0,-->

<!--    }-->
<!--  },-->
<!--  created() {-->

<!--  },-->
<!--  methods: {-->
<!--    next_step() {-->
<!--      if (this.active >= 4) {-->
<!--        return;-->
<!--      }-->
<!--      this.active += 1;-->
<!--    },-->
<!--    pre_step(){-->
<!--      if (this.active <= 0) {-->
<!--        return;-->
<!--      }-->
<!--      this.active -= 1;-->
<!--    }-->
<!--  },-->

<!--}-->
<!--</script>-->

<!--<style scoped>-->
<!--.cancel {-->
<!--  background-color: #f5eded;-->
<!--  height: 500px;-->
<!--}-->

<!--.step_bar {-->
<!--  padding-top: 50px;-->
<!--}-->

<!--.nextbtn {-->
<!--  display: block;-->
<!--}-->

<!--.method {-->
<!--  width: 500px;-->

<!--}-->
<!--</style>-->


<template>
  <div class="page-container">
    <div>
      <div class="title-img-wr">
        <img src="" alt="">
      </div>
      <div class="forgot-password">
        <el-button type="text" v-show="active>='1'" class="return-page-wr" @click="active--"><i
            class="el-icon-arrow-left"></i> 上一步
        </el-button>
        <el-button type="text" v-show="active=='0'" class="return-page-wr" @click="$router.push({path:'/login'})">
          <i class="el-icon-arrow-left"></i> 返回主页
        </el-button>
        <div class="content">
          <el-steps :active="active" finish-status="success">
            <el-step title="输入密码"></el-step>
            <el-step title="邮箱验证"></el-step>
            <el-step title="确认注销"></el-step>
          </el-steps>
          <div v-if="active=='0'">
            <el-form class="demo-ruleForm" @submit.native.prevent>
              <el-form-item prop="account" label="密码" style="margin-top: 10px">
                <el-input v-model="password" type="password" placeholder="请输入密码"></el-input>
              </el-form-item>
            </el-form>
            <div class="btn-wr">
              <button type="button" class="login-btn" style="cursor:pointer;" @click.prevent="checkPassword">下一步
                <i class="el-icon-arrow-right"></i>
              </button>
            </div>
          </div>
          <div v-if="active=='1'">
            <el-form class="demo-ruleForm" @submit.native.prevent>
              <el-form-item :label="'通过邮箱 '+email+' 进行验证:'">
                <el-input style="width: 260px" v-model="code" type="text" placeholder="请输入验证码"
                          auto-complete="off">
                </el-input>
                <el-button v-if="countDown" style="width: 130px" @click="getEmailCode">
                  {{ btn_name === false ? '获取验证码' : '重新发送' }}
                </el-button>
                <el-button v-if="!countDown" style="width: 130px" disabled>重新发送（{{ this.count }}）</el-button>
              </el-form-item>
            </el-form>
            <div class="btn-wr">
              <button type="button" class="login-btn" @click.prevent="checkEmailCode" style="cursor:pointer;">确认 <i
                  class="el-icon-arrow-right"></i></button>
            </div>
          </div>
          <div v-if="active=='2'">
            <div class="reset-success">
              <img src="" alt="">
              <p class="set-success">您确定要注销吗？</p>
            </div>
            <div class="btn-wr">
              <button type="button" class="login-btn" @click="cancelAccount" style="cursor:pointer;">
                确认注销<i style="margin-left: -25px" class="el-icon-circle-check"></i>
              </button>
            </div>
            <div class="btn-wr">
              <button style="margin-top: 20px;background-color: #409EFF;cursor:pointer;" type="button" class="login-btn"
                      @click="$message.info('取消注销');$router.push({path:'/personalCenter'})">
                取消<i class="el-icon-circle-close"></i>
              </button>
            </div>
          </div>
        </div>


      </div>

    </div>

  </div>
</template>


<script>

import {checkPassword} from "@/api/user";
import {sendEmailCode} from "@/api/user";
import {getUserById} from "@/api/user";
import {checkCancelEmailCode} from "@/api/user";
import {cancelAccount} from "@/api/user";
import {removeToken} from "../../utils/auth";
import {logout} from "@/api/user";

export default {
  name: "cancelaccount",
  data() {
    return {
      password: '',
      email: '',
      active: 0,
      code: '',

      btn_name: false,

      countDown: true,
      count: 0,

      uid: -1,

    };
  },
  created() {
    let userInfo = JSON.parse(localStorage.getItem("userInfo"));
    if (!userInfo) {
      this.$router.push('/login')
    }
    this.uid = userInfo.id
    getUserById(this.uid).then(res => {
      this.email = res.data.email
    })
  },
  methods: {
    cancelAccount() {
      cancelAccount(this.uid).then(res=>{
        this.$message.success('注销成功');
      })
      localStorage.removeItem("userInfo")
      removeToken()
      logout()

      this.$router.push({path: '/'})
    },
    checkEmailCode() {
      checkCancelEmailCode(this.email, this.code).then(res => {
        if (res.code === 200) {
          this.$message.success('验证成功')
          this.active = 2
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    getEmailCode() {
      if (this.count > 0) {
        this.$message.warning("验证码已发送")
        return
      }
      sendEmailCode(this.uid, this.email).then(res => {
        this.timeDown()
      });
      this.btn_name = true
    },
    checkPassword() {
      checkPassword(this.uid, this.password).then(res => {
        if (res.code === 303) {
          this.$message.error(res.msg)
          return;
        }
        this.active = 1
      })
    },
    timeDown() {
      this.countDown = false;//倒计时
      this.count = 60; //赋值3秒
      let times = setInterval(() => {
        this.count--; //递减
        if (this.count <= 0) {
          // <=0 变成获取按钮
          this.countDown = true;
          clearInterval(times);
        }
      }, 1000); //1000毫秒后执行
    },
    next() {
      if (this.active++ > 2) this.active = 0;
    },

  },

}
</script>

<style scoped lang="less">
.page-container {
  width: 100%;
  height: 100vh;
  min-height: 600px;
  overflow-y: auto;
  background: url("@/assets/img/haixing.jpeg") no-repeat center;
  background-size: cover;
  display: flex;
  align-items: center;
  justify-content: center;

  .wid {
    width: 100%;
  }

  .title-img-wr {
    text-align: center;
  }

  .forgot-password {
    .return-page-wr {
      position: relative;
      top: 30px;
      left: 30px;
      color: #e5525b;
      font-size: 16px;
      margin-bottom: 15px;

      i {
        font-size: 20px;
        vertical-align: middle;
      }
    }

    width: 580px;
    height: 540px;
    background: #FFFFFF;
    border-radius: 6px;

    .content {
      padding: 30px 90px;
      box-sizing: border-box;
    }

    /deep/ .el-step.is-horizontal {
      .el-step__line {
        height: 2px;
        top: 20px;
        left: 48px;
        right: 10px;
      }
    }

    /deep/ .el-step__icon {
      width: 42px;
      height: 42px;
      color: #e6545a;
      font-size: 18px;
      border: 2px solid #e6545a;
    }

    /deep/ .el-step__line {
      background: #e6545a;
    }

    /deep/ .is-process {
      .el-step__icon {
        background: #e6545a;
        color: #FFFFFF;
        border: 0px;

      }
    }

    /deep/ .el-step__title.is-process {
      color: #e6545a;
    }


    /deep/ .el-form-item__label {
      width: 100%;
      text-align: left;
    }

    /deep/ .el-form-item__content {
      display: inline-block;
      width: 100%;
      margin-left: 0px;
    }
  }

  .btn-wr {
    text-align: center;
  }

  .reset-success {
    text-align: center;
    margin: 60px 0;
  }

  .set-success {
    margin-top: 20px;
    font-weight: 600;
  }

  .login-btn {
    width: 200px;
    height: 50px;
    text-align: center;
    line-height: 50px;
    margin: 0 auto;
    background: #e6545a;
    font-weight: 600;
    font-size: 16px;
    color: #FFFFFF;
    border-radius: 4px;

    i {
      right: -50px;
      font-size: 27px;
      position: relative;
      vertical-align: middle;
    }
  }
}

</style>
