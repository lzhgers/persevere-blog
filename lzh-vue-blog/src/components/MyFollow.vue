<template>
  <div class="mycollect" style="height: 870px;border-radius:10px">
    <el-card v-for="follow in allData" class="box-card" shadow="hover"
             style="border-radius: 10px;margin-bottom: 9px;cursor:pointer;">
      <div class="follow_left">
        <img class="follow_img" :src="follow.avatar" style="width: 83px;margin-bottom: 5px;border-radius: 50%"/>
      </div>
      <div class="user_desc">
        <h3>{{ follow.nickName }}</h3>
        <h5>{{ follow.remark }}</h5>
      </div>
      <div class="follow_botton">
        <el-button style="float:right;margin-top: 15px;width: 96px" v-if="follow.isFocusEach === 1"
                   @click="nofollowUser(follow.id)"
                   type="warning"
                   size="small"
                   round
                   icon="el-icon-check"
        >已关注
        </el-button>
        <el-button style="float:right;margin-top: 15px" v-if="follow.isFocusEach === 0"
                   @click="nofollowUser(follow.id)"
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
import {pageUserSubscribed} from "@/api/subscribe";
import {noSubscribe} from "@/api/subscribe";

export default {
  name: "myfollow",
  data() {
    return {
      allData: [],
      item: {
        avatar: "http://rir6wdlzn.hd-bkt.clouddn.com/2022/10/14/a64d2c310c0e41a9b0d2b59f253d1c18.jpg"
      },
      pageNum: 1,
      pageSize: 7,
      total: 0,

      userId: -1,

    };
  },
  created() {
    let userInfo = JSON.parse(localStorage.getItem("userInfo"));
    if (!userInfo) {
      this.$message.info('请先登陆')
      this.$router.push('/login')
    }
    this.userId = userInfo.id;

    pageUserSubscribed(userInfo.id, this.pageNum, this.pageSize).then(res => {
      console.log(res)
      this.allData = res.data.rows
      this.total = parseInt(res.data.total)
    })
  },
  methods: {
    handleSizeChange(pageSize) {
      this.pageSize = pageSize;
      pageUserSubscribed(this.userId, this.pageNum, this.pageSize).then(res => {
        this.allData = res.data.rows
        this.total = parseInt(res.data.total)
      })
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum;
      pageUserSubscribed(this.userId, this.pageNum, this.pageSize).then(res => {
        this.allData = res.data.rows
        this.total = parseInt(res.data.total)
      })
    },
    nofollowUser(userId) {
      this.$confirm('是否要取关该用户?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        noSubscribe(this.userId, userId).then(res => {
        })

        if (this.pageNum === 1) {
          this.pageNum = 1
          location.reload()
          return;
        }
        pageUserSubscribed(this.userId, this.pageNum, this.pageSize).then(res => {
          this.allData = res.data.rows
          this.total = parseInt(res.data.total)
          if (this.total <= 8) {
            this.pageNum = 1;
            pageUserSubscribed(this.userId, this.pageNum, this.pageSize).then(res => {
              this.allData = res.data.rows
              this.total = parseInt(res.data.total)
            });
          } else {
            if ((this.total - 1) % 7 === 0) {
              this.pageNum -= 1;
              pageUserSubscribed(this.userId, this.pageNum, this.pageSize).then(res => {
                this.allData = res.data.rows
                this.total = parseInt(res.data.total)
              });
            } else {
              pageUserSubscribed(this.userId, this.pageNum, this.pageSize).then(res => {
                this.allData = res.data.rows
                this.total = parseInt(res.data.total)
              });
            }
          }
        })
      });
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
