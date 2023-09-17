<template>
  <div class="basicLayout">
    <el-container style="height: 100%;position: absolute;width: 100%" direction="vertical">
      <el-header>
        <Header></Header>
      </el-header>
      <el-container>
        <el-main>
          <Sort></Sort>
        </el-main>
        <el-footer style="height: 11vh">
          <Footer></Footer>
        </el-footer>
      </el-container>
    </el-container>
  </div>
</template>

<script setup lang="ts">

import Footer from "../components/Footer.vue";
import Header from "../components/Header.vue";
import Category from "../components/Category.vue";
import Sort from "../components/Sort.vue";
</script>

<style scoped>
.basicLayout {
  padding: 0;
  margin: 0;
  height: 100%;
}

.el-header {
  background-color: #e41b14;
}

.el-footer {
  background-color: #e41b14;
}

.el-main {
  background-color: #f6d9d9;
}
</style>
