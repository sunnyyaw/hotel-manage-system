<template>
  <div>
  <el-page-header @back="goBack" :content="this.$route.params.setmealId == null ? '添加套餐' : '修改套餐'">
  </el-page-header>
    <el-form id="form" ref="form" :model="form">
      <el-form-item label="套餐名" :label-width="labelWidth"
      :rules="{required: true, message: '请输入套餐名', trigger:'blur'}">
        <el-input v-model="form.name"></el-input>
      </el-form-item>
      <el-form-item label="类别" :label-width="labelWidth"
      :rules="{required: true, message: '请选择类别', trigger:'blur'}">
        <el-select v-model="form.categoryId" placeholder="请选择类别">
            <el-option v-for="item in categories" :key="item.id" :label="item.name" :value="item.id"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="菜品" :label-width="labelWidth">
        <template slot-scope="scope">
          <div v-for="(item,index) in setmealDishList"
            :key="index">
            <span>{{ item.dishName }}</span>
            <span>{{ item.num }}</span>
          </div>
          <el-button @click="handleAddDish" type="primary">添加菜品</el-button>
        </template>
      </el-form-item>
      <el-form-item label="描述" :label-width="labelWidth">
        <el-input v-model="form.description" type="textarea" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="封面" :label-width="labelWidth">
        <img-upload @onUpload="uploadImg" ref="imgUpload"></img-upload>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="goBack" type="primary">取消</el-button>
      <el-button @click="onSubmit" type="primary">确定</el-button>
    </div>
    <setmeal-dish-form ref="setmealDishForm"></setmeal-dish-form>
  </div>
</template>
<script>
import ImgUpload from './ImgUpload'
import SetmealDishForm from './SetmealDishForm'
export default {
  name: 'SetmealAdd',
  components: {ImgUpload, SetmealDishForm},
  data () {
    return {
      form: {
        name: '',
        categoryId: '',
        description: '',
        cover: ''
      },
      setmealDishList: [],
      categories: [],
      labelWidth: '120px'
    }
  },
  created () {
    this.init()
  },
  methods: {
    goBack () {
      this.$router.replace('/setmeals')
    },
    init () {
      if (this.$route.params.setmealId != null) {
        this.getSetmeal()
      }
      this.getCategories()
    },
    getSetmeal () {
      this.$axios.get(`/setmeals/${this.$route.params.setmealId}`)
        .then(resp => {
          if (resp && resp.data.code === 200) {
            this.form = resp.data.data
            this.setmealDishList = this.form.dishList.map(dish => {
              return {
                dishId: dish.id,
                dishName: dish.dishName,
                num: this.form.setmeal_dishList.find(setmealDish =>
                  setmealDish.dishId === dish.id).num
              }
            })
            this.$refs.imgUpload.url = resp.data.data.cover
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
    getCategories () {
      this.$axios.get(`/categories?type=1`)
        .then(resp => {
          if (resp && resp.status === 200) {
            this.categories = resp.data.data
          }
        }).catch(error => {
          this.$message({
            type: 'error',
            message: `系统接口${error.response.status}错误`
          })
        })
    },
    onSubmit () {
      this.form.setmeal_dishList = this.setmealDishList
      this.form.setmeal_dishList.forEach(setmealDish => {
        setmealDish.setmealId = this.$route.params.setmealId
      })
      this.$axios({method: this.$route.params.setmealId == null ? 'POST' : 'PUT',
        url: '/setmeals',
        data: this.form})
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
    },
    handleAddDish () {
      this.$refs.setmealDishForm.loadDishes()
      this.$refs.setmealDishForm.setmealDishList = this.setmealDishList
      this.$refs.setmealDishForm.dialogFormVisible = true
    },
    uploadImg () {
      this.form.cover = this.$refs.imgUpload.url
    }
  }
}
</script>
<style scoped>
#form{
    height: 50%;
    width: 50%;
    text-align: left;
}
</style>
