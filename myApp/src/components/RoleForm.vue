<template>
  <el-dialog
  title="创建/修改角色"
  :visible.sync="dialogVisible"
  :before-close="clear"
  @open="loadPermissions">
    <el-form ref="form" :model="roleForm">
      <el-form-item label="角色名">
        <el-input v-model="roleForm.roleName"></el-input>
      </el-form-item>
      <el-form-item label="权限">
        <el-checkbox v-for="item in permissions" :key="item.id" :label="item.description"
        v-model="item.checked"></el-checkbox>
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
  name: 'RoleForm',
  data () {
    return {
      dialogVisible: false,
      roleForm: {
        id: '',
        roleName: '',
        permissions: []
      },
      permissions: []
    }
  },
  methods: {
    clear () {
      this.roleForm = {
        id: '',
        roleName: '',
        permissions: []
      }
      this.permissions = []
      this.dialogVisible = false
    },
    loadPermissions () {
      this.$axios.get('/permissions')
        .then(resp => {
          if (resp && resp.data.code === 200) {
            resp.data.data.forEach(permission => {
              let check = false
              this.roleForm.permissions.forEach(selectedPermission => {
                if (permission.id === selectedPermission.id) {
                  check = true
                }
              })
              permission.checked = check
            })
            this.permissions = resp.data.data
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
    },
    onSubmit () {
      this.roleForm.permissions = this.permissions.filter(perm => perm.checked)
      this.$axios.post('/roles', this.roleForm)
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
