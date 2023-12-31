import Vue from 'vue'
import VueRouter from 'vue-router'
import HomeView from '../views/HomeView.vue'
import BasicLayout from "@/layouts/BasicLayout.vue";
import LoginView from "@/views/LoginView.vue";

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    redirect: '/home'
  },
  {
    path: '/layout',
    name: 'layout',
    component: BasicLayout
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
    component: () => import(/* webpackChunkName: "login" */ '../views/LoginView.vue')
  },
  {
    path: '/regist',
    name: 'regist',
    component: () => import(/* webpackChunkName: "regist" */ '../views/RegisterView.vue')
  },
  {
    path: '/article/detail/:id',
    name: 'articleDetail',
    component: () => import(/* webpackChunkName: "articleDetail" */ '../components/ArticleDetail')
  },
  {
    path: '/aboutMe',
    name: 'aboutMe',
    component: () => import(/* webpackChunkName: "aboutMe" */ '../components/AboutMe')
  },
  {
    path: '/personalCenter',
    name: 'personalCenter',
    component: () => import(/* webpackChunkName: "personalCenter" */ '../views/PersonalCenter')
  },
  {
    path: '/search',
    name: 'search',
    component: () => import(/* webpackChunkName: "search" */ '../components/Search')
  },
  {
    path: '/draft/:id',
    name: 'draft',
    component: () => import(/* webpackChunkName: "draft" */ '../views/DraftView')
  },
  {
    path: '/category',
    name: 'category',
    component: () => import(/* webpackChunkName: "category" */ '../views/CategoryView')
  },
  {
    path: '/sort',
    name: 'sort',
    component: () => import(/* webpackChunkName: "sort" */ '../views/SortView')
  },
  {
    path: '/cancelaccount',
    name: 'cancelaccount',
    component: () => import(/* webpackChunkName: "cancelaccount" */ '../views/CancelAccount')
  },
  {
    path: '/publishview/:id',
    name: 'publishview',
    component: () => import(/* webpackChunkName: "publishview" */ '../views/PublishView')
  },
  {
    path: '/forgetPassword',
    name: 'forgetPassword',
    component: () => import(/* webpackChunkName: "forgetPassword" */ '../components/ForgetPassword')
  },
  {
    path: '/chat',
    name: 'chat',
    component: () => import(/* webpackChunkName: "chat" */ '../components/Chat')
  },
  {
    path: '/friendlink',
    name: 'friendlink',
    component: () => import(/* webpackChunkName: "friendlink" */ '../views/FriendLink')
  },
  {
    path: '/myBlog',
    name: 'myBlog',
    component: () => import(/* webpackChunkName: "myBlog" */ '../components/MyBlog'),
    children: [
      {
        path: '/myBlog/myinitblog',
        name: 'myinitblog',
        component: r => require.ensure([], () => r(require('@/views/MyInitBlog')), 'myinitblog')
      },
      {
        path: '/myBlog/myrough',
        name: 'myrough',
        component: r => require.ensure([], () => r(require('@/components/MyRough')), 'myrough')
      },
      {
        path: '/myBlog/mycollect',
        name: 'mycollect',
        component: r => require.ensure([], () => r(require('@/components/MyCollect')), 'mycollect')
      },
      {
        path: '/myBlog/mylike',
        name: 'mylike',
        component: r => require.ensure([], () => r(require('@/components/MyLike')), 'mylike')
      },
      {
        path: '/myBlog/mypublish',
        name: 'mypublish',
        component: r => require.ensure([], () => r(require('@/components/MyPublish')), 'mypublish')
      },
      {
        path: '/myBlog/myfollow',
        name: 'myfollow',
        component: r => require.ensure([], () => r(require('@/components/MyFollow')), 'myfollow')
      },
      {
        path: '/myBlog/myfan',
        name: 'myfan',
        component: r => require.ensure([], () => r(require('@/components/MyFan')), 'myfan')
      }
    ]
  },
  {
    path: '/im',
    name: 'Im',
    component: () => import(/* webpackChunkName: "im" */ '../components/Im')
  },

]

const router = new VueRouter({
  // mode: 'history',
  mode: 'hash',
  base: process.env.BASE_URL,
  routes
})

export default router
