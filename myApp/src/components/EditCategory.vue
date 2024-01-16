<template>
  <div>
    <el-button type="primary" @click="dialogFormVisible = true">添加类别</el-button>
    <el-dialog
      title="添加/修改类别"
      :visible.sync="dialogFormVisible"
      @close="clear">
      <el-form v-model="form" style="text-align: left;" ref="dataForm">
        <el-form-item label="类别名" label-width="120px" prop="name">
          <el-input v-model="form.name" autocomplete="off"></el-input>
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
  name: 'EditCategory',
  data () {
    return {
      dialogFormVisible: false,
      form: {
        id: '',
        name: ''
      }
    }
  },
  methods: {
    clear () {
      this.form = {
        id: '',
        name: ''
      }
    },
    onSubmit () {
      this.$axios.post('/categories', {
        id: this.form.id,
        name: this.form.name
      }).then(resp => {
        if (resp && resp.status === 200) {
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
