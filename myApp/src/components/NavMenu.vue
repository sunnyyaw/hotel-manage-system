<template>
  <el-menu :default-active="defaultActive"
  class="el-menu-demo"
  router mode="horizontal">
  <el-menu-item v-for="(item,i) in navList" :key="i" :index="item.name">{{ item.navItem }}</el-menu-item>
  <el-button @click="logout" size="small" type="warning" icon="el-icon-switch-button"> 注销 </el-button>
  </el-menu>
</template>
<script>
export default {
  name: 'NavMenu',
  data () {
    return {
      navList: [
        {name: '/index', navItem: '首页'},
        {name: '/dish', navItem: '菜单'},
        {name: '/dishes', navItem: '菜品管理'},
        {name: '/setmeals', navItem: '套餐管理'},
        {name: '/category', navItem: '类别管理'},
        {name: '/customer', navItem: '餐桌管理'},
        {name: '/bill', navItem: '账单管理'},
        {name: '/user', navItem: '个人中心'}
      ]
    }
  },
  computed: {
    defaultActive () {
      switch (this.$route.path) {
        case '/userInfo':
          return '/user'
        case '/userBill':
          return '/user'
        case '/users':
          return '/user'
        case '/role':
          return '/user'
        case '/permission':
          return '/user'
        default:
          return this.$route.path
      }
    }
  },
  methods: {
    logout () {
      this.$axios.get('/logout').then(resp => {
        if (resp.data.code === 200) {
          this.$store.commit('logout')
          this.$router.replace('/login')
        }
      })
    }
  }
}
</script>
<style scoped>
</style>
