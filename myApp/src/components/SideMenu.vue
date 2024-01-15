<template>
    <el-menu
        class="categories"
        default-active="0"
        @select="handleSelect"
        active-text-color="red">
    <el-menu-item v-for="item in navList" :key="item.id" :index="item.id+''">
      <i class="el-icon-menu"></i>
      {{ item.name }}
      <i v-if="item.id>0" class="el-icon-edit" @click="editCategory(item)"></i>
      <i v-if="item.id>0" class="el-icon-delete" @click="deleteCategory(item.id)"></i>
    </el-menu-item>
    <edit-category @onSubmit="loadCategories()" ref="edit"></edit-category>
    </el-menu>
</template>
<script>
import EditCategory from './EditCategory'
export default {
  name: 'SideMenu',
  components: {EditCategory},
  data () {
    return {
      navList: [
        {id: 0, name: '全部'}
      ],
      cid: 0
    }
  },
  mounted: function () {
    this.loadCategories()
  },
  methods: {
    loadCategories () {
      this.navList = [{id: 0, name: '全部'}]
      this.$axios.get('/categories')
        .then(resp => {
          if (resp && resp.status === 200) {
            this.navList = this.navList.concat(resp.data)
          }
        })
    },
    handleSelect (index) {
      this.cid = Number(index)
      this.$emit('indexSelect')
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
