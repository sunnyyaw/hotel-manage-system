<template>
    <el-form class="login-container" label-position="left">
        <h3 class="login_title">系统登录</h3>
        <el-form-item>
            <el-input type="text" v-model="loginForm.username"
            auto-complete="off" placeholder="账号"></el-input>
        </el-form-item>
        <el-form-item>
            <el-input type="text" v-model="loginForm.password"
            auto-complete="off" placeholder="密码" show-password></el-input>
        </el-form-item>
        <el-form-item style="width:100%">
            <el-button type="primary" v-on:click="login" v-loading="loginLoading">登录</el-button>
            <el-button type="primary" v-on:click="register" v-loading="registerLoading">注册</el-button>
            <el-checkbox v-model="loginForm.rememberMe">记住我</el-checkbox>
        </el-form-item>
    </el-form>
</template>
<script>
export default {
  name: 'Login',
  data () {
    return {
      loginForm: {
        username: '',
        password: '',
        rememberMe: false
      },
      loginLoading: false,
      registerLoading: false,
      responseResult: []
    }
  },
  methods: {
    login () {
      this.loginLoading = true
      this.axios.post('/login', this.loginForm)
        .then(successResponse => {
          if (successResponse.data.code === 200) {
            this.$store.commit('login', this.loginForm)
            var path = this.$route.query.redirect
            this.$router.replace({path: path === '/' || path === undefined ? '/index' : path})
          } else {
            this.$alert(successResponse.data.message, '提示', {
              confirmButtonText: '确定'})
          }
          this.loginLoading = false
        })
        .catch(failResponse => {
          this.$alert('登录失败', '提示', {
            confirmButtonText: '确定'})
          this.loginLoading = false
        })
    },
    register () {
      this.registerLoading = true
      this.axios.post('/register',
        {username: this.loginForm.username,
          password: this.loginForm.password})
        .then(successResponse => {
          if (successResponse.data.code === 200) {
            this.$alert('注册成功', '提示', {
              confirmButtonText: '确定'
            })
          } else {
            this.$alert(successResponse.data.message, '提示', {
              confirmButtonText: '确定'
            })
          }
          this.registerLoading = false
        })
        .catch(failResponse => {
          this.$alert('注册失败', '提示', {
            confirmButtonText: '确定'})
          this.registerLoading = false
        })
    }
  }
}
</script>
<style>
  .login-container {
    border-radius: 15px;
    background-clip: padding-box;
    margin: 90px auto;
    width: 350px;
    padding: 35px 35px 15px 35px;
    background: #fff;
    border: 1px solid #eaeaea;
    box-shadow: 0 0 25px #cac6c6;
  }

  .login_title {
    margin: 0px auto 40px auto;
    text-align: center;
    color: #505458;
  }

</style>
