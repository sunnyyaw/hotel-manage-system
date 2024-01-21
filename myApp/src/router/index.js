import Vue from 'vue'
import Router from 'vue-router'
import AppIndex from '@/components/AppIndex'
import DishIndex from '@/components/DishIndex'
import DishInfo from '@/components/DishInfo'
import Dishes2 from '@/components/Dishes2'
import CustomerIndex from '@/components/CustomerIndex'
import Categories from '@/components/Categories'
import CategoryUpdate from '@/components/CategoryUpdate'
import UserIndex from '@/components/UserIndex'
import UserInfo from '@/components/UserInfo'
import UserBill from '@/components/UserBill'
import UserForm from '@/components/UserForm'
import Users from '@/components/Users'
import Bills from '@/components/Bills'
import Permissions from '@/components/Permissions'
import Roles from '@/components/Roles'
import Login from '@/components/Login'
import PhoneLogin from '@/components/PhoneLogin'
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
          path: '/dishes',
          name: 'Dishes2',
          component: Dishes2,
          meta: {
            requireAuth: true
          }
        },
        {
          path: '/dishes/:dishId/comments',
          name: 'DishInfo',
          component: DishInfo,
          meta: {
            requireAuth: true
          }
        },
        {
          path: '/category',
          name: 'Categories',
          component: Categories,
          meta: {
            requireAuth: true
          }
        },
        {
          path: '/categories/:categoryId/update',
          name: 'CategoryUpdate',
          component: CategoryUpdate,
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
          path: '/bill',
          name: 'Bills',
          component: Bills,
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
              path: '/userBill',
              name: 'UserBill',
              component: UserBill,
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
              path: '/users/:userId?/add',
              name: 'UserForm',
              component: UserForm,
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
      path: '/phoneLogin',
      name: 'phoneLogin',
      component: PhoneLogin
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
