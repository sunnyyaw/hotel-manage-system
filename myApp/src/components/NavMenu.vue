<template>
  <el-menu :default-active="this.$route.path"
  class="el-menu-demo"
  router mode="horizontal">
  <el-menu-item v-for="(item,i) in navList" :key="i" :index="item.name">{{ item.navItem }}</el-menu-item>
  <i class="el-icon-switch-button" v-on:click="logout"></i>
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
        {name: '/customer', navItem: '餐桌'},
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
