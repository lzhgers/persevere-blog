import Vue from 'vue'
import VueRouter from 'vue-router'
import HomeView from '../views/HomeView.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    redirect: '/home'
  },
  {
    path: '/home',
    name: 'home',
    component: HomeView
  },
  {
    path: '/about',
    name: 'about',
    component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
  },
  {
    path: '/article/write',
    name: 'write',
    component: () => import(/* webpackChunkName: "write" */ '../components/Write')
  },
  {
    path: '/login',
    name: 'login',
    component: () => import(/* webpackChunkName: "login" */ '../components/Login')
  },
  {
    path: '/regist',
    name: 'regist',
    component: () => import(/* webpackChunkName: "regist" */ '../components/Regist')
  },
  {
    path: '/article/detail/:id',
    name: 'articleDetail',
    component: () => import(/* webpackChunkName: "articleDetail" */ '../components/ArticleDetail')
  },
  {
    path: '/aboutMe',
    name: 'aboutMe',
    component: () => import(/* webpackChunkName: "about" */ '../components/AboutMe')
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router