<template>
    <el-menu
        class="categories"
        default-active=""
        @select="handleSelect"
        active-text-color="red">
    <el-menu-item v-for="(item,index) in navList" :key="index" :index="item.id">
      <i class="el-icon-menu"></i>
      {{ item.name }}
    </el-menu-item>
    </el-menu>
</template>
<script>
export default {
  name: 'SideMenu',
  data () {
    return {
      navList: [
        {id: '', name: '所有菜品', type: 3},
        {id: '-1', name: '所有套餐', type: 4}
      ]
    }
  },
  created () {
    this.loadCategories()
  },
  methods: {
    loadCategories () {
      this.navList = [
        {id: '', name: '所有菜品', type: 3},
        {id: '-1', name: '所有套餐', type: 4}
      ]
      this.$axios.get('/categories')
        .then(resp => {
          if (resp && resp.status === 200) {
            this.navList = this.navList.concat(resp.data.data)
          }
        })
    },
    handleSelect (index) {
      let category = this.navList.find(item => item.id === index)
      this.$emit('indexSelect', index, category)
    },
    editCategory (item) {
      this.$refs.edit.dialogFormVisible = true
      this.$refs.edit.form = {
        id: item.id,
        name: item.name
      }
    },
    deleteCategory (id) {
      this.$confirm('此操作将永久删除此类别，是否继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$axios.delete('/categories/' + id)
          .then(resp => {
            if (resp && resp.status === 200) {
              this.loadCategories()
            }
          })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        }
        )
      })
    }
  }
}
</script>
<style scoped>
</style>
