<template>
  <div>
  <el-page-header @back="goBack" :content="this.$route.params.dishId == null ? '添加菜品' : '修改菜品'">
  </el-page-header>
    <el-form id="form" ref="form" :model="form">
      <el-form-item label="菜品名" :label-width="labelWidth"
      :rules="{required: true, message: '请输入菜品名', trigger:'blur'}">
        <el-input v-model="form.dishName"></el-input>
      </el-form-item>
      <el-form-item label="类别" :label-width="labelWidth"
      :rules="{required: true, message: '请选择类别', trigger:'blur'}">
        <el-select v-model="form.categoryId" placeholder="请选择类别">
            <el-option v-for="item in categories" :key="item.id" :label="item.name" :value="item.id"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="口味" :label-width="labelWidth">
        <div v-for="(dishFlavor,index) in form.dishFlavorList" :key="index">
          <td>
            <el-select v-model="dishFlavor.name" placeholder="请选择口味">
            <el-option value="甜味"></el-option>
            <el-option value="温度"></el-option>
            <el-option value="辣味"></el-option>
            <el-option value="忌口"></el-option>
            </el-select>
          </td>
          <td>
            <el-input v-model="dishFlavor.value"></el-input>
          </td>
          <td>
            <el-button @click="form.dishFlavorList.splice(index,1)">删除</el-button>
          </td>
        </div>
        <el-button type="primary" size="small" @click="addDishFlavor">添加口味</el-button>
      </el-form-item>
      <el-form-item label="描述" :label-width="labelWidth">
        <el-input v-model="form.description" type="textarea" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="单价" :label-width="labelWidth"
      :rules="{required: true, message: '请输入单价', trigger:'blur'}">
        <el-input v-model="form.unitPrice" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="封面" :label-width="labelWidth">
        <img-upload @onUpload="uploadImg" ref="imgUpload"></img-upload>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="goBack" type="primary">取消</el-button>
      <el-button @click="onSubmit" type="primary">确定</el-button>
    </div>
  </div>
</template>
<script>
import ImgUpload from './ImgUpload'
export default {
  name: 'DishAdd',
  components: {ImgUpload},
  data () {
    return {
      form: {
        dishName: '',
        categoryId: '',
        description: '',
        unitPrice: '',
        cover: '',
        dishFlavorList: []
      },
      categories: [],
      labelWidth: '120px'
    }
  },
  created () {
    this.init()
  },
  methods: {
    goBack () {
      this.$router.replace('/dishes')
    },
    init () {
      if (this.$route.params.dishId != null) {
        this.getDish()
      }
      this.getCategories()
    },
    getDish () {
      this.$axios.get(`/dishes/${this.$route.params.dishId}`)
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
    getCategories () {
      this.$axios.get(`/categories`)
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
      this.$axios.post('/dishes', this.form)
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
    uploadImg () {
      this.form.cover = this.$refs.imgUpload.url
    },
    addDishFlavor () {
      this.form.dishFlavorList.push({name: '', value: ''})
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
