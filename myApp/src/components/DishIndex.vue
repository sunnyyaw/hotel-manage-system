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
    listByCategory (index) {
      this.$refs.dishesArea.cid = index
      this.$refs.dishesArea.currentPage = 1
      this.$refs.dishesArea.loadDishes()
    }
  }
}
</script>
<style scoped>
</style>
