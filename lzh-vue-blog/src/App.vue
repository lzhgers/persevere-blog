<template>
  <div id="app">
    <router-view v-if="isRouterAlive"/>
  </div>
</template>

<script>


export default {
  provide() {
    return {
      reload: this.reload //提供数据
    }
  },
  data() {
    return {
      isRouterAlive: true,
    }
  },
  methods: {
    reload() {
      this.isRouterAlive = false
      this.$nextTick(() => {
        this.isRouterAlive = true
        console.log("确实刷新了")
      })
    }
  },
}

</script>

<style lang="less">
#app {
  padding: 0;
  margin: 0;
  background-color: #c4e1bb;
}

li {
  list-style: none;
}

</style>
