<template>
  <div class="personal">
    <Header ref="sub1"></Header>
    <div class="card">
      <el-card class="box-card1" shadow="hover">
        <div class="left" style="text-align: center">
          <el-avatar :src="userForm.avatar"
                     :size="200" style="margin: 5px 0"></el-avatar>
          <h4 class="userName">{{ userForm.userName }}</h4>
          <div class="createTime" style="margin: 10px 0;font-size: 8px">
            创建日期 ： {{ userForm.createTime }}
          </div>

          <el-upload
              :file-list="fileList"
              class="upload-demo"
              name="img"
              action="upload"
              :limit="1"
              :http-request="handleUpload"
          >
            <el-button round type="success">修改头像</el-button>
          </el-upload>


          <div style="margin: 20px 0 10px 0;font-weight: lighter">—————— <span style="font-weight: normal">联系方式</span>
            ——————
          </div>
          <div style="text-align: left;margin-top: 30px" class="contact">
            <p style="margin-bottom: 15px;margin-top: 10px"><span style="">手机 ：{{ userForm.phonenumber }}</span>
              <el-button @click="$message.info('功能待开发')" style="margin-left: 5px" type="text"><i
                  class="el-icon-mobile-phone"></i>修改手机
              </el-button>
            </p>
            <p><span style="">邮箱 ：{{ userForm.email }}</span>
              <el-button type="text" @click="dialogFormVisibleEmail = true" style="cursor:pointer;margin-left: 5px"><i
                  class="el-icon-edit-outline"></i>修改邮箱
              </el-button>
            </p>
          </div>
        </div>
        <div class="right" style="text-align: center">
          <div class="info" style="margin: 10px 0;font-weight: lighter">———————————— <span style="font-weight: normal">个人信息</span>
            ————————————
          </div>
          <el-form label-width="80px" style="width: 500px">
            <el-form-item label="用户名">
              <el-input v-model="userForm.userName" :disabled="isUpdate"></el-input>
            </el-form-item>
            <el-form-item label="昵称">
              <el-input v-model="userForm.nickName" :disabled="isUpdate"></el-input>
            </el-form-item>
            <el-form-item label="地址">
              <el-input v-model="userForm.address" :disabled="isUpdate"></el-input>
            </el-form-item>
            <el-form-item label="性别" style="">
              <el-select v-model="userForm.sex" placeholder="请选择性别" style="width: 420px" :disabled="isUpdate">
                <el-option @click.native="getSexNum" label="男" value="0"></el-option>
                <el-option @click.native="getSexNum" label="女" value="1"></el-option>
                <el-option @click.native="getSexNum" label="未知" value="2"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="备注">
              <el-input v-model="userForm.remark" type="textarea" :disabled="isUpdate"></el-input>
            </el-form-item>
            <el-button @click="updateBtnInfo" style="width: 470px;margin-left: 35px;margin-bottom: 10px" type="primary">
              {{ updateBtn }}
            </el-button>
            <el-button @click="dialogFormVisible = true" style="width: 470px;margin-left: 35px"
                       type="danger">修改密码
            </el-button>
          </el-form>
        </div>

        <div class="btm">
          <hr>
          <el-button type="warning">注销此账号</el-button>
        </div>


      </el-card>
    </div>

    <el-dialog title="修改密码" :visible.sync="dialogFormVisible">
      <el-form :model="pwdForm" :rules="rules">
        <el-form-item label="当前密码" :label-width="formLabelWidth" prop="curPassword">
          <el-input type="password" v-model="pwdForm.curPassword" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="新密码" :label-width="formLabelWidth" prop="newPassword">
          <el-input type="password" v-model="pwdForm.newPassword" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" :label-width="formLabelWidth" prop="conPassword">
          <el-input type="password" v-model="pwdForm.conPassword" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <router-link to="/" style="text-decoration: none">
          <p class="forgetPwd" style="font-weight: lighter;margin: -50px 0 30px 0;font-size: 10px">已有帐号，忘记密码？</p>
        </router-link>
        <el-button type="primary" @click="updatePwd">修改密码</el-button>
      </div>
    </el-dialog>
    <el-dialog title="账号安全验证" :visible.sync="dialogFormVisibleEmail">
      <el-form :model="emailForm" :rules="rules">
        <h2 style="text-align: center">你正在进行敏感操作，继续操作前请验证您的身份</h2>
        <p style="margin: 20px auto;text-align: center;padding: 10px 0;border: 2px solid #b58105;width: 450px">
          更换邮箱后，你将无法通过 「原邮箱+密码」 登录</p>
        <el-form-item label="密码验证" :label-width="formLabelWidth" prop="curPassword">
          <el-input type="password" :disabled="updatePwdCheck" placeholder="请输入当前账号密码" v-model="emailForm.curPassword"
                    autocomplete="off"
                    style="width: 467px"></el-input>
        </el-form-item>
        <p style="margin-left: 120px;font-weight: bold;margin-bottom: 10px">短信验证 (接收邮箱 {{ userForm.email }}) </p>
        <el-form-item label="邮箱验证" :label-width="formLabelWidth" prop="code">
          <el-input placeholder="请输入邮箱验证码" v-model="emailForm.code" autocomplete="off"
                    style="width: 300px" :disabled="emailCodeInput"></el-input>
          <el-button v-if="emailTimeDown" @click="sendEmailCode" style="width: 165px">发送验证码</el-button>
          <el-button v-if="!emailTimeDown" disabled style="width: 165px">（{{ count }}）s后可重新发送</el-button>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="nextStep">下一步</el-button>
      </div>
    </el-dialog>
    <el-dialog title="新邮箱号绑定" :visible.sync="dialogFormVisibleEmailSuccess">
      <el-form :model="newEmailForm" :rules="rules">
        <h3 style="text-align: center;margin-bottom: 20px">验证成功，请验证新的邮箱号</h3>
        <el-form-item label="邮箱号" :label-width="formLabelWidth" prop="email">
          <el-input :disabled="newEmailInput" placeholder="请输入要验证的邮箱号" v-model="newEmailForm.email" autocomplete="off"
                    style="width: 467px"></el-input>
        </el-form-item>
        <el-form-item label="邮箱验证码" :label-width="formLabelWidth" prop="code">
          <el-input placeholder="请输入邮箱验证码" v-model="newEmailForm.code" autocomplete="off"
                    style="width: 300px" :disabled="newEmailCodeInput"></el-input>
          <el-button v-if="newEmailTimeDown" @click="sendNewEmailCode" style="width: 165px">发送验证码</el-button>
          <el-button v-if="!newEmailTimeDown" disabled style="width: 165px">（{{ newCount }}）s后可重新发送</el-button>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisibleEmailSuccess = false">取消</el-button>
        <el-button type="primary" @click="finishUpdateEmail">确定</el-button>
      </div>
    </el-dialog>

    <Footer></Footer>
  </div>
