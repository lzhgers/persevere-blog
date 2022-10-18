<template>
  <div class="sort">
    <div class="radio" style="margin-bottom: 30px">
      <span style="color: #fc0303">排序：</span>
      <el-radio-group v-model="reverse">
        <el-radio style="color: #b31d28" :label="true">倒序</el-radio>
        <el-radio style="color: #b31d28" :label="false">正序</el-radio>
      </el-radio-group>
    </div>
    <div class="sortOpt">
      <el-timeline :reverse="reverse">
        <el-timeline-item
            color="red"
            v-for="(date, index) in dates"
            :key="index">
          <span :style="date === selectedDate ? selectedStyle : ''"
                @click="listArticleByDate(date)" class="categoryName">{{ date }}</span>
        </el-timeline-item>
      </el-timeline>
    </div>
    <el-timeline class="timeLine">
      <el-timeline-item color="red" :timestamp="article.createTime" placement="top" v-for="article in articles">
        <el-card class="sortedArticle">
          <h4 style="margin-bottom: 20px" @click="getDetailArticle(article.id)">{{ article.title }}</h4>
          <el-tag :type="tagTypes[index]" style="margin-right: 10px;cursor:pointer;"
                  v-for="(tag, index) in article.tags"
                  @click="selectArticleByTagId(tag.id)">
            {{ tag.name }}
          </el-tag>
        </el-card>
      </el-timeline-item>

    </el-timeline>
  </div>
</template>

<script>
import {listDiffDate} from "@/api/sort";
import {listArticleByDate} from "@/api/sort";

export default {
  name: "CategoryView",
  data() {
    return {
      dates: [],
      articles: [],
      tagTypes: ["danger", "success", "warning", "", "danger", "info", "warning", "danger", "", "success", "info", "warning", "danger", "", "success", "info", "warning", "danger", "", "success", "info", "warning", "danger", "", "success", "info", "warning", "danger", ""],
      firstFlag: true,
      selectedStyle: '',
      selectedDate: '',
      reverse: false
    }
  },
  created() {
    this.init()
    listDiffDate().then(res => {
      if (this.firstFlag) {
        this.selectedDate = res.data[0]
        listArticleByDate(this.selectedDate).then(res => {
          this.articles = res.data;
        })
        this.selectedStyle = 'color: red';
        this.firstFlag = false;
      }
      this.dates = res.data;
    })
  },
  methods: {
    selectArticleByTagId(tagId) {
      this.$router.push('/search?tagId=' + tagId)
    },
    getDetailArticle(id) {
      let routeData = this.$router.resolve({path: '/article/detail/' + id, query: {id: 1}});
      window.open(routeData.href, '_blank');
    },
    listArticleByDate(date) {
      listArticleByDate(date).then(res => {
        this.selectedDate = date
        this.articles = res.data
        console.log(res.data)
      })
    },
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
.sortedArticle h4 {
  cursor: pointer;
}

.sortedArticle h4:hover {
  color: red;
}

.sort {
  width: 1200px;
  margin: 10px auto;
}

.sort .categoryName:hover {
  color: #f80231;
  cursor: pointer;
}

.sort .categoryName {
  color: #a64343;
  cursor: pointer;
}

.sort .sortOpt {
  width: 200px;
  height: 600px;
  float: left;
  margin-left: 60px;
  margin-right: 20px;
  overflow-y: scroll;
}

.sort .timeLine {
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
