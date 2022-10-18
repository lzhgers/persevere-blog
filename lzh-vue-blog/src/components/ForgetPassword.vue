<template>
  <div>

    <div class="login" clearfix>

      <div class="login-wrap">
        <el-row type="flex" justify="center" v-if="active === 1">
          <el-form ref="loginForm" :model="user" status-icon label-width="80px" @submit.native.prevent>
            <h3>找回密码</h3>
            <hr>
            <p>请输入与要找回的账户关联的邮箱</p>
            <el-input @keyup.enter.native="checkEmail" v-model="user.email" placeholder="请输入邮箱"></el-input>
            <router-link to="/">返回主页</router-link>
            <el-form-item>
              <el-button type="warning" icon="el-icon-arrow-right" @click="checkEmail">下一步</el-button>
            </el-form-item>
          </el-form>
        </el-row>
        <el-row type="flex" justify="center" v-if="active === 2">
          <el-form ref="loginForm" :model="user" status-icon label-width="80px" @submit.native.prevent>
            <h3>邮箱验证</h3>
            <hr>
            <p style="font-size: 10px">通过账户关联邮箱 {{ user.email }} 进行验证。</p>
            <el-input @keyup.enter.native="checkCode" v-model="user.code" placeholder="请输入验证码">
              <el-button v-if="isSend" slot="append" @click="sendCode">发送</el-button>
              <el-button v-if="!isSend" slot="append" disabled>{{ count }}秒</el-button>
            </el-input>

            <router-link to="/">返回主页</router-link>
            <el-form-item>
              <el-button type="warning" icon="el-icon-arrow-right" @click="checkCode">下一步</el-button>
            </el-form-item>
          </el-form>
        </el-row>

        <el-row type="flex" justify="center" v-if="active === 3">
          <el-form ref="loginForm" :model="user" status-icon label-width="80px" :rules="rules">
            <h3 style="margin-left: 10px">重置密码</h3>
            <hr>
            <p style="font-size: 10px"></p>
            <el-form-item prop="newPassword">
              <el-input style="width: 300px;margin-left: -70px" v-model="user.newPassword"
                        placeholder="请输入新密码"></el-input>
            </el-form-item>
            <el-form-item prop="conPassword">
              <el-input style="width: 300px;margin-left: -70px" v-model="user.conPassword"
                        placeholder="请再次输入密码"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button style="margin-left: 35px" type="warning" icon="el-icon-check" @click="confirmPassword">确定
              </el-button>
            </el-form-item>
          </el-form>
        </el-row>

      </div>
    </div>
  </div>
</template>

<script>
import {checkEmail, checkCode, rePassword, getRePwdCode} from "@/api/user";

export default {
  name: "ForgetPassword",
  data() {
    let validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'));
      } else if (value !== this.user.newPassword) {
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    };
    return {
      user: {
        email: '',
        code: '',
        newPassword: '',
        conPassword: ''
      },
      active: 1,
      isSend: true,
      count: 0,

      rules: {
        newPassword: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur'}
        ],
        conPassword: [
          {required: true, message: '请再次输入密码', trigger: 'blur'},
          {validator: validatePass2, trigger: 'blur'}
        ],
      }
    }
  },
  created() {
    this.isSend = true
    this.active = 1
    this.count = 0
  },
  methods: {
    checkEmail() {
      if (!this.user.email) {
        this.$message.error('请输入邮箱');
        return
      }
      checkEmail(this.user.email).then(res => {
        if (res.code === 303) {
          this.$message.error("该邮箱没有关联的账户")
        } else if (res.code === 200) {
          this.active = 2
          this.sendCode()
        }
      });
    },
    checkCode() {
      if (!this.user.code) {
        this.$message.error('请输入验证码')
        return;
      }
      checkCode(this.user.email, this.user.code).then(res => {
        if (res.code === 200) {
          this.active = 3
        } else {
          this.$message.error(res.msg)
        }
      });
    },
    confirmPassword() {
      rePassword(this.user.email, this.user.newPassword, this.user.conPassword).then(res => {
        if (res.code === 200) {
          this.$message.success("密码重置成功");
          this.$router.push('/login')
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    timeDown() {
      this.isSend = false;//倒计时
      this.count = 60; //赋值3秒
      let times = setInterval(() => {
        this.count--; //递减
        if (this.count <= 0) {
          // <=0 变成获取按钮
          this.isSend = true;
          clearInterval(times);
        }
      }, 1000); //1000毫秒后执行
    },
    sendCode() {
      getRePwdCode(this.user.email).then(res => {})
      this.timeDown()
    }
  }
}
</script>

<style scoped>

.login {
  width: 100%;
  height: 740px;
  background: url("@/assets/bcimg/bc5.jpeg") no-repeat;
  background-size: cover;
  overflow: hidden;
  opacity: 0.8;
}

.login-wrap {
  background: url("../assets/bcimg/sl.jpeg") no-repeat;
  /*background-color: #e5c3e3;*/
  background-size: cover;
  width: 400px;
  height: 300px;
  margin: 100px auto;
  overflow: hidden;
  padding-top: 10px;
  line-height: 40px;
  border-radius: 20px;
  opacity: 0.9;
}

h3 {
  color: rgba(160, 11, 234, 0.72);
  font-size: 24px;
}

hr {
  background-color: #444;
  margin: 20px auto;
}

a {
  text-decoration: none;
  color: #aaa;
  font-size: 15px;
}

a:hover {
  color: #c550ff;
}
</style>
