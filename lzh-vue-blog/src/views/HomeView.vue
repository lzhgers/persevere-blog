<template>
  <div class="home">
    <Header></Header>
    <Content></Content>
    <Footer></Footer>

  </div>
</template>

<script>
import Header from "@/components/Header";
import Content from "@/components/Content";
import Footer from "@/components/Footer";


export default {
  name: 'HomeView',
  data() {
    return {
      src: '../assets/img/emot/image/aini.gif'
    }
  },
  components: {
    Header,
    Content,
    Footer,
  }
}
</script>

<style>
* {
  padding: 0;
  margin: 0;
}
.home {
  background-color: #eab9b9;
  width: 100%;
  min-height: 100%;
  max-height: 100%;
  /*height: 1300px;*/
}

</style>
