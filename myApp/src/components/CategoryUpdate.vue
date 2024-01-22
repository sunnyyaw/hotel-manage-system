<template>
  <div>
  <el-page-header @back="goBack" content="修改类别">
  </el-page-header>
    <el-form id="form" ref="form" :model="form">
      <el-form-item label="类别名">
        <el-input v-model="form.name"></el-input>
      </el-form-item>
      <el-form-item label="类型">
        <el-radio-group v-model="form.type">
          <el-radio :label="0">菜品类别</el-radio>
          <el-radio :label="1">套餐类别</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="排序字段">
        <el-input v-model="form.sort"></el-input>
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
  name: 'CategoryUpdate',
  data () {
    return {
      form: {
        id: '',
        name: '',
        type: '',
        sort: ''
      }
    }
  },
  created () {
    this.init()
  },
  methods: {
    goBack () {
      this.$router.replace('/category')
    },
    init () {
      this.getCategories()
    },
    getCategories () {
      this.$axios.get(`/categories/${this.$route.params.categoryId}`)
        .then(resp => {
          if (resp && resp.data.code === 200) {
            this.form = resp.data.data
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
      this.$axios.post('/categories', this.form)
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
#form{
  width: 40%;
  height: 40%;
  text-align: left;
}
</style>
