<template>
    <el-form class="login-container" label-position="left">
        <h3 class="login_title">用户名密码登录</h3>
        <el-form-item>
            <el-input type="text" v-model="loginForm.username"
            auto-complete="off" placeholder="账号"></el-input>
        </el-form-item>
        <el-form-item>
            <el-input type="text" v-model="loginForm.password"
            auto-complete="off" placeholder="密码" show-password></el-input>
        </el-form-item>
        <el-form-item>
            <el-input type="text" v-model="loginForm.verifyCode"
            auto-complete="off" placeholder="验证码"></el-input>
            <img style="width:200px;height:100px;"
            :src="imgSrc" fit="fill" @click="refreshVerifyCode">
        </el-form-item>
        <el-form-item style="width:100%">
            <el-button type="primary" v-on:click="login"
             v-loading="loginLoading" element-loading-spinner="el-icon-loading">登录</el-button>
            <el-button type="primary" v-on:click="register"
             v-loading="registerLoading" element-loading-spinner="el-icon-loading">注册</el-button>
            <el-checkbox v-model="loginForm.rememberMe">记住我</el-checkbox>
            <el-link type="primary" href="http://localhost:8080/phoneLogin">验证码登录</el-link>
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
        phone: '',
        verifyCode: '',
        rememberMe: false
      },
      imgSrc: '',
      loginLoading: false,
      registerLoading: false
    }
  },
  mounted: function () {
    this.refreshVerifyCode()
  },
  methods: {
    login () {
      this.loginLoading = true
      this.axios.post('/login', this.loginForm)
        .then(successResponse => {
          if (successResponse.data.code === 200) {
            this.$store.commit('login', this.loginForm)
            this.$router.replace({path: '/index'})
          } else {
            this.$alert(successResponse.data.message, '提示', {
              confirmButtonText: '确定'})
          }
          this.loginLoading = false
          this.refreshVerifyCode()
        })
        .catch(failResponse => {
          this.$alert('登录失败', '提示', {
            confirmButtonText: '确定'})
          this.loginLoading = false
          this.refreshVerifyCode()
        })
    },
    register () {
      this.registerLoading = true
      this.axios.post('/register', this.loginForm)
        .then(successResponse => {
          if (successResponse.data.code === 200) {
            this.$alert('注册成功', '提示', {
              confirmButtonText: '确定'
            })
            this.$store.commit('login', this.loginForm)
            this.$router.replace({path: '/index'})
          } else {
            this.$alert(successResponse.data.message, '提示', {
              confirmButtonText: '确定'
            })
          }
          this.registerLoading = false
          this.refreshVerifyCode()
        })
        .catch(failResponse => {
          this.$alert('注册失败', '提示', {
            confirmButtonText: '确定'})
          this.registerLoading = false
          this.refreshVerifyCode()
        })
    },
    refreshVerifyCode () {
      this.imgSrc = 'http://localhost:8443/verifyCode?t=' + new Date().getTime()
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
