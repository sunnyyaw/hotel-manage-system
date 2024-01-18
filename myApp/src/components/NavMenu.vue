<template>
  <el-menu :default-active="this.$route.path"
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
        {name: '/customer', navItem: '餐桌管理'},
        {name: '/bill', navItem: '账单管理'},
        {name: '/user', navItem: '个人中心'}
      ]
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
