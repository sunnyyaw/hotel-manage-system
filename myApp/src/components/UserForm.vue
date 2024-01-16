<template>
  <el-dialog
  title="创建/修改用户"
  @open="loadRoles"
  :visible.sync="dialogVisible"
  :before-close="clear">
    <el-form ref="form" :model="userForm">
      <el-form-item label="用户名">
        <el-input v-model="userForm.username"></el-input>
      </el-form-item>
      <el-form-item label="密码">
        <el-input @change="handlePasswordChange" v-model="userForm.password" show-password></el-input>
      </el-form-item>
      <el-form-item label="角色">
        <el-checkbox v-for="item in roles" :key="item.id" :label="item.roleName"
        v-model="item.checked"></el-checkbox>
      </el-form-item>
      <el-form-item label="手机号">
        <el-input v-model="userForm.phone"></el-input>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="clear" type="primary">取消</el-button>
      <el-button @click="onSubmit" type="primary">确定</el-button>
    </div>
  </el-dialog>
</template>
<script>
export default {
  name: 'UserForm',
  data () {
    return {
      dialogVisible: false,
      userForm: '',
      roles: ''
    }
  },
  methods: {
    clear () {
      this.userForm = ''
      this.dialogVisible = false
      this.roles = ''
    },
    handlePasswordChange () {
      this.userForm.needEncode = true
    },
    loadRoles () {
      this.$axios.get('/roles')
        .then(resp => {
          if (resp && resp.data.code === 200) {
            resp.data.data.forEach(role => {
              let check = false
              this.userForm.roles.forEach(selectedRole => {
                if (role.id === selectedRole.id) {
                  check = true
                }
              })
              role.checked = check
            })
            this.roles = resp.data.data
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
    },
    onSubmit () {
      this.userForm.roles = this.roles.filter(role => role.checked)
      this.$axios.post('/users', this.userForm)
        .then(resp => {
          if (resp && resp.data.code === 200) {
            this.$message({
              type: 'success',
              message: resp.data.message
            })
            this.dialogVisible = false
            this.$emit('onSubmit')
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
