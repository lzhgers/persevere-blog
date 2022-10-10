<template>
  <div class="aboutme">
    <body>
    <div class="about">
      <h3 class="about_tit">要不断的努力，才能成为更好的自己</h3>
      <img class="about_bgc" src="../assets/img/haixing.jpeg" alt="">
      <div class='ribbon'>
        <router-link to="/home"><span>Home</span></router-link>
<!--        <router-link to="/home"><span>Form</span></router-link>-->
        <router-link to="/aboutMe"><span>About</span></router-link>

      </div>
      <div class="me" style="margin-bottom: -90px">
        <img class="me_img" src="../assets/bcimg/bc1.jpeg" style="margin-left: 20px">
        <span class="me_tit animate" style="margin-left: 10px;margin-right: 20px;color: #2b50e1">我是一名大二的学生，平时爱好看看书，浏览网页资料，学习一下新知识，为以后毕业做些小打算。跟大多数人一样，对未来些许迷茫，当忽然之间脑海里闪现出来一些关于几年前的某个片段时，会情不自禁的回想那个时候的自己，关于那个曾经的所有点点滴滴也渐渐浮现出一些画面，突然拼命的想要去找一点在那个记忆点里的某些东西，寻找的过程中脑海里的思绪早已沉溺在回忆里面，忘了的还有那些忘不了的感触在这一刻全都涌了出来，我觉得最后寻找的结果或许已不重要了，因为这突如其来想要寻找的一些东西的念头，让自己突然变得好安静，莫名的思绪蔓延开来了，它又带我回到了现在已经体会不到的那种怦然心动的时候，仿佛打开了老旧的抽屉，里面装满了当初的成长，青涩，遗憾与那一种想起来会不禁忽然笑出来的感觉，可能想笑的是那时的自己，又或许是笑那带有遗憾结尾的时光，怀念真好。
          又一年即将过去了，生活一如既往的推着自己往前走，在行人眼里穿梭，而不时会回头看看的，还是那个仿佛不知在追逐着什么的自己，此刻的空气很好，它不会留下痕迹。
        </span>
      </div>
      <div class="videos">
            <span>
              这是我很喜欢的一个动漫电影，玩具总动员里面的第四部，讲述的事胡迪深知自己在这个世界上的使命，就是照顾他的主人，无论是原来的安迪还是新主人邦妮。当邦妮将不情愿成为玩具的“叉叉”带回家时，胡迪又担起了教导叉叉接受自己新身份的责任。 然而当邦妮将所有玩具带上房车家庭旅行时，胡迪与伙伴们将共同踏上全新的冒险之旅，领略房间外面的世界有多广阔，甚至偶遇老朋友牧羊女。在多年的独自闯荡中，牧羊女已经变得热爱冒险，不再只是一个精致的洋娃娃。正当胡迪和牧羊女发现彼此对玩具的使命的意义大相径庭时，他们很快意识到更大的威胁即将到来。
            </span>
        <video width="800" controls id="video" preload="auto" height="100%" autoplay="autoplay" loop="loop" src="../assets/video/city-night.mp4"
               type="video/mp4">
        </video>
      </div>
    </div>
    </body>

  </div>
</template>

<script>
export default {
  name: "AboutMe"
}
</script>

<style scoped>
* {
  margin: 0;
  padding: 0;
}

.about {
  width: 100%;
  height: 100%;
  overflow: hidden;
  background-color: #bfd5ec;
}

.about_bgc {
  width: 100%
}

.about_tit {
  position: absolute;
  top: 280px;
  left: 0;
  right: 0;
  font-size: 28px;
  font-weight: 550;
  bottom: 0;
  width: 800px;
  margin: auto;
  text-align: center;
}

.ribbon {
  display: flex;
  justify-content: center;
  position: absolute;
  right: 300px;
  top: 50px;
  margin: auto;
}

.ribbon:after,
.ribbon:before {
  margin-top: 0.5em;
  content: "";
  display: flex;
;
  border: 1.5em solid #fff;
}

.ribbon:after {
  border-right-color: transparent;
}

.ribbon:before {
  border-left-color: transparent;
}

.ribbon a:link,
.ribbon a:visited {
  color: #000;
  text-decoration: none;
  height: 3.5em;
  overflow: hidden;
}

.ribbon span {
  background: #fff;
  display: inline-block;
  line-height: 3em;
  padding: 0 1.5em;
  margin-top: 0.5em;
  position: relative;
  -webkit-transition: background-color 0.2s, margin-top 0.2s;
  /* Saf3.2+, Chrome */
  -moz-transition: background-color 0.2s, margin-top 0.2s;
  /* FF4+ */
  -ms-transition: background-color 0.2s, margin-top 0.2s;
  /* IE10 */
  -o-transition: background-color 0.2s, margin-top 0.2s;
  /* Opera 10.5+ */
  transition: background-color 0.2s, margin-top 0.2s;
}

.ribbon a:hover span {
  background: #FFD204;
  margin-top: 0;
}

.ribbon span:before {
  content: "";
  position: absolute;
  top: 3em;
  left: 0;
  border-right: 0.5em solid #9B8651;
  border-bottom: 0.5em solid #fff;
}

.ribbon span:after {
  content: "";
  position: absolute;
  top: 3em;
  right: 0;
  border-left: 0.5em solid #9B8651;
  border-bottom: 0.5em solid #fff;
}

.me {
  display: flex;
  justify-content: center;
  margin-top: 60px;
  overflow: hidden;
}

.me_tit {
  width: 600px;
  line-height: 36px;
  font-size: 18px;
  margin-left: 100px;
  margin-top: 50px;
}

.me_img {
  width: 800px;
  height: 600px;
}

.animate {
  padding-left: 20px;
  width: 500px;
  font-size: 16px;
  color: #000;
  animation: 10s wordsLoop linear infinite normal;
}

@keyframes wordsLoop {
  0% {
    transform: translateY(100px);
    -webkit-transform: translateY(100px);
  }
  100% {
    transform: translateY(-100%);
    -webkit-transform: translateY(-100%);
  }
}

.videos {
  display: flex;
  margin-top: 10px;
  margin-right: 20px;
  padding-bottom: 80px;
}

.videos span {
  width: 800px;
  margin-right: 20px;
  margin-left: 20px;
  color: #2b50e1
}

</style>
