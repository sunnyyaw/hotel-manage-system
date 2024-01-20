<template>
  <div>
    <el-button @click="dialogFormVisible=true;form.type=0" type="primary">添加菜品类别</el-button>
    <el-button @click="dialogFormVisible=true;form.type=1" type="primary">添加套餐类别</el-button>
    <el-dialog
      title="添加类别"
      :visible.sync="dialogFormVisible"
      @close="clear">
      <el-form v-model="form" style="text-align: left;" ref="dataForm">
        <el-form-item label="类别名" label-width="120px" prop="name">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="类型" label-width="120px" prop="type">
          {{ form.type === 0 ? '菜品类别' : '套餐类别'}}
        </el-form-item>
        <el-form-item label="排序字段" label-width="120px" prop="sort">
          <el-input v-model="form.sort" autocomplete="off"></el-input>
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
export default {
  name: 'CategoryForm',
  data () {
    return {
      dialogFormVisible: false,
      form: {
        id: '',
        name: '',
        type: 0,
        sort: 0
      }
    }
  },
  methods: {
    clear () {
      this.form = {
        id: '',
        name: '',
        type: 0,
        sort: 0
      }
    },
    onSubmit () {
      this.$axios.post('/categories', this.form).then(resp => {
        if (resp && resp.data.code === 200) {
          this.dialogFormVisible = false
          this.$message({
            type: 'success',
            message: resp.data.message
          })
          this.$emit('onSubmit')
        } else {
          this.$message({
            type: 'warning',
            message: resp.data.message
          })
        }
      }).catch(error => {
        this.$message({
          type: 'danger',
          message: `系统接口${error.response.status}异常`
        })
      })
    }
  }
}
</script>
<style scoped>
</style>
