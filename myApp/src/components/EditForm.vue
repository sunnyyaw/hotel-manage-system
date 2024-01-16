<template>
  <div>
    <el-button type="primary" @click="dialogFormVisible = true">添加菜品</el-button>
    <el-dialog
      title="添加/修改餐品"
      :visible.sync="dialogFormVisible"
      @close="clear"
      @open="loadCategories">
      <el-form v-model="form" style="text-align: left;" ref="dataForm">
        <el-form-item label="餐品名" :label-width="formLabelWidth" prop="dishName">
          <el-input v-model="form.dishName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="类别" :label-width="formLabelWidth" prop="categoryId">
          <el-select v-model="form.categoryId" placeholder="请选择类别">
            <el-option v-for="item in categories" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="描述" :label-width="formLabelWidth" prop="description">
          <el-input v-model="form.description" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="单价" :label-width="formLabelWidth" prop="unitPrice">
          <el-input v-model="form.unitPrice" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="封面" :label-width="formLabelWidth" prop="cover">
          <el-input v-model="form.cover" autocomplete="off" placeholder="图片 URL"></el-input>
          <img-upload @onUpload="uploadImg" ref="imgUpload"></img-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="onSubmit">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import ImgUpload from './ImgUpload'
export default {
  name: 'EditForm',
  components: {ImgUpload},
  data () {
    return {
      dialogFormVisible: false,
      form: {
        id: '',
        cover: '',
        dishName: '',
        categoryId: '',
        description: '',
        unitPrice: ''
      },
      categories: [],
      formLabelWidth: '120px'
    }
  },
  methods: {
    clear () {
      this.form = {
        id: '',
        cover: '',
        dishName: '',
        categoryId: '',
        description: '',
        unitPrice: ''
      }
      this.$refs.imgUpload.clear()
    },
    onSubmit () {
      this.$axios.post('/dishes', {
        id: this.form.id,
        cover: this.form.cover,
        dishName: this.form.dishName,
        categoryId: this.form.categoryId,
        description: this.form.description,
        unitPrice: this.form.unitPrice
      }).then(resp => {
        if (resp && resp.status === 200) {
          this.dialogFormVisible = false
          this.$emit('onSubmit')
        }
      })
    },
    loadCategories () {
      this.$axios.get('/categories')
        .then(resp => {
          if (resp && resp.status === 200) {
            this.categories = resp.data
          }
        })
    },
    uploadImg () {
      this.form.cover = this.$refs.imgUpload.url
    }
  }
}
</script>
<style scoped>
</style>
