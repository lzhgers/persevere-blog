<template>
  <div class="box">
    <div class="top"></div>
    <div class="form">
      <el-form ref="form" :model="userForm" :rules="rules" label-width="80px">
        <el-form-item>
          <h1 style="text-align: center">注册</h1>
        </el-form-item>
        <br>

        <el-form-item prop="email">
          <el-input placeholder="邮箱" v-model="userForm.email"></el-input>
        </el-form-item>
        <el-form-item prop="userName" style="margin-top: 20px">
          <el-input placeholder="用户名" v-model="userForm.userName"></el-input>
        </el-form-item>
        <el-form-item prop="password" style="margin-top: 20px">
          <el-input type="password" placeholder="密码" v-model="userForm.password"></el-input>
        </el-form-item>
        <el-form-item prop="confirmPassword" style="margin-top: 20px">
          <el-input type="password" placeholder="确认密码" v-model="userForm.confirmPassword"></el-input>
        </el-form-item>
        <el-form-item style="text-align: center;width: 350px;margin-top: 20px">
          <el-button style="width: 270px" type="primary" @click="onSubmit">注册</el-button>
        </el-form-item>
        <el-form-item>
          <br>
          <hr>
          <router-link class="login" style="margin-left: 135px;font-size: 15px;margin-top: 10px" to="/login">
            已有账号，立即登陆
          </router-link>
        </el-form-item>
      </el-form>
    </div>


    <el-dialog title="账户激活 邮箱验证" :visible.sync="dialogFormVisible">
      <el-form :rules="rules">
        <el-form-item label="" :label-width="formLabelWidth">
          <span style="font-size: 10px">已将验证码发送至 {{ userForm.email }}</span>
        </el-form-item>
        <el-form-item prop="code" :label-width="formLabelWidth">
          <el-input style="width: 180px" placeholder="请输入验证码" v-model="userForm.code"></el-input>
          <el-button v-if="countDown" style="width: 95px" @click="reSend">
            <span>重新发送</span>
          </el-button>
          <el-button v-if="!countDown" style="width: 100px" @click="reSend" disabled>
            <span style="margin-left: -10px">重新发送({{count}})</span>
          </el-button>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="registUser">完成注册</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import {regist, getEmailCode} from "@/api/user";

export default {
  name: "Login",
  data() {
    let validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'));
      } else {
        if (this.userForm.password !== '') {
          this.$refs.ruleForm.validateField('confirmPassword');
        }
        callback();
      }
    };
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
    let validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'));
      } else if (value !== this.userForm.password) {
        callback(new Error('两次输入密码不一致!'));
      } else {
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
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur'},
          {validator: validatePass, trigger: 'blur'}
        ],
        confirmPassword: [
          {required: true, message: '请再次输入密码', trigger: 'blur'},
          {min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur'},
          {validator: validatePass2, trigger: 'blur'}
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
      this.dialogFormVisible = true
      getEmailCode(this.userForm.email).then(res => {
        if (res.code === 200) {
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
      })
    }
  }
}
</script>

<style scoped>
.top {
  padding-top: 60px;
}

p {
  float: left;
  display: inline;
}

el-input {
  float: left;
  display: inline;
}

.el-form-item {
  width: 350px;
  margin: 10px auto;
  display: block;
}

.box {
  background-color: #86b29c;
  width: 100%;
  height: 569px;
  color: #fff;
}

.form {
  width: 400px;
  border: 1px solid #c4e1bb;
  margin: 0 auto;
  padding-right: 80px;
}

.wjmm:hover, .login:hover {
  color: #c1eeb4;
  cursor: pointer;
  text-decoration: underline;
}

.login {
  color: #fff;
  text-decoration: none;
}
</style>
