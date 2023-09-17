<template>
  <div class="box">
    <div class="content">
      <div class="login-wrapper">
        <h1>注册</h1>
        <div class="login-form">
          <div class="username form-item">
            <span>邮箱</span>
            <input type="text" class="input-item" v-model="userForm.email">
          </div>
          <div class="password form-item">
            <span>用户名</span>
            <input type="text" class="input-item" v-model="userForm.userName">
          </div>
          <div class="password form-item">
            <span>密码</span>
            <input type="password" class="input-item" v-model="userForm.password">
          </div>
          <div class="password form-item">
            <span>确认密码</span>
            <input type="password" class="input-item" v-model="userForm.confirmPassword" @keyup.enter="onSubmit">
          </div>
          <button class="login-btn" @click="onSubmit">注 册</button>
        </div>
        <div style="margin-top: 10px"></div>
        <div style="width: 100%">
          <div style="float:left;">
            <router-link class="regist" style="margin-left: 10px;margin-top: 10px" to="/login">已有帐号，立即登录
            </router-link>
          </div>
          <div style="float:right;">
            <router-link to="/" class="home">主页</router-link>
          </div>
        </div>
      </div>
    </div>

    <el-dialog title="账户激活 邮箱验证" :visible.sync="dialogFormVisible" class="dialogEmail" width="390px"
               style="margin: 90px 0">
      <el-form :rules="rules" :inline="true">
        <el-form-item label="" :label-width="formLabelWidth">
          <span style="font-size: 10px">已将验证码发送至 {{ userForm.email }}</span>
        </el-form-item>
        <el-form-item prop="code" :label-width="formLabelWidth">
          <el-input style="width: 180px" placeholder="请输入验证码" v-model="userForm.code"></el-input>
          <el-button v-if="countDown" style="width: 105px;height: 40px" @click="reSend">
            <span>重新发送</span>
          </el-button>
          <el-button v-if="!countDown" style="width: 105px;height: 40px" @click="reSend" disabled>
            <span style="margin-left: -10px">重新发送({{ count }})</span>
          </el-button>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false" style="padding: 10px">取 消</el-button>
        <el-button type="primary" @click="registUser" style="padding: 10px">完成注册</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import {regist, getEmailCode} from "@/api/user";

