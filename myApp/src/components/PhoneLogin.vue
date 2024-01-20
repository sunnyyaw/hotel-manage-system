<template>
    <el-form class="login-container" label-position="left">
        <h3 class="login_title">短信登录</h3>
        <el-form-item>
            <el-input type="text" v-model="loginForm.phone"
            auto-complete="off" placeholder="手机号"></el-input>
        </el-form-item>
        <el-form-item>
            <el-input type="text" v-model="loginForm.verifyCode"
            auto-complete="off" placeholder="验证码"></el-input>
            <el-button type="primary" v-on:click="getVerifyCode">获取验证码</el-button>
        </el-form-item>
        <el-form-item style="width:100%">
            <el-button type="primary" v-on:click="login"
            v-loading="loginLoading" element-loading-spinner="el-icon-loading">注册并登录</el-button>
            <el-checkbox v-model="loginForm.rememberMe">记住我</el-checkbox>
            <el-link type="primary" href="http://localhost:8080/login">用户名密码登录</el-link>
        </el-form-item>
    </el-form>
</template>
<script>
export default {
  name: 'PhoneLogin',
  data () {
    return {
      loginForm: {
        username: '',
        password: '',
        phone: '',
        verifyCode: '',
        rememberMe: false
      },
      loginLoading: false,
      verifyCodeLoading: false
    }
  },
  methods: {
    login () {
      this.loginLoading = true
      this.axios.post('/validate', this.loginForm)
        .then(successResponse => {
          if (successResponse.data.code === 200) {
            this.$store.commit('login', this.loginForm)
            this.$router.replace({path: '/index'})
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
    getVerifyCode () {
      this.verifyCodeLoading = true
      this.axios.post('/sms', this.loginForm)
        .then(successResponse => {
          this.$alert(successResponse.data.message, '提示', {
            confirmButtonText: '确定'})
          this.verifyCodeLoading = false
        })
        .catch(failResponse => {
          this.$alert('发送失败', '提示', {
            confirmButtonText: '确定'})
          this.verifyCodeLoading = false
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
