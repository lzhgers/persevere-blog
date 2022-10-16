<template>
  <div>
    <Header></Header>
    <Sort></Sort>
    <Footer></Footer>
  </div>
</template>

<script>
import Header from "@/components/Header";
import Footer from "@/components/Footer";
import Sort from "@/components/Sort";

export default {
  name: "SortView",
  components: {
    Header,
    Footer,
    Sort
  }
}
</script>

<style scoped>

</style>
