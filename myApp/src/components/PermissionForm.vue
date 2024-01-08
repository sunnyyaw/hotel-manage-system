<template>
  <el-dialog
  title="创建/修改权限"
  :visible.sync="dialogVisible"
  :before-close="clear">
    <el-form ref="form" :model="permissionForm">
      <el-form-item label="接口地址">
        <el-input v-model="permissionForm.url"></el-input>
      </el-form-item>
      <el-form-item label="权限描述">
        <el-input v-model="permissionForm.description"></el-input>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible=false" type="primary">取消</el-button>
      <el-button @click="onSubmit" type="primary">确定</el-button>
    </div>
  </el-dialog>
</template>
<script>
export default {
  name: 'PermissionForm',
  data () {
    return {
      dialogVisible: false,
      permissionForm: {
        id: '',
        url: '',
        description: ''
      }
    }
  },
  methods: {
    clear () {
      this.permissionForm = {
        id: '',
        url: '',
        description: ''
      }
      this.dialogVisible = false
    },
    onSubmit () {
      this.$axios.post('/permissions', this.permissionForm)
        .then(resp => {
          if (resp && resp.data.code === 200) {
            this.$message({
              type: 'success',
              message: resp.data.message
            })
            this.dialogVisible = false
            this.$emit('onSubmit')
          } else {
            this.$message({
              type: 'warning',
              message: resp.data.message
            })
          }
        }).catch(failResponse => {
          this.$message({
            type: 'error',
            message: '出错'
          })
        })
    }
  }
}
</script>
<style scoped>
</style>
