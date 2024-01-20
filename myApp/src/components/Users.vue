<template>
  <div>
    <el-row>
      <el-col :span=12>
        <el-input
        placeholder="输入用户名搜索"
        v-model="input"
        clearable
        @blur="handleSearch">
        <i slot="prefix" class="el-input__icon el-icon-search"></i>
        </el-input>
      </el-col>
      <el-col :span=12>
        <router-link :to="'/users/add'">
          <el-button type="primary">添加用户</el-button>
        </router-link>
      </el-col>
    </el-row>
    <el-table
    :data="users"
    style="width:100%;">
      <el-table-column
      prop="username"
      label="用户名">
      </el-table-column>
      <el-table-column
      label="角色">
      <template slot-scope="scope">
        <span v-for="item in scope.row.roles" :key="item.id">{{ item.roleName }} </span>
      </template>
      </el-table-column>
      <el-table-column
      prop="phone"
      label="手机号">
      </el-table-column>
      <el-table-column
      label="状态">
      <template slot-scope="scope">
        <span>{{scope.row.status === 0 ? '已禁用' : '正常'}}</span>
      </template>
      </el-table-column>
      <el-table-column
        fixed="right"
        label="操作"
        width="150">
        <template slot-scope="scope">
          <router-link :to="`/users/${scope.row.id}/add`">
            <el-button size="small" type="text">编辑</el-button>
          </router-link>
          <el-button @click="handleDelete(scope.row)" size="small" type="text">删除</el-button>
          <el-button id="status1" v-if="scope.row.status === 0"
          @click="handleStatus(scope.row)"
          size="small" type="text">
            启用
          </el-button>
          <el-button id="status2" v-else
          @click="handleStatus(scope.row)"
          size="small" type="text">
            禁用
          </el-button>
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
import UserForm from './UserForm'
export default {
  name: 'Users',
  components: {UserForm},
  data () {
    return {
      users: [],
      pageSize: 10,
      count: 0,
      currentPage: 1,
      input: ''
    }
  },
  mounted: function () {
    this.loadUsers()
  },
  methods: {
    handleEdit (row) {
      this.$refs.userForm.userForm = row
      this.$refs.userForm.dialogVisible = true
    },
    handleDelete (row) {
      this.$confirm('此操作将永久删除此用户，是否继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$axios.delete('/users/' + row.id)
          .then(resp => {
            if (resp && resp.data.code === 200) {
              this.loadUsers()
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
              message: `系统接口${error.response.status}异常`
            })
          })
      }
      )
    },
    loadUsers () {
      this.$axios.get(`/users?currentPage=${this.currentPage}&pageSize=${this.pageSize}&username=${this.input}`)
        .then(resp => {
          this.users = resp.data.data
          this.count = resp.data.count
        }
        ).catch(failResponse => {
          this.$message({
            type: 'error',
            message: `系统接口${failResponse.status}错误`
          })
        })
    },
    handleCurrentChange (currentPage) {
      this.currentPage = currentPage
      this.loadUsers()
    },
    handleSizeChange (pageSize) {
      this.pageSize = pageSize
      this.loadUsers()
    },
    handleSearch () {
      this.currentPage = 1
      this.loadUsers()
    },
    handleStatus (user) {
      user.status = user.status === 0 ? 1 : 0
      this.$axios.put('/users', user)
        .then(resp => {
          if (resp && resp.data.code === 200) {
            this.$message({
              type: 'success',
              message: resp.data.message
            })
          } else {
            this.loadUsers()
            this.$message({
              type: 'warning',
              message: resp.data.message
            })
          }
        }).catch(error => {
          this.loadUsers()
          if (error.response) {
            this.$message({
              type: 'danger',
              message: `系统接口${error.response.status}异常`
            })
          } else {
            this.$message({
              type: 'danger',
              message: '服务器无响应'
            })
          }
        })
    }
  }
}
</script>
<style scoped>
#status2 {
  color:red;
}
</style>
