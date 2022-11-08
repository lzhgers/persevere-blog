<template>
  <div class="box">
    <div class="top"></div>
    <div class="form">
      <el-form ref="form" :model="userForm" label-width="80px" :rules="rules">
        <el-form-item>
          <h1 style="text-align: center">登陆</h1>
        </el-form-item>
        <br>
        <el-form-item prop="userName">
          <el-input placeholder="用户名" prefix-icon="el-icon-s-custom" v-model="userForm.userName"></el-input>
        </el-form-item>
        <el-form-item style="margin-top: 20px" prop="password">
          <el-input @keyup.enter.native="onSubmit" prefix-icon="el-icon-lock" show-password type="password" placeholder="密码" v-model="userForm.password"></el-input>
        </el-form-item>

        <el-form-item label-width="300px">
         <router-link style="color: #fff" to="/forgetPassword">
           <p class="wjmm" style="font-size: 10px;">忘记密码</p>
         </router-link>
        </el-form-item>
        <el-form-item style="text-align: center;width: 350px">
          <el-button style="width: 270px" type="primary" @click="onSubmit">登陆</el-button>
        </el-form-item>
        <el-form-item>
          <br>
          <hr>
          <router-link class="regist" style="margin-left: 210px;font-size: 15px;margin-top: 10px" to="/regist">
            立即注册
          </router-link>

        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import {login} from "@/api/user";
import {setToken} from "../../utils/auth";

export default {
  name: "Login",
  data() {
    return {
      userForm: {
        userName: '',
        password: ''
      },
      rules: {
        userName: [
          {required: true, message: '请输入昵称', trigger: 'blur'},
          {min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur'},
        ],
      }
    }
  },
  methods: {
    onSubmit() {
      console.log('login.................')
      let userName = this.userForm.userName.trim();
      let password = this.userForm.password;

      if (!userName) {
        this.$message.error('用户名不能为空')
        return;
      } else if (!password) {
        this.$message.error('密码不能为空')
        return;
      }
      login(userName, password).then(res => {
        console.log(res)
        setToken(res.data.token)
        localStorage.setItem("userInfo", JSON.stringify(res.data.userInfo))

        this.$message({
          message: '登陆成功',
          type: 'success'
        })
        if (this.$route.query.type === 'w') {
          this.$router.push('/article/write');
        } else if (this.$route.query.type === 'm') {
          this.$router.push('/article/detail/' + this.$route.query.aid)
        }else if (this.$route.query.type === 'c') {
          this.$router.push('/chat')
        } else if (this.$route.query.type === 'ch') {
          this.$router.push('im')
        } else {
          this.$router.push("/home");
        }
      });
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
  background-color: #d98f8f;
  width: 100%;
  height: 700px;
  color: #fff;
}

.form {
  width: 400px;
  border: 1px solid #c4e1bb;
  margin: 50px auto;
  padding-right: 80px;
}

.wjmm:hover, .regist:hover {
  color: #c1eeb4;
  cursor: pointer;
  text-decoration: underline;
}

.regist {
  color: #fff;
  text-decoration: none;
}
</style>
