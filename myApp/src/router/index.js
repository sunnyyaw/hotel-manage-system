import Vue from 'vue'
import Router from 'vue-router'
import AppIndex from '@/components/AppIndex'
import DishIndex from '@/components/DishIndex'
import CustomerIndex from '@/components/CustomerIndex'
import UserIndex from '@/components/UserIndex'
import UserInfo from '@/components/UserInfo'
import Users from '@/components/Users'
import Permissions from '@/components/Permissions'
import Roles from '@/components/Roles'
import Login from '@/components/Login'
import Home from '@/components/Home'
import Error from '@/components/Error'
Vue.use(Router)

export default new Router({
  mode: 'history',
  routes: [
    {
      path: '/home',
      name: 'Home',
      component: Home,
      redirect: '/index',
      children: [
        {
          path: '/index',
          name: 'AppIndex',
          component: AppIndex,
          meta: {
            requireAuth: true
          }
        },
        {
          path: '/',
          name: 'AppIndex',
          component: AppIndex,
          meta: {
            requireAuth: true
          }
        },
        {
          path: '/dish',
          name: 'DishIndex',
          component: DishIndex,
          meta: {
            requireAuth: true
          }
        },
        {
          path: '/customer',
          name: 'CustomerIndex',
          component: CustomerIndex,
          meta: {
            requireAuth: true
          }
        },
        {
          path: '/user',
          name: 'UserIndex',
          component: UserIndex,
          redirect: '/userInfo',
          meta: {
            requireAuth: true
          },
          children: [
            {
              path: '/userInfo',
              name: 'UserInfo',
              component: UserInfo,
              meta: {
                requireAuth: true
              }
            },
            {
              path: '/users',
              name: 'Users',
              component: Users,
              meta: {
                requireAuth: true
              }
            },
            {
              path: '/role',
              name: 'Roles',
              component: Roles,
              meta: {
                requireAuth: true
              }
            },
            {
              path: '/permission',
              name: 'Permissions',
              component: Permissions,
              meta: {
                requireAuth: true
              }
            }
          ]
        }
      ]
    },
    {
      path: '/login',
      name: 'Login',
      component: Login
    },
    {
      path: '*',
      name: 'Error',
      component: Error,
      meta: {
        requireAuth: true
      }
    }
  ]
})
