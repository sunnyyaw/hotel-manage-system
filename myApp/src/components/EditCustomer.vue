<template>
  <div>
    <el-dialog
      title="添加/修改餐桌"
      :visible.sync="dialogFormVisible"
      @close="clear">
      <el-form v-model="form" style="text-align: left;" ref="dataForm">
        <el-form-item label="描述" label-width="120px" prop="description">
          <el-input v-model="form.description" autocomplete="off"></el-input>
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
  name: 'EditCustomer',
  data () {
    return {
      dialogFormVisible: false,
      form: {
        id: '',
        description: ''
      }
    }
  },
  methods: {
    clear () {
      this.form = {
        id: '',
        description: ''
      }
    },
    onSubmit () {
      this.$axios.post('/customers', this.form).then(resp => {
        if (resp && resp.status === 200) {
          this.$message({
            type: 'success',
            message: '添加成功'
          })
          this.dialogFormVisible = false
          this.$emit('onSubmit')
        }
      }).catch(failresponse => this.$alert(
        '失败', '提示', {
          confirmButtonText: '确定'}))
    }
  }
}
</script>
<style scoped>
</style>
