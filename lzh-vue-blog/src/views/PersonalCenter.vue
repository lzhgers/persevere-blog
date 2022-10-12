<template>
  <div class="personal">
    <Header></Header>
    <div class="card">
      <el-card class="box-card1" shadow="hover">
        <!--        <div slot="header" class="clearfix"></div>-->
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
            <!--            <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>-->
          </el-upload>


          <div style="margin: 20px 0 10px 0;font-weight: lighter">—————— <span style="font-weight: normal">联系方式</span>
            ——————
          </div>
          <div style="margin-top: 10px;text-align: right;margin-right: 10px" class="contact">
            <p style="margin: 15px 0 5px -50px"><span style="margin-right: 80px">手机 ：{{ userForm.phonenumber }}</span>
              <el-tag type="success"><i class="el-icon-mobile-phone"></i>修改手机</el-tag>
            </p>
            <p><span style="margin-right: 32px">邮箱 ：{{ userForm.email }}</span>
              <el-tag><i class="el-icon-edit-outline"></i>修改邮箱</el-tag>
            </p>
          </div>
        </div>
        <div class="right" style="text-align: center">
          <div class="info" style="margin: 10px 0;font-weight: lighter">———————————— <span style="font-weight: normal">个人信息</span>
            ————————————
          </div>
          <el-form label-width="80px" style="width: 500px">
            <el-form-item label="用户名">
              <el-input v-model="userForm.userName"></el-input>
            </el-form-item>
            <el-form-item label="昵称">
              <el-input v-model="userForm.nickName"></el-input>
            </el-form-item>
            <el-form-item label="地址">
              <el-input v-model="userForm.address"></el-input>
            </el-form-item>
            <el-form-item label="性别" style="">
              <el-select v-model="userForm.sex" placeholder="请选择性别" style="width: 420px">
                <el-option label="男" value="0"></el-option>
                <el-option label="女" value="1"></el-option>
                <el-option label="未知" value="2"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="备注">
              <el-input v-model="userForm.remark" type="textarea"></el-input>
            </el-form-item>
            <el-button style="width: 470px;margin-left: 35px;margin-bottom: 10px" type="primary">修改信息</el-button>
            <el-button style="width: 470px;margin-left: 35px" type="danger">修改密码</el-button>
          </el-form>
        </div>
      </el-card>
    </div>

    <Footer></Footer>
  </div>
</template>

<script>
import Header from "@/components/Header";
import Footer from "@/components/Footer";

import {getUserInfo} from "@/api/personal";
import {uploadImg} from "@/api/upload";
import {updateUserAvatar} from "@/api/personal";

export default {
  name: "PersonalCenter",
  components: {
    Header,
    Footer
  },
  data() {
    return {
      userForm: {},
      fileList: [],
    }
  },
  created() {
    this.fileList = []
    this.init()
  },
  methods: {
    init() {
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
      })
    },
    handleUpload(img) {
      uploadImg(img.file).then(response => {
        console.log(response)
        this.userForm.avatar = response.data
        this.fileList.push({name: img.file.name, url: response.data})

        var userInfo = JSON.parse(localStorage.getItem("userInfo"));
        //更新用户头像
        updateUserAvatar(userInfo.id, response.data).then(res => {
          this.$message.success('头像修改成功')
        })
        setTimeout(() => {
          location.reload()
        }, 1000)
      }).catch(error => {
        this.$message.error('头像修改失败')
      })
    }
  }

}
</script>

<style scoped>
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
  height: 500px;
  margin: 50px auto;
  float: left;
}

.personal .card {
  width: 900px;
  height: 500px;
  margin: 0 auto;
}
</style>
