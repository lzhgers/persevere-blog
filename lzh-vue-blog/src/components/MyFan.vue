<template>
  <div class="mycollect" style="height: 870px;border-radius:10px">
    <el-card v-for="fan in allData" class="box-card" shadow="hover"
             style="border-radius: 10px;margin-bottom: 10px;cursor:pointer;">
      <div class="follow_left">
        <img class="follow_img" :src="fan.avatar" style="width: 83px;margin-bottom: 5px;border-radius: 50%"/>
      </div>
      <div class="user_desc">
        <h3>{{ fan.nickName }}</h3>
        <h5>{{ fan.remark }}</h5>
      </div>
      <div class="fan_botton">
        <el-button style="float:right;margin-top: 15px;width: 96px" v-if="fan.isFocusEach === 1"
                   @click="subscribeFan(fan.id)"
                   type="warning"
                   size="small"
                   round
                   icon="el-icon-plus"
        >回关
        </el-button>
        <el-button style="float:right;margin-top: 15px;width: 96px" v-if="fan.isFocusEach === 0"
                   @click="noSubscribeFan(fan.id)"
                   type="danger"
                   size="small"
                   round
                   icon="el-icon-s-promotion"
        >互相关注
        </el-button>
      </div>

    </el-card>

    <el-pagination v-if="allData.length !== 0"
                   @size-change="handleSizeChange"
                   @current-change="handleCurrentChange"
                   :current-page.sync="pageNum"
                   :page-size="pageSize"
                   layout="total, prev, pager, next, jumper"
                   :total="total">
    </el-pagination>

    <el-empty
        v-if="allData.length == 0"
        :image-size="250"
        description="暂未关注任何人额"
    ></el-empty>

  </div>
</template>

<script>
import {pageUserFan} from "@/api/subscribe";
import {subscribeFan} from "@/api/subscribe";
import {noSubEach} from "@/api/subscribe";

export default {
  name: "myfan",
  data() {
    return {
      allData: [],

      pageNum: 1,
      pageSize: 7,
      total: 0,

      userId: -1,
    };
  },
  created() {
    let userInfo = JSON.parse(localStorage.getItem("userInfo"));
    if (!userInfo) {
      this.$message.warning('请先登录')
      this.$router.push('/login');
    }
    this.userId = userInfo.id;

    pageUserFan(this.userId, this.pageNum, this.pageSize).then(res => {
      this.allData = res.data.rows
      this.total = parseInt(res.data.total)
    })
  },
  methods: {
    subscribeFan(fanId) {
      subscribeFan(this.userId, fanId).then(res => {
      })
      pageUserFan(this.userId, this.pageNum, this.pageSize).then(res => {
        console.log(']]]]]]]]]]]]]]]]')
        console.log(res.data)
        console.log(']]]]]]]]]]]]]]]]')
        this.allData = res.data.rows
        this.total = parseInt(res.data.total)
      })
    },
    noSubscribeFan(fanId) {
      noSubEach(this.userId, fanId).then(res => {
      })
      pageUserFan(this.userId, this.pageNum, this.pageSize).then(res => {
        console.log(']]]]]]]]]]]]]]]]')
        console.log(res.data)
        console.log(']]]]]]]]]]]]]]]]')
        this.allData = res.data.rows
        this.total = parseInt(res.data.total)
      })
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize

    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum

    },


  }

}
</script>

<style scoped>
.follow_img {
  float: left;
}

.user_desc {
  margin-left: 15px;
  float: left;
}

.user_desc h3 {
  margin-top: 5px;
  font-weight: initial;
  font-size: 20px;
}

.user_desc h5 {
  font-weight: lighter;
  margin-top: 10px;
}

span {
  font-weight: lighter;
  font-size: 10px;
}
</style>
