<template>
  <div>
    <Header></Header>
    <MyBlog></MyBlog>
    <Footer></Footer>
  </div>
</template>

<script>
import MyBlog from "@/components/MyBlog";
import Header from "@/components/Header";
import Footer from "@/components/Footer";

export default {
  name: "MyBlogView",
  components: {
    MyBlog,
    Header,
    Footer
  }
}
</script>

<style scoped>

</style>
