<template>
  <div>
  <el-descriptions class="margin-top" title="用户信息" :column="2" border>
    <el-descriptions-item label="用户名">{{ user.username }}</el-descriptions-item>
    <el-descriptions-item label="密码">
      <el-input placeholder="请输入密码" @change="user.needEncode=true" v-model="user.password" show-password></el-input>
    </el-descriptions-item>
    <el-descriptions-item label="电话">
      <el-input placeholder="请输入电话号码" v-model="user.phone"></el-input>
    </el-descriptions-item>
    <el-descriptions-item label="角色">
      <span v-for="item in user.roles" :key="item.id">{{ item.roleName }}</span>
    </el-descriptions-item>
  </el-descriptions>
  <el-button type="primary" @click="modifyUser()">保存修改</el-button>
  </div>
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
    modifyUser () {
      this.$axios.post('/userInfo', this.user)
        .then(resp => {
          if (resp && resp.data.code === 200) {
            this.$message({
              type: 'success',
              message: '修改成功'
            })
            this.loadUser()
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
