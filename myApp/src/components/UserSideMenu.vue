<template>
    <el-menu
    :default-active="this.$route.path"
    router
    class="el-menu-vertical-demo">
      <el-menu-item v-for="(item,i) in navList" :key="i" :index="item.name">
        <i class="el-icon-document"></i>
        <span slot="title">{{ item.navItem }}</span>
      </el-menu-item>
    </el-menu>
</template>
<script>
export default {
  name: 'UserSideMenu',
  data () {
    return {
      navList: [
        {name: '/userInfo', navItem: '个人信息'},
        {name: '/userBill', navItem: '用户账单'},
        {name: '/users', navItem: '用户管理'},
        {name: '/role', navItem: '角色管理'},
        {name: '/permission', navItem: '权限管理'}
      ]
    }
  },
  methods: {
  }
}
</script>
<style scoped>
</style>