export default {
  name: "Login",
  data() {
    let codeCheck = (rule, value, callback) => {
      if (this.userForm.code === '') {
        callback(new Error('请输入验证码'));
      } else {
        if (this.userForm.code.length !== 6) {
          callback(new Error('验证码为6个数字'));
        }
        callback();
      }
    };
    return {
      countDown: true,
      count: 0,
      userForm: {
        email: '',
        userName: '',
        password: '',
        confirmPassword: '',
        code: ''
      },
      dialogTableVisible: false,
      dialogFormVisible: false,
      formLabelWidth: '70px',

      rules: {
        email: [
          {required: true, message: '请输入邮箱地址', trigger: 'blur'},
          {type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change']}
        ],
        userName: [
          {required: true, message: '请输入昵称', trigger: 'blur'},
          {min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur'}
        ],
        code: [
          {validator: codeCheck, trigger: 'blur'}
        ]
      }
    }
  },
  methods: {
    reSend() {
      getEmailCode(this.userForm.email).then(res => {
        if (res.code === 200) {
          this.$message({
            message: '验证码发送成功',
            type: 'success'
          });
        } else {
          this.$message({
            message: '验证码已发送',
            type: 'success'
          });
        }
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
    onSubmit() {
      if (!this.userForm.email) {
        this.$message.error("邮箱不能为空");
        return;
      }
      if (!this.userForm.userName) {
        this.$message.error("用户名不能为空");
        return;
      }
      if (!this.userForm.password) {
        this.$message.error("请输入密码");
        return;
      }
      if (!this.userForm.confirmPassword) {
        this.$message.error("请再次输入密码");
        return;
      }
      if (this.userForm.password !== this.userForm.confirmPassword) {
        this.$message.error("两次密码输入不一致");
        return;
      }

      getEmailCode(this.userForm.email).then(res => {
        if (res.code === 200) {
          this.dialogFormVisible = true
          this.$message({
            message: '验证码发送成功',
            type: 'success'
          });
          this.timeDown()
        } else {
          this.$message({
            message: '验证码已发送',
            type: 'success'
          });
        }
      })
    },
    registUser() {
      regist(this.userForm.userName, this.userForm.email, this.userForm.password, this.userForm.code).then(res => {
        if (res.code === 200) {
          this.$message({
            message: '注册成功',
            type: 'success'
          });
          this.dialogFormVisible = false;
          this.$router.push("/login");
        } else if (res.code === 310) {
          this.$message({
            message: res.msg,
            type: 'error'
          });
        }
      });
    }
  }
}
</script>


<style scoped>
* {
  margin: 0;
  padding: 0;
}

.regist, .wjmm {
  color: rgba(0, 5, 14, 0.99);
  text-decoration: none;
}

/*公共CSS*/
.box {
  width: 100vw;
  height: 100vh;
  background-color: rgb(29, 67, 89);
}

.box .content .login-wrapper h1 {
  text-align: center;
}

.box .content .login-wrapper .login-form .form-item {
  margin: 20px 0;
}

.box .content .login-wrapper .login-form .form-item span {
  display: block;
  margin: 5px 20px;
  font-weight: 100;
}

.box .content .login-wrapper .login-form .form-item .input-item {
  width: 100%;
  border-radius: 40px;
  padding: 20px;
  box-sizing: border-box;
  font-size: 20px;
  font-weight: 200;
}

.box .content .login-wrapper .login-form .form-item .input-item:focus {
  outline: none;
}

.box .content .login-wrapper .login-form .login-btn {
  width: 100%;
  border-radius: 40px;
  color: #fff;
  border: 0;
  font-weight: 100;
  margin-top: 10px;
  cursor: pointer;
}

.box .content .login-wrapper .divider {
  width: 100%;
  margin: 20px 0;
  text-align: center;
  display: flex;
  align-items: center;
  justify-content: center;
}

.box .content .login-wrapper .divider span:nth-child(1) {
  flex: 1;
}

.box .content .login-wrapper .divider span:nth-child(3) {
  flex: 1;
}

.box .content .login-wrapper .divider .line {
  display: inline-block;
  max-width: 30%;
  width: 30%;
}

.box .content .login-wrapper .divider .divider-text {
  vertical-align: middle;
  margin: 0px 20px;
  line-height: 0px;
  display: inline-block;
  width: 100px;
}

.box .content .login-wrapper .other-login-wrapper {
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.box .content .login-wrapper .other-login-item {
  border: 1px solid rgb(214, 222, 228);
  padding: 10px;
  margin: 10px;
  cursor: pointer;
}

/*一般大于手机的尺寸CSS*/
@media (min-width: 767px) {
  .box {
    background-color: rgb(29, 67, 89);
  }

  .box .content {
    width: 85vw;
    height: 90vh;
    background: url("../assets/login_two.jpg") no-repeat;
    background-size: 90% 100%;
    position: absolute;
    right: 15%;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    border-radius: 20px;
    background-color: #fff;
  }

  .box .content .login-wrapper {
    width: 25vw;
    position: absolute;
    right: 15%;
    top: 50%;
    transform: translateY(-50%);
  }

  .box .content .login-wrapper h1 {
    text-align: center;
    font-size: 45px;
    color: rgb(81, 100, 115);
    margin-bottom: 40px;
  }

  .box .content .login-wrapper .login-form {
    margin: 10px 0;
  }

  .box .content .login-wrapper .login-form .form-item span {
    color: rgb(81, 100, 115);
  }

  .box .content .login-wrapper .login-form .form-item .input-item {
    height: 60px;
    border: 1px solid rgb(214, 222, 228);
  }

  .box .content .login-wrapper .login-form .login-btn {
    height: 50px;
    background-color: rgb(59, 72, 89);
    font-size: 20px;
  }

  .box .content .login-wrapper .divider .line {
    border-bottom: 1px solid rgb(214, 222, 228);
  }

  .box .content .login-wrapper .other-login-item {
    border-radius: 20px;
  }

  .box .content .login-wrapper .other-login-item img {
    width: 40px;
    height: 40px;
  }
}

/*手机端CSS*/
@media (max-width: 768px) {
  .box .content {
    width: 100vw;
    height: 100vh;
    background: url("../assets/login_bg_phone.png") no-repeat;
    background-size: 100% 100%;
    display: flex;
    align-items: flex-start;
    justify-content: center;
  }

  .box .content .login-wrapper {
    width: 70%;
    height: 60%;
    padding-top: 15%;
  }

  .box .content .login-wrapper h1 {
    font-size: 30px;
    color: #fff;
  }

  .box .content .login-wrapper .login-form .form-item {
    margin: 10px 0;
  }

  .box .content .login-wrapper .login-form .form-item span {
    color: rgb(113, 129, 141);
  }

  .box .content .login-wrapper .login-form .form-item .input-item {
    height: 30px;
    border: 1px solid rgb(113, 129, 141);
    background-color: transparent;
    color: #fff;
  }

  .box .content .login-wrapper .login-form .login-btn {
    height: 40px;
    background-color: rgb(235, 95, 93);
    font-size: 16px;
  }

  .box .content .login-wrapper .divider .line {
    border-bottom: 1px solid #fff;
  }

  .box .content .login-wrapper {
    color: #fff;
  }

  .box .content .login-wrapper .other-login-item {
    border-radius: 15px;
  }

  .box .content .login-wrapper .other-login-item img {
    width: 35px;
    height: 35px;
  }
}

.home {
  text-decoration: none;
  background: none;
  color: #000;
}

.regist:hover, .home:hover {
  color: #34536c;
  cursor: pointer;
  text-decoration: underline;
}
</style>