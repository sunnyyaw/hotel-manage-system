<template>
  <div>
  <el-page-header @back="goBack" :content="this.$route.params.userId==null?'添加用户':'修改用户'">
  </el-page-header>
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
      <el-form-item label="状态">
        <el-radio-group v-model="userForm.status">
          <el-radio :label="0">禁用</el-radio>
          <el-radio :label="1">启用</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="goBack" type="primary">取消</el-button>
      <el-button @click="onSubmit" type="primary">确定</el-button>
    </div>
  </div>
</template>
<script>
export default {
  name: 'UserForm',
  data () {
    return {
      userForm: {
        username: '',
        password: '',
        roles: [],
        phone: ''
      },
      roles: ''
    }
  },
  created () {
    this.init()
  },
  methods: {
    goBack () {
      this.$router.replace('/users')
    },
    handlePasswordChange () {
      this.userForm.needEncode = true
    },
    init () {
      if (this.$route.params.userId != null) {
        this.getUser()
      }
      this.getRoles()
    },
    getUser () {
      this.$axios.get(`/users/${this.$route.params.userId}`)
        .then(resp => {
          if (resp && resp.data.code === 200) {
            this.userForm = resp.data.data
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
    },
    getRoles () {
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
        }).catch(error => {
          this.$message({
            type: 'error',
            message: `系统接口${error.response.status}错误`
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
            this.goBack()
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
  }
}
</script>
<style scoped>
</style>