</template>

<script>
import Header from "@/components/Header";
import Footer from "@/components/Footer";

import {uploadImg} from "@/api/upload";
import {updateUserAvatar, updateUserInfo, getUserInfo, updateUserPassword} from "@/api/personal";
import {getUserById} from "@/api/user";
import {sendEmailCodeToUpdateEmail, sendNewEmailCodeToCheckEmail} from "@/api/personal";
import {checkCode} from "@/api/personal";
import {finishEmailUpdate} from "@/api/personal";
import {showInfo} from "@/api/user";


export default {
  name: "PersonalCenter",
  components: {
    Header,
    Footer
  },
  data() {
    let checkConPwd = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'));
      } else if (value !== this.pwdForm.newPassword) {
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    };
    return {
      count: 0,
      newCount: 0,
      userForm: {
        id: '',
        userName: '',
        nickName: '',
        sex: -1,
        address: '',
        remark: ''
      },
      fileList: [],
      isUpdate: true,
      updateBtn: '修改信息',
      dialogFormVisible: false,
      dialogFormVisibleEmail: false,
      dialogFormVisibleEmailSuccess: false,

      updatePwdCheck: false,

      formLabelWidth: '120px',
      pwdForm: {
        userId: -1,
        curPassword: '',
        newPassword: '',
        conPassword: ''
      },
      emailForm: {
        userId: -1,
        curPassword: '',
        code: '',
        email: ''
      },
      newEmailForm: {
        userId: -1,
        email: '',
        code: ''
      },
      emailCodeInput: true,
      emailTimeDown: true,
      newEmailCodeInput: true,
      newEmailTimeDown: true,

      newEmailInput: false,
      rules: {
        curPassword: [
          {required: true, message: '请输入当前密码', trigger: 'blur'},
          {min: 4, max: 20, message: '长度在 4 到 20 个字符', trigger: 'blur'}
        ],
        newPassword: [
          {required: true, message: '请输入新密码', trigger: 'blur'},
          {min: 4, max: 20, message: '长度在 4 到 20 个字符', trigger: 'blur'}
        ],
        code: [
          {required: true, message: '请输入验证码', trigger: 'blur'},
          {min: 6, max: 6, message: '验证码为6位字符', trigger: 'blur'}
        ],
        conPassword: [
          {required: true, message: '确认密码和新密码保持一致', trigger: 'blur'},
          {validator: checkConPwd, trigger: 'blur'}
        ],
        email: [
          {required: true, message: '请输入邮箱地址', trigger: 'blur'},
          {type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change']}
        ],
      },

    }
  },
  created() {
    this.init()
  },
  methods: {
    finishUpdateEmail() {
      let userInfo = JSON.parse(localStorage.getItem("userInfo"));
      this.newEmailForm.userId = userInfo.id
      finishEmailUpdate(this.newEmailForm).then(res => {
        if (res.code === 200) {
          this.$message.success("邮箱修改成功")
          this.dialogFormVisibleEmailSuccess = false
          this.init()
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    sendEmailCode() {
      this.emailForm.email = this.userForm.email
      this.emailForm.userId = this.userForm.id
      sendEmailCodeToUpdateEmail(this.emailForm).then(res => {
        console.log(res)
        if (res.code === 200) {
          this.$message.success("验证码发送成功");
          this.emailCodeInput = false
          this.updatePwdCheck = true
          this.timeDown()
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    sendNewEmailCode() {
      sendNewEmailCodeToCheckEmail(this.newEmailForm).then(res => {
        if (res.code === 200) {
          this.$message.success("验证码发送成功");
          this.newEmailCodeInput = false
          this.newEmailInput = true
          this.newTimeDown()
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    nextStep() {
      checkCode(this.emailForm).then(res => {
        if (res.code === 200) {

          this.dialogFormVisibleEmail = false
          this.dialogFormVisibleEmailSuccess = true
          this.$message({
            message: '账户验证成功',
            type: 'success'
          })
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    getSexNum() {
      // console.log(this.userForm.sex)
    },
    init() {
      this.newCount = 0
      this.count = 0
      this.emailCodeInput = true
      this.fileList = []
      this.isUpdate = true
      this.updateBtn = '修改信息'
      var userInfo = JSON.parse(localStorage.getItem("userInfo"));
      getUserInfo(userInfo.id).then(res => {
        console.log(res);
        this.userForm = res.data;
        let sexNum = res.data.sex
        if (sexNum === '0') {
          this.userForm.sex = '男'
        } else if (sexNum === '1') {
          this.userForm.sex = '女';
        } else {
          this.userForm.sex = '未知'
        }
      });

    },
    updateBtnInfo() {
      if (this.isUpdate === true) {
        this.isUpdate = false;
        this.updateBtn = '保存';
      } else {
        let item = localStorage.getItem("userInfo");
        var userInfo = JSON.parse(item);
        if (userInfo) {
          if (this.userForm.sex === '男') {
            this.userForm.sex = '0'
          } else if (this.userForm.sex === '女') {
            this.userForm.sex = '1'
          } else if (this.userForm.sex === '未知') {
            this.userForm.sex = '2'
          }
          updateUserInfo(this.userForm).then(res => {
            console.log(res);
            this.$message({
              message: '个人信息更新成功',
              type: 'success'
            })
          });
          setTimeout(() => {
            this.init()
            getUserById(userInfo.id).then(res => {
              this.$refs.sub1.userInfo.userName = res.data.userName
              this.$refs.sub1.userInfo.avatar = res.data.avatar
            })
          }, 300)
        } else {
          this.$message.error('请先登录');
          this.$router.push("/login")
        }
      }
    },
    handleUpload(img) {
      uploadImg(img.file).then(response => {
        console.log(response)
        this.userForm.avatar = response.data
        this.fileList.push({name: img.file.name, url: response.data})

        var userInfo = JSON.parse(localStorage.getItem("userInfo"));
        if (!userInfo) {
          this.$message.error('请先登录')
          this.$router.push("/login")
          return;
        }
        //更新用户头像
        updateUserAvatar(userInfo.id, response.data).then(res => {
          this.$message.success('头像修改成功')
        });
        setTimeout(() => {
          getUserById(userInfo.id).then(res => {
            this.$refs.sub1.userInfo.avatar = res.data.avatar
          })
          this.init()
        }, 300)
      }).catch(error => {
        this.$message.error('头像修改失败')
      })
    },
    updatePwd() {
      var userInfo = JSON.parse(localStorage.getItem("userInfo"));
      if (!userInfo) {
        this.$message.error("请先登陆")
        this.$router.push("/login")
        return;
      }

      this.pwdForm.userId = userInfo.id;
      updateUserPassword(this.pwdForm).then(res => {
        console.log('-----------');
        console.log(res);
        debugger
        if (res.code === 200) {
          this.$message({
            message: '密码修改成功',
            type: "success"
          });
          this.dialogFormVisible = false;
        } else {
          this.$message.error(res.msg)
        }
      })

    },
    timeDown() {
      this.emailTimeDown = false;//倒计时
      this.count = 60; //赋值3秒
      let times = setInterval(() => {
        this.count--; //递减
        if (this.count <= 0) {
          // <=0 变成获取按钮
          this.emailTimeDown = true;
          clearInterval(times);
        }
      }, 1000); //1000毫秒后执行
    },
    newTimeDown() {
      this.newEmailTimeDown = false;//倒计时
      this.newCount = 60; //赋值3秒
      let times = setInterval(() => {
        this.newCount--; //递减
        if (this.newCount <= 0) {
          // <=0 变成获取按钮
          this.newEmailTimeDown = true;
          clearInterval(times);
        }
      }, 1000); //1000毫秒后执行
    },

  },


}
</script>

<style scoped>
.btm ul li span {
  font-weight: bold;
  font-size: 18px;
}

.btm ul li {
  float: left;
  margin-top: 10px;
  margin-right: 20px;
  cursor: pointer;
}

.btm {
  margin-top: 30px;
  width: 850px;
  height: 200px;
  /*border: 1px solid red;*/
  float: left;
}

.forgetPwd:hover {
  color: #2d915f;
  cursor: pointer;
  text-decoration: underline;
}

.contact p {
  font-size: 10px;
}

.box-card1 .left {
  width: 300px;
  height: 460px;
  float: left;
}

.box-card1 .right {
  width: 555px;
  height: 460px;
  border-left: 1px solid #6a737d;
  float: left;
}

.box-card1 {
  width: 900px;
  height: 550px;
  margin: 50px auto;
  float: left;
}

.personal .card {
  width: 900px;
  height: 500px;
  margin: 0 auto;
}
</style>
