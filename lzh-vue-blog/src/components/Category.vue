<template>
  <div class="category">
    <div class="categoryOpt">
      <el-timeline>
        <el-timeline-item
            v-for="(category, index) in categories"
            :key="index">
          <span :style="selectedCategoryId === category.id ? selectedStyle : ''"
                @click="listArticleByCategoryId(category.id)" class="categoryName">{{ category.name }}</span>
        </el-timeline-item>
      </el-timeline>
    </div>
    <el-timeline class="timeLine">
      <el-timeline-item :timestamp="article.createTime" placement="top" v-for="article in articles">
        <el-card>
          <h4 style="margin-bottom: 20px">{{ article.title }}</h4>
          <el-tag :type="tagTypes[index]" style="margin-right: 10px" v-for="(tagName, index) in article.tagNames">
            {{ tagName }}
          </el-tag>
        </el-card>
      </el-timeline-item>

    </el-timeline>
  </div>
</template>

<script>
import {listAllCategory} from "@/api/category";
import {getArticleByCategoryId} from "@/api/category";

export default {
  name: "CategoryView",
  data() {
    return {
      categories: [],
      articles: [],
      tagTypes: ["danger", "success", "warning", "", "danger", "info", "warning", "danger", "", "success", "info", "warning", "danger", "", "success", "info", "warning", "danger", "", "success", "info", "warning", "danger", "", "success", "info", "warning", "danger", ""],
      firstFlag: true,
      selectedStyle: '',
      selectedCategoryId: -1,

    }
  },
  created() {
    this.init()
    listAllCategory().then(res => {
      if (this.firstFlag) {
        this.selectedCategoryId = res.data[0].id
        this.listArticleByCategoryId(res.data[0].id)
        this.firstFlag = false
      }
      this.categories = res.data;
    })
  },
  methods: {
    init() {
      this.firstFlag = true
    },
    listArticleByCategoryId(categoryId) {
      this.selectedStyle = 'color: red'
      this.selectedCategoryId = categoryId
      getArticleByCategoryId(categoryId).then(res => {
        console.log(res.data)
        this.articles = res.data
      })
    },

  }
}
</script>

<style scoped>

.category {
  width: 1200px;
  margin: 10px auto;
}

.category .categoryName:hover {
  color: #f80231;
  cursor: pointer;
}

.category .categoryName {
  color: #a64343;
  cursor: pointer;
}

.category .categoryOpt {
  width: 200px;
  height: 600px;
  float: left;
  margin-left: 60px;
  margin-right: 20px;
  overflow-y: scroll;
}

.category .timeLine {
  width: 870px;
  height: 600px;
  float: left;
  overflow-y: scroll;
}


/* 整个滚动条 */
::-webkit-scrollbar {
  width: 10px;
  height: 10px;
}

/* 滚动条上的滚动滑块 */
::-webkit-scrollbar-thumb {
  background-color: #de4131;
  /* 关键代码 */
  background-image: -webkit-linear-gradient(45deg,
  rgba(255, 255, 255, 0.4) 25%,
  transparent 25%,
  transparent 50%,
  rgba(255, 255, 255, 0.4) 50%,
  rgba(255, 255, 255, 0.4) 75%,
  transparent 75%,
  transparent);
  border-radius: 32px;
}

/* 滚动条轨道 */
::-webkit-scrollbar-track {
  background-color: #f6d9d9;
  border-radius: 32px;
}

</style>
