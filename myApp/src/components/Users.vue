<template>
  <div>
    <el-table
    :data="users"
    style="width:100%;">
      <el-table-column
      prop="id"
      label="用户编号">
      </el-table-column>
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
        fixed="right"
        label="操作"
        width="150">
        <template slot-scope="scope">
          <el-button-group>
            <el-button @click="handleEdit(scope.row)" size="small" type="warning" icon="el-icon-edit"></el-button>
            <el-button @click="handleDelete(scope.row)" size="small" type="danger" icon="el-icon-delete"></el-button>
          </el-button-group>
        </template>
      </el-table-column>
    </el-table>
    <el-row>
        <el-button @click="handleAdd" type="primary">添加用户</el-button>
    </el-row>
    <user-form @onSubmit="loadUsers" ref="userForm"></user-form>
  </div>
</template>
<script>
import UserForm from './UserForm'
export default {
  name: 'Users',
  components: {UserForm},
  data () {
    return {
      users: []
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
          }).catch(failResponse => {
            this.$message({
              type: 'error',
              message: '出错'
            })
          })
      }
      )
    },
    handleAdd () {
      this.$refs.userForm.clear()
      this.$refs.userForm.dialogVisible = true
    },
    loadUsers () {
      this.$axios.get('/users')
        .then(resp => {
          if (resp && resp.data.code === 200) {
            this.users = resp.data.data
          } else {
            this.$message({
              type: 'warning',
              message: resp.data.message
            })
          }
        }).catch(failResponse => {
          this.$message({
            type: 'error',
            message: '出错'
          })
        })
    }
  }
}
</script>
<style scoped>
</style>
