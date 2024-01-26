<template>
  <div>
    <el-row>
      <el-col :span=16>
        <el-input
        placeholder="输入套餐名搜索"
        v-model="input"
        clearable
        @blur="handleSearch">
        <i slot="prefix" class="el-input__icon el-icon-search"></i>
        </el-input>
      </el-col>
      <el-col :span=8>
        <el-button @click="batchDelete" type="text">批量删除</el-button>
        <el-button @click="batchUpdate(1)" type="text">批量启售</el-button>
        <el-button @click="batchUpdate(0)" type="text">批量停售</el-button>
        <router-link to="/setmeals/add">
          <el-button type="primary">添加套餐</el-button>
        </router-link>
      </el-col>
    </el-row>
    <el-table
    :data="setmeals"
    :row-key="(row)=>row.id"
    style="width:100%;"
    ref="multipleTable">
      <el-table-column
      type="selection"
      :reserve-selection="true">
      </el-table-column>
      <el-table-column
      prop="name"
      label="套餐名">
      </el-table-column>
      <el-table-column
      label="套餐类别">
        <template slot-scope="scope">
          <span>{{ scope.row.category ? scope.row.category.name : '未分配'}}</span>
        </template>
      </el-table-column>
      <el-table-column
      prop="description"
      label="套餐描述">
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
      label="关联菜品数">
        <template slot-scope="scope">
          <span>{{ scope.row.dishList.length }}</span>
        </template>
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
          <router-link :to="`/setmeals/${scope.row.id}/add`">
            <el-button size="small" type="text">编辑</el-button>
          </router-link>
          <el-button @click="deleteSetmeal(scope.row.id)" size="small" type="text">删除</el-button>
          <el-button v-if="scope.row.status === 0" @click="handleStatus(scope.row)"
           size="small" type="text">启售</el-button>
          <el-button id="status1" v-else @click="handleStatus(scope.row)"
           size="small" type="text">停售</el-button>
        </template>
      </el-table-column>
    </el-table>
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
export default{
  name: 'Setmeal',
  data () {
    return {
      setmeals: [],
      currentPage: 1,
      pageSize: 10,
      count: 0,
      input: ''
    }
  },
  created () {
    this.load()
  },
  methods: {
    load () {
      this.$axios.get(`/setmeals?currentPage=${this.currentPage}&pageSize=${this.pageSize}&name=${this.input}`).then(resp => {
        if (resp && resp.status === 200) {
          this.setmeals = resp.data.data
          this.count = resp.data.count
        }
      })
    },
    handleCurrentChange (currentPage) {
      this.currentPage = currentPage
      this.load()
    },
    handleSizeChange (size) {
      this.pageSize = size
      this.currentPage = 1
      this.load()
    },
    handleSearch () {
      this.currentPage = 1
      this.load()
    },
    handleStatus (row) {
      let data = {
        id: row.id,
        status: row.status === 0 ? 1 : 0}
      this.$axios.put('/setmeals', data).then(resp => {
        if (resp && resp.data.code === 200) {
          this.$message({
            type: 'success',
            message: resp.data.message
          })
          this.load()
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
    deleteSetmeal (id) {
      this.$confirm('此操作将永久删除此套餐，是否继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$axios.delete(`/setmeals/${id}`)
          .then(resp => {
            if (resp && resp.data.code === 200) {
              this.$message({
                type: 'success',
                message: resp.data.message
              })
              this.load()
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
    },
    batchUpdate (newStatus) {
      let selection = this.$refs.multipleTable.selection
      let dishList = selection.map(dish => {
        return {
          id: dish.id,
          status: newStatus
        }
      })
      this.$axios.put('/setmealsBatch', dishList).then(resp => {
        if (resp && resp.data.code === 200) {
          this.$message({
            type: 'success',
            message: resp.data.message
          })
          this.load()
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
    batchDelete () {
      let selection = this.$refs.multipleTable.selection
      let ids = selection.map(setmeal => setmeal.id)
      console.log(ids)
      this.$axios.delete(`/setmeals?ids=${ids}`).then(resp => {
        if (resp && resp.data.code === 200) {
          this.$message({
            type: 'success',
            message: resp.data.message
          })
          this.load()
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
