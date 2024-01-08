<template>
    <el-container>
        <el-aside stype="width: 200px;margin-top: 20px">
            <switch></switch>
            <user-side-menu ref="sideMenu"></user-side-menu>
        </el-aside>
        <el-main>
          <router-view></router-view>
        </el-main>
    </el-container>
</template>
<script>
import UserSideMenu from './UserSideMenu'
export default {
  name: 'UserIndex',
  components: {UserSideMenu},
  data () {
    return {
    }
  },
  methods: {
  }
}
</script>
<style scoped>
</style>
