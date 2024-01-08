<template>
    <el-container>
        <el-aside stype="width: 200px;margin-top: 20px">
            <switch></switch>
            <SideMenu @indexSelect="listByCategory" ref="sideMenu"></SideMenu>
        </el-aside>
        <el-main>
            <dishes class="dishes-area" ref="dishesArea"></dishes>
        </el-main>
    </el-container>
</template>
<script>
import SideMenu from './SideMenu'
import Dishes from './Dishes'
export default {
  name: 'DishIndex',
  components: {SideMenu, Dishes},
  methods: {
    listByCategory () {
      var cid = this.$refs.sideMenu.cid
      var url = ''
      if (cid === '0') {
        url = 'dishes'
      } else {
        url = 'categories/' + cid + '/dishes'
      }
      this.$axios.get(url)
        .then(resp => {
          if (resp && resp.status === 200) {
            this.$refs.dishesArea.dishes = resp.data
          }
        })
    }
  }
}
</script>
<style scoped>
</style>
