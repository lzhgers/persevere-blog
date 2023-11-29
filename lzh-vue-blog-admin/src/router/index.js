import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
    {
        path: '/login',
        component: () => import('@/views/login/index'),
        hidden: true
    },
    {
        path: '/404',
        component: () => import('@/views/404'),
        hidden: true
    },

    {
        path: '/',
        component: Layout,
        redirect: '/dashboard',
        children: [{
            path: 'dashboard',
            name: 'Dashboard',
            component: () => import('@/views/dashboard/index'),
            meta: {title: '博客管理系统', icon: 'el-icon-s-home'}
        }]
    },

    {
        path: '/blog-manager',
        component: Layout,
        redirect: '/blog-manager/article-manager',
        name: 'blogManager',
        meta: {title: '博客管理', icon: 'el-icon-s-help'},
        children: [
            {
                path: 'article-manager',
                name: 'articleManager',
                component: () => import('@/views/blog-manager/article-manager'),
                meta: {title: '文章管理', icon: 'el-icon-edit-outline'}
            },
            {
                path: 'tag-manager',
                name: 'tag-manager',
                component: () => import('@/views/tag-manager/tag-manager'),
                meta: {title: '标签管理', icon: 'el-icon-collection-tag'}
            },
            {
                path: 'category-manager',
                name: 'category-manager',
                component: () => import('@/views/blog-manager/category-manager/category-manager'),
                meta: {title: '分类管理', icon: 'el-icon-collection'}
            },
            {
                path: 'friend-link',
                name: 'friend-link',
                component: () => import('@/views/blog-manager/friend-link/friend-link'),
                meta: {title: '友链管理', icon: 'el-icon-link'}
            },
            {
                path: 'carousel-img',
                name: 'carousel-img',
                component: () => import('@/views/blog-manager/carousel-img/carousel-img.vue'),
                meta: {title: '轮播图管理', icon: 'el-icon-files'}
            }
        ]
    },

    {
        path: '/message-manager',
        component: Layout,
        redirect: '/message-manager/comment-manager',
        name: 'message-manager',
        meta: {title: '消息管理', icon: 'el-icon-chat-line-round'},
        children: [
            {
                path: 'comment-manager',
                name: 'comment-manager',
                component: () => import('@/views/message-manager/comment-manager'),
                meta: {title: '评论管理', icon: 'el-icon-s-comment'}
            },
            {
                path: 'feedback-manager',
                name: 'feedback-manager',
                component: () => import('@/views/message-manager/feedback-manager'),
                meta: {title: '反馈管理', icon: 'el-icon-message'}
            }
        ]
    },

    {
        path: '/article-content',
        name: 'article-content',
        Layout,
        hidden: true,
        component: () => import('@/views/article-content/article-content'),
        meta: {title: '文章内容', icon: 'el-icon-s-help'}
    },

    {
        path: '/article-update/:id',
        name: 'article-update',
        Layout,
        hidden: true,
        component: () => import('@/views/article-content/article-update'),
        meta: {title: '修改文章', icon: 'el-icon-s-help'}
    },
    {
        path: '/system-manager',
        name: 'system-manager',
        component: Layout,
        meta: {title: '系统管理', icon: 'el-icon-setting'},
        children: [
            {
                path: '/user',
                name: 'user',
                component: () => import('@/views/user/index'),
                meta: {title: '个人中心', icon: 'el-icon-user'}
            },
            {
                path: 'index',
                name: 'Form',
                component: () => import('@/views/form/index'),
                meta: {title: 'Form', icon: 'form'}
            }
        ]
    },

    {
        path: '/log-manage',
        component: Layout,
        redirect: '/log-manage-manage/user-log-manage',
        name: 'log',
        meta: {title: '日志管理', icon: 'el-icon-chat-line-round'},
        children: [
            {
                path: 'user-log',
                name: 'userLog',
                component: () => import('@/views/log-manage/user-log.vue'),
                meta: {title: '用户日志', icon: 'el-icon-cherry'}
            },
            {
                path: 'operate-log',
                name: 'operateLog',
                component: () => import('@/views/log-manage/operate-log.vue'),
                meta: {title: '操作日志', icon: 'el-icon-ship'}
            },
            {
                path: 'abnormal-log',
                name: 'abnormalLog',
                component: () => import('@/views/log-manage/abnormal-log.vue'),
                meta: {title: '异常日志', icon: 'el-icon-orange'}
            }
        ]
    },

    {
        path: '/form',
        component: Layout,
        children: [
            {
                path: 'index',
                name: 'Form',
                component: () => import('@/views/form/index'),
                meta: {title: 'Form', icon: 'form'}
            }
        ]
    },

    {
        path: '/nested',
        component: Layout,
        redirect: '/nested/menu1',
        name: 'Nested',
        meta: {
            title: 'Nested',
            icon: 'nested'
        },
        children: [
            {
                path: 'menu1',
                component: () => import('@/views/nested/menu1/index'), // Parent router-view
                name: 'Menu1',
                meta: {title: 'Menu1'},
                children: [
                    {
                        path: 'menu1-1',
                        component: () => import('@/views/nested/menu1/menu1-1'),
                        name: 'Menu1-1',
                        meta: {title: 'Menu1-1'}
                    },
                    {
                        path: 'menu1-2',
                        component: () => import('@/views/nested/menu1/menu1-2'),
                        name: 'Menu1-2',
                        meta: {title: 'Menu1-2'},
                        children: [
                            {
                                path: 'menu1-2-1',
                                component: () => import('@/views/nested/menu1/menu1-2/menu1-2-1'),
                                name: 'Menu1-2-1',
                                meta: {title: 'Menu1-2-1'}
                            },
                            {
                                path: 'menu1-2-2',
                                component: () => import('@/views/nested/menu1/menu1-2/menu1-2-2'),
                                name: 'Menu1-2-2',
                                meta: {title: 'Menu1-2-2'}
                            }
                        ]
                    },
                    {
                        path: 'menu1-3',
                        component: () => import('@/views/nested/menu1/menu1-3'),
                        name: 'Menu1-3',
                        meta: {title: 'Menu1-3'}
                    }
                ]
            },
            {
                path: 'menu2',
                component: () => import('@/views/nested/menu2/index'),
                name: 'Menu2',
                meta: {title: 'menu2'}
            }
        ]
    },

    {
        path: 'external-link',
        component: Layout,
        children: [
            {
                path: 'https://panjiachen.github.io/vue-element-admin-site/#/',
                meta: {title: 'External Link', icon: 'link'}
            }
        ]
    },

    // 404 page must be placed at the end !!!
    {path: '*', redirect: '/404', hidden: true}
]

const createRouter = () => new Router({
    // mode: 'history', // require service support
    scrollBehavior: () => ({y: 0}),
    routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
    const newRouter = createRouter()
    router.matcher = newRouter.matcher // reset router
}

export default router
