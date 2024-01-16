<template>
  <div>
    <el-button type="primary" @click="dialogFormVisible = true">添加菜品</el-button>
    <el-dialog
      title="添加/修改餐品"
      :visible.sync="dialogFormVisible"
      @close="clear"
      @open="loadCategories">
      <el-form v-model="dish" style="text-align: left;" ref="dataForm">
        <el-form-item label="餐品名" :label-width="formLabelWidth" prop="dishName">
          <el-input v-model="dish.dishName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="类别" :label-width="formLabelWidth" prop="categoryId">
          <el-select v-model="dish.categoryId" placeholder="请选择类别">
            <el-option v-for="item in categories" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="描述" :label-width="formLabelWidth" prop="description">
          <el-input v-model="dish.description" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="单价" :label-width="formLabelWidth" prop="unitPrice">
          <el-input v-model="dish.unitPrice" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="封面" :label-width="formLabelWidth" prop="cover">
          <el-input v-model="dish.cover" autocomplete="off" placeholder="图片 URL"></el-input>
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
      dish: {
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
      this.dish = ''
      this.$refs.imgUpload.clear()
    },
    onSubmit () {
      this.$axios.post('/dishes', this.dish).then(resp => {
        if (resp && resp.status === 200) {
          this.$message({
            type: 'success',
            message: '保存成功'
          })
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
      this.dish.cover = this.$refs.imgUpload.url
    }
  }
}
</script>
<style scoped>
</style>
