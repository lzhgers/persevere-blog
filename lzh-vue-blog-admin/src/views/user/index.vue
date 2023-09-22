<template>
  <div style="margin: 20px">
    <el-row :gutter="20">
      <el-col :span="8" :xs="24">
        <el-card class="box-card" style="margin: 5px">
          <div slot="header" class="clearfix">
            <span>个人信息</span>
          </div>
          <div>
            <div class="text-center">
              <UserAvatar :user="user"/>
            </div>
            <ul class="list-group list-group-striped">
              <li class="list-group-item">
                <svg-icon icon-class="user"/>
                用户名称
                <div class="pull-right">{{ user.userName }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="phone"/>
                手机号码
                <div class="pull-right">{{ user.phonenumber }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="email"/>
                用户邮箱
                <div class="pull-right">{{ user.email }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="radio"/>
                用户性别
                <div class="pull-right">{{ user.sex === '0' ? '男' : '女' }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="peoples"/>
                所属角色
                <div class="pull-right">{{ user.role }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="date"/>
                创建日期
                <div class="pull-right">{{ user.createTime }}</div>
              </li>
            </ul>
          </div>
        </el-card>
      </el-col>
      <el-col :span="16" :xs="24">
        <el-card style="margin: 5px">
          <div slot="header" class="clearfix">
            <span>基本资料</span>
          </div>
          <el-tabs v-model="activeTab">
            <el-tab-pane label="基本资料" name="userinfo">
              <userInfo :user="user"/>
            </el-tab-pane>
            <el-tab-pane label="修改密码" name="resetPwd">
              <ResetPwd :user="user"/>
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import UserInfo from "@/views/user/profile/userInfo.vue";
import UserAvatar from "@/views/user/profile/userAvatar.vue";
import ResetPwd from "@/views/user/profile/resetPwd.vue";
import {getUserProfile} from "@/api/user";

export default {
  name: "UserView",
  components: {UserAvatar, UserInfo, ResetPwd},
  data() {
    return {
      user: {},
      roleGroup: {},
      postGroup: {},
      activeTab: "userinfo"
    };
  },
  created() {
    this.getUser();
  },
  methods: {
    getUser() {
      getUserProfile().then(response => {
        this.user = response.data;
      });
    }
  }
};
</script>


<style scoped>

</style>