<template>
  <div>
    <el-row style="height: 840px;">
      <search-bar @onSearch="searchResult" ref="searchBar"></search-bar>
        <el-tooltip effect="dark" placement="right"
        v-for="item in dishes.slice((currentPage-1)*pageSize,currentPage*pageSize)"
        :key="item.id">
        <p slot="content" style="font-size: 13px;margin-bottom: 6px;">
            <span>{{ item.description }}</span>
        </p>
        <el-card style="width: 135px;margin-bottom: 20px;height: 233px;float: left;margin-right: 15px;"
        class="dish" dishStyle="padding:10px" shadow="hover" >
        <div class="cover" @click="editDish(item)">
            <img :src="item.cover" alt="封面">
        </div>
        <div class="info">
            <div class="name">
                <a href="">{{ item.dishName }}</a>
            </div>
            <i class="el-icon-edit" @click="editDish(item)"></i>
            <i class="el-icon-delete" @click="deleteDish(item.id)"></i>
            <div class="price">{{ item.unitPrice }}元</div>
        </div>
        </el-card>
        </el-tooltip>
        <edit-form @onSubmit="loadDishes()" ref="edit"></edit-form>
    </el-row>
    <el-row>
        <el-pagination
            @current-change="handleCurrentChange"
            :current-page="currentPage"
            :page-size="pageSize"
            :total="dishes.length">
        </el-pagination>
    </el-row>
  </div>
</template>
<script>
import EditForm from './EditForm'
import SearchBar from './SearchBar'
export default{
  name: 'Dishes',
  components: {EditForm, SearchBar},
  data () {
    return {
      dishes: [],
      currentPage: 1,
      pageSize: 15
    }
  },
  mounted: function () {
    this.loadDishes()
  },
  methods: {
    loadDishes () {
      this.$axios.get('/dishes').then(resp => {
        if (resp && resp.status === 200) {
          this.dishes = resp.data
        }
      })
    },
    editDish (item) {
      this.$refs.edit.dialogFormVisible = true
      this.$refs.edit.form = {
        id: item.id,
        cover: item.cover,
        dishName: item.dishName,
        categoryId: item.categoryId,
        description: item.description,
        unitPrice: item.unitPrice
      }
    },
    searchResult () {
      this.$axios.get('/dishes?keyword=' +
      this.$refs.searchBar.keyword, {})
        .then(resp => {
          if (resp && resp.status === 200) {
            this.dishes = resp.data
          }
        })
    },
    handleCurrentChange: function (currentPage) {
      this.currentPage = currentPage
    },
    deleteDish (id) {
      this.$confirm('此操作将永久删除此餐品，是否继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$axios.delete('/dishes/' + id)
          .then(resp => {
            if (resp && resp.status === 200) {
              this.loadDishes()
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
img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
</style>
