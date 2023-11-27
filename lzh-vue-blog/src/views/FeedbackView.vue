<template>
  <div class="feedbackview">
    <div slot="title" class="dialog-footer">
      <div style="font-size:16px">
        问题反馈
      </div>
      <div class="separateline">
      </div>
    </div>
    <div style="margin-top:-10px">
      <div style="display:flex">
        <div style="width:78px;color:#000">
          问题类型:
        </div>
        <div>
          <el-radio v-model="type" label="1">系统问题</el-radio>
          <el-radio v-model="type" label="2">内容问题</el-radio>
          <el-radio v-model="type" label="3">我要吐槽</el-radio>
        </div>
      </div>
      <div style="display:flex;margin-top: 30px;">
        <div style="width:78px;color:#000;">
          描&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;述:
        </div>
        <div style="flex-grow: 1">
          <el-input type="textarea" v-model="describe" style="width:100%" :rows="7" placeholder="请输入内容">
          </el-input>
        </div>
      </div>
<!--      <div style="display:flex;margin-top: 30px;">-->
<!--        <div style="width:78px;color:#000">-->
<!--          问题截图:-->
<!--        </div>-->
<!--        <div style="flex-grow: 1">-->
<!--          &lt;!&ndash; 引用图片上传的组件 &ndash;&gt;-->
<!--          <UpLoadImg/>-->
<!--        </div>-->
<!--      </div>-->
    </div>
    <span slot="footer" class="dialog-footer">
                    <el-button @click="dialogVisible = false" size="small">我在想想</el-button>
                    <el-button type="primary" size="small" @click="dialogVisible = false">确定反馈</el-button>
                </span>

  </div>
</template>

<script>

export default {
  components: {},
  data() {
    return {
      dialogVisible: false,
      describe: '',
      type: -1
    }
  }
}
</script>


<style scoped>

</style>