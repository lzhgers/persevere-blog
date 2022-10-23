<template>
  <div class="mycollect" style="height: 870px;border-radius:10px">
    <el-card v-for="follow in allData" class="box-card" shadow="hover"
             style="border-radius: 10px;margin-bottom: 10px;cursor:pointer;">
      <div class="follow_left">
        <img class="follow_img" :src="item.avatar" style="width: 86px"/>
      </div>
      <div class="user_desc">
        <h3>声声慢</h3>
        <h5>自信 专注</h5>
      </div>
      <div class="follow_botton">
        <el-button style="float:left;margin-left: 400px;margin-top: 15px" v-if="false"
                   @click="followUser(item.id)"
                   type="primary"
                   size="small"
                   round
                   icon="el-icon-check"
        >已关注
        </el-button>
        <el-button style="float:left;margin-left: 400px;margin-top: 15px" v-if="true"
                   @click="followUser(item.id)"
                   type="primary"
                   size="small"
                   round
                   icon="el-icon-check"
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


export default {
  name: "myfollow",
  data() {
    return {
      allData: [1, 2, 3, 4, 5, 6, 7],
      item: {
        avatar: "http://rir6wdlzn.hd-bkt.clouddn.com/2022/10/14/a64d2c310c0e41a9b0d2b59f253d1c18.jpg"
      },
      pageNum: 1,
      pageSize: 7,
      total: 0
    };
  },
  created() {

  },
  methods: {
    handleSizeChange(pageSize) {

    },
    handleCurrentChange(pageNum) {

    },
    followUser(userId) {

    }

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
