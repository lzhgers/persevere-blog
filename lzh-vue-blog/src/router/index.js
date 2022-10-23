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
  }

]

const router = new VueRouter({
  // mode: 'history',
  mode: 'hash',
  base: process.env.BASE_URL,
  routes
})

export default router
