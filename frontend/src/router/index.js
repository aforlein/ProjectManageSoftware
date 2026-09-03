import { createRouter, createWebHashHistory } from 'vue-router'
import { getToken } from '@/utils/auth'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: { noAuth: true }
  },
  {
    path: '/',
    component: () => import('@/layout/index.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/index.vue'),
        meta: { title: 'Dashboard' }
      },
      {
        path: 'project',
        name: 'ProjectList',
        component: () => import('@/views/project/index.vue'),
        meta: { title: '项目列表' }
      },
      {
        path: 'project/detail/:id',
        name: 'ProjectDetail',
        component: () => import('@/views/project/detail.vue'),
        meta: { title: '项目详情' }
      },
      {
        path: 'risk',
        name: 'RiskList',
        component: () => import('@/views/risk/index.vue'),
        meta: { title: '风险管理' }
      },
      {
        path: 'issue',
        name: 'IssueList',
        component: () => import('@/views/issue/index.vue'),
        meta: { title: '问题日志' }
      },
      {
        path: 'map',
        name: 'ProjectMap',
        component: () => import('@/views/map/index.vue'),
        meta: { title: '项目地图' }
      },
      {
        path: 'system/user',
        name: 'UserManage',
        component: () => import('@/views/system/user.vue'),
        meta: { title: '用户管理', roles: ['ADMIN'] }
      },
      {
        path: 'system/role',
        name: 'RoleManage',
        component: () => import('@/views/system/role.vue'),
        meta: { title: '角色管理', roles: ['ADMIN'] }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

// 导航守卫
router.beforeEach((to, from, next) => {
  const token = getToken()

  if (to.meta.noAuth) {
    // 已登录用户访问登录页，重定向到首页
    if (token) {
      next('/dashboard')
    } else {
      next()
    }
    return
  }

  if (!token) {
    next('/login')
    return
  }

  next()
})

export default router
