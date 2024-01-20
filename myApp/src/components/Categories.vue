<template>
  <div>
    <el-row>
      <el-col :span=12>
        <el-input
        placeholder="输入类别名搜索"
        v-model="input"
        clearable
        @blur="handleSearch">
        <i slot="prefix" class="el-input__icon el-icon-search"></i>
        </el-input>
      </el-col>
      <el-col :span=12>
        <category-form @onSubmit="loadCategories"></category-form>
      </el-col>
    </el-row>
    <el-table
    :data="categories"
    style="width:100%;">
      <el-table-column
      prop="name"
      label="类别名">
      </el-table-column>
      <el-table-column
      label="类型">
      <template slot-scope="scope">
        <span >{{ scope.row.type === 0 ? '菜品类别' : '套餐类别' }} </span>
      </template>
      </el-table-column>
      <el-table-column
      prop="sort"
      label="排序字段">
      </el-table-column>
      <el-table-column
        fixed="right"
        label="操作"
        width="150">
        <template slot-scope="scope">
          <router-link :to="`/categories/${scope.row.id}/update`">
            <el-button size="small" type="text">编辑</el-button>
          </router-link>
          <el-button @click="handleDelete(scope.row)" size="small" type="text">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
    @size-change="handleSizeChange"
    @current-change="handleCurrentChange"
    :current-page="currentPage"
    :page-size="pageSize"
    :total="count"
    layout="total, sizes, prev, pager, next, jumper">
    </el-pagination>
  </div>
</template>
<script>
import CategoryForm from './CategoryForm'
export default {
  name: 'Categories',
  components: {CategoryForm},
  data () {
    return {
      categories: [],
      pageSize: 10,
      count: 0,
      currentPage: 1,
      input: ''
    }
  },
  mounted: function () {
    this.loadCategories()
  },
  methods: {
    handleEdit (row) {
    },
    handleDelete (row) {
      this.$confirm('此操作将永久删除此类别，是否继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$axios.delete(`/categories/${row.id}`)
          .then(resp => {
            if (resp && resp.data.code === 200) {
              this.loadCategories()
              this.$message({
                type: 'success',
                message: resp.data.message
              })
            } else {
              this.$message({
                type: 'warning',
                message: resp.data.message
              })
            }
          }).catch(error => {
            this.$message({
              type: 'error',
              message: `系统接口${error.response.status}错误`
            })
          })
      }
      )
    },
    loadCategories () {
      this.$axios.get(`/categories?currentPage=${this.currentPage}&pageSize=${this.pageSize}&name=${this.input}`)
        .then(resp => {
          this.categories = resp.data.data
          this.count = resp.data.count
        }
        ).catch(error => {
          this.$message({
            type: 'error',
            message: `系统接口${error.response.status}错误`
          })
        })
    },
    handleCurrentChange (currentPage) {
      this.currentPage = currentPage
      this.loadCategories()
    },
    handleSizeChange (pageSize) {
      this.pageSize = pageSize
      this.currentPage = 1
      this.loadCategories()
    },
    handleSearch () {
      this.currentPage = 1
      this.loadCategories()
    }
  }
}
</script>
<style scoped>
</style>
