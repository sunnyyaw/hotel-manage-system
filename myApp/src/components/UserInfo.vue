<template>
  <el-descriptions title="用户信息">
    <el-descriptions-item label="用户名">{{ user.username }}</el-descriptions-item>
    <el-descriptions-item label="密码">
      <el-input placeholder="请输入密码" v-model="user.password" show-password></el-input>
      <el-button type="text" @click="modifyPassword()">修改密码</el-button>
    </el-descriptions-item>
  </el-descriptions>
</template>
<script>
export default{
  name: 'UserInfo',
  components: {},
  data () {
    return {
      user: '',
      currentPage: 1,
      pageSize: 15
    }
  },
  mounted: function () {
    this.loadUser()
  },
  methods: {
    loadUser () {
      this.$axios
        .get('/userInfo')
        .then(resp => {
          if (resp && resp.data.code === 200) {
            this.user = resp.data.data
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
    modifyPassword () {
      this.$axios.post('/userInfo', this.user)
        .then(resp => {
          if (resp && resp.data.code === 200) {
            this.$message({
              type: 'success',
              message: '修改成功'
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
  }
}
</script>
<style scoped>
</style>
