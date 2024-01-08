<template>
  <div>
    <el-table
    :data="permissions"
    style="width:100%;">
      <el-table-column
        prop="id"
        label="权限编号">
      </el-table-column>
      <el-table-column
        prop="url"
        label="接口地址">
      </el-table-column>
      <el-table-column
        prop="description"
        label="权限描述">
      </el-table-column>
      <el-table-column
        fixed="right"
        label="操作"
        width="150">
        <template slot-scope="scope">
          <el-button-group>
            <el-button @click="handleEdit(scope.row)" size="small" type="warning" icon="el-icon-edit"></el-button>
            <el-button @click="handleDelete(scope.row)" size="small" type="danger" icon="el-icon-delete"></el-button>
          </el-button-group>
        </template>
      </el-table-column>
    </el-table>
    <el-row>
        <el-button @click="handleAdd" type="primary">添加权限</el-button>
    </el-row>
    <permission-form @onSubmit="loadPermissions" ref="permissionForm"></permission-form>
   </div>
</template>
<script>
import PermissionForm from './PermissionForm'
export default {
  name: 'Permissions',
  components: {PermissionForm},
  data () {
    return {
      permissions: []
    }
  },
  mounted: function () {
    this.loadPermissions()
  },
  methods: {
    handleEdit (row) {
      this.$refs.permissionForm.permissionForm = row
      this.$refs.permissionForm.dialogVisible = true
    },
    handleAdd () {
      this.$refs.permissionForm.clear()
      this.$refs.permissionForm.dialogVisible = true
    },
    handleDelete (row) {
      this.$confirm('此操作将永久删除此权限，是否继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$axios.delete('/permissions/' + row.id)
          .then(resp => {
            if (resp && resp.data.code === 200) {
              this.loadPermissions()
              this.$message({
                type: 'success',
                message: resp.data.message
              })
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
      )
    },
    loadPermissions () {
      this.$axios.get('/permissions')
        .then(resp => {
          if (resp && resp.data.code === 200) {
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
    }
  }
}
</script>
<style scoped>
</style>
