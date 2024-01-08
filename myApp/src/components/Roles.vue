<template>
  <div>
    <el-table
    :data="roles"
    style="width:100%;">
      <el-table-column
        prop="roleName"
        label="角色名">
      </el-table-column>
      <el-table-column
      label="权限">
      <template slot-scope="scope">
        <span v-for="item in scope.row.permissions" :key="item.id">{{ item.description }} </span>
      </template>
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
        <el-button @click="handleAdd" type="primary">添加角色</el-button>
    </el-row>
    <role-form @onSubmit="loadRoles" ref="roleForm"></role-form>
   </div>
</template>
<script>
import RoleForm from './RoleForm'
export default {
  name: 'Roles',
  components: {RoleForm},
  data () {
    return {
      roles: []
    }
  },
  mounted: function () {
    this.loadRoles()
  },
  methods: {
    handleEdit (row) {
      this.$refs.roleForm.roleForm = row
      this.$refs.roleForm.dialogVisible = true
    },
    handleAdd () {
      this.$refs.roleForm.clear()
      this.$refs.roleForm.dialogVisible = true
    },
    handleDelete (row) {
      this.$confirm('此操作将永久删除此角色，是否继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$axios.delete('/roles/' + row.id)
          .then(resp => {
            if (resp && resp.data.code === 200) {
              this.loadRoles()
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
    loadRoles () {
      this.$axios.get('/roles')
        .then(resp => {
          if (resp && resp.data.code === 200) {
            this.roles = resp.data.data
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
