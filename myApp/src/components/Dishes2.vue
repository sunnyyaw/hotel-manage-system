<template>
  <div>
    <el-row>
      <el-col :span=12>
        <el-input
        placeholder="输入菜品名搜索"
        v-model="input"
        clearable
        @blur="handleSearch">
        <i slot="prefix" class="el-input__icon el-icon-search"></i>
        </el-input>
      </el-col>
      <el-col :span=12>
        <el-button type="primary" @click="handleAdd">添加菜品</el-button>
      </el-col>
    </el-row>
    <el-table
    :data="dishes"
    style="width:100%;">
      <el-table-column
      prop="dishName"
      label="菜品名">
      </el-table-column>
      <el-table-column
      label="菜品类别">
        <template slot-scope="scope">
          <span>{{ scope.row.category.name }}</span>
        </template>
      </el-table-column>
      <el-table-column
      prop="description"
      label="菜品描述">
      </el-table-column>
      <el-table-column
      prop="unitPrice"
      label="单价">
      </el-table-column>
      <el-table-column
      label="封面">
        <template slot-scope="scope">
          <img v-if="scope.row.cover && scope.row.cover!=''"
          :src="scope.row.cover"
          class="avatar">
          <span v-else>无图片</span>
        </template>
      </el-table-column>
      <el-table-column
      prop="billNum"
      label="关联账单数">
      </el-table-column>
      <el-table-column
      label="状态">
      <template slot-scope="scope">
        <span>{{ scope.row.status === 0 ? '停售' : '启售' }}</span>
      </template>
      </el-table-column>
      <el-table-column
        fixed="right"
        label="操作"
        width="200">
        <template slot-scope="scope">
          <el-button @click="editDish(scope.row)" size="small" type="text">编辑</el-button>
          <el-button @click="deleteDish(scope.row.id)" size="small" type="text">删除</el-button>
          <el-button v-if="scope.row.status === 0" @click="handleStatus(scope.row)"
           size="small" type="text">启售</el-button>
          <el-button id="status1" v-else @click="handleStatus(scope.row)"
           size="small" type="text">停售</el-button>
        </template>
      </el-table-column>
    </el-table>
    <edit-form @onSubmit="loadDishes" ref="edit"></edit-form>
    <el-row>
        <el-pagination
            @current-change="handleCurrentChange"
            @size-change="handleSizeChange"
            :current-page="currentPage"
            :page-size="pageSize"
            :total="count"
            layout="total, sizes, prev, pager, next, jumper">
        </el-pagination>
    </el-row>
  </div>
</template>
<script>
import EditForm from './EditForm'
export default{
  name: 'Dishes2',
  components: {EditForm},
  data () {
    return {
      dishes: [],
      currentPage: 1,
      pageSize: 10,
      count: 0,
      input: ''
    }
  },
  created () {
    this.loadDishes()
  },
  methods: {
    loadDishes () {
      this.$axios.get(`/dishes?currentPage=${this.currentPage}&pageSize=${this.pageSize}&dishName=${this.input}`).then(resp => {
        if (resp && resp.status === 200) {
          this.dishes = resp.data.data
          this.count = resp.data.count
        }
      })
    },
    editDish (item) {
      this.$refs.edit.dialogFormVisible = true
      this.$refs.edit.dish = {
        id: item.id,
        dishName: item.dishName,
        categoryId: item.categoryId,
        unitPrice: item.unitPrice,
        description: item.description,
        cover: item.cover
      }
    },
    handleCurrentChange (currentPage) {
      this.currentPage = currentPage
      this.loadDishes()
    },
    handleSizeChange (size) {
      this.pageSize = size
      this.currentPage = 1
      this.loadDishes()
    },
    handleSearch () {
      this.currentPage = 1
      this.loadDishes()
    },
    handleAdd () {
      this.$refs.edit.dialogFormVisible = true
    },
    handleStatus (row) {
      let data = {
        id: row.id,
        status: row.status === 0 ? 1 : 0}
      this.$axios.post('/dishes', data).then(resp => {
        if (resp && resp.data.code === 200) {
          this.$message({
            type: 'success',
            message: resp.data.message
          })
          this.loadDishes()
        } else {
          this.$message({
            type: 'warning',
            message: resp.data.message
          })
        }
      }).catch(error => {
        this.$message({
          type: 'error',
          message: `系统接口${error.response.status}异常`
        })
      })
    },
    deleteDish (id) {
      this.$confirm('此操作将永久删除此菜品，是否继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$axios.delete(`/dishes/${id}`)
          .then(resp => {
            if (resp && resp.data.code === 200) {
              this.$message({
                type: 'success',
                message: resp.data.message
              })
              this.loadDishes()
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
      }).catch(() => {
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
#status1 {
  color: red;
}
</style>
