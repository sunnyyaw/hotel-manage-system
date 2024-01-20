<template>
  <div>
    <el-row>
      <el-col :span=12>
        <el-input
        placeholder="输入餐桌号搜索"
        v-model="input"
        clearable
        @blur="handleSearch">
        <i slot="prefix" class="el-input__icon el-icon-search"></i>
        </el-input>
      </el-col>
      <el-col :span=12>
        <el-button type="primary" @click="()=>{this.$refs.edit.dialogFormVisible = true}">添加餐桌</el-button>
      </el-col>
    </el-row>
    <el-table
    :data="customers"
    style="width:100%;">
      <el-table-column
      prop="description"
      label="描述">
      </el-table-column>
      <el-table-column
        fixed="right"
        label="操作"
        width="200">
        <template slot-scope="scope">
          <el-button @click="selectBill(scope.row.id)" size="small" type="text">查看账单明细</el-button>
          <el-button @click="editCustomer(scope.row)" size="small" type="text">编辑</el-button>
          <el-button @click="deleteCustomer(scope.row.id)" size="small" type="text">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <edit-customer @onSubmit="loadCustomers" ref="edit"></edit-customer>
    <bill-table ref="billTable"></bill-table>
    <el-row>
        <el-pagination
            @current-change="handleCurrentChange"
            @size-change="handleSizeChange"
            :current-page="currentPage"
            :page-size="pageSize"
            :total="count"
            layout="total, sizes, prev, pager, next, jumper">
        </el-pagination>
    </el-row>
  </div>
</template>
<script>
import EditCustomer from './EditCustomer'
import EditBill from './EditBill'
import BillTable from './BillTable'
export default{
  name: 'Customers',
  components: {EditCustomer, EditBill, BillTable},
  data () {
    return {
      customers: [],
      currentPage: 1,
      pageSize: 10,
      count: 0,
      input: ''
    }
  },
  created () {
    this.loadCustomers()
  },
  methods: {
    loadCustomers () {
      this.$axios.get(`/customers?currentPage=${this.currentPage}&pageSize=${this.pageSize}&description=${this.input}`).then(resp => {
        if (resp && resp.status === 200) {
          this.customers = resp.data.data
          this.count = resp.data.count
        }
      })
    },
    editCustomer (item) {
      this.$refs.edit.dialogFormVisible = true
      this.$refs.edit.form = {
        id: item.id,
        description: item.description
      }
    },
    editBill (id) {
      this.$refs.editBill.dialogFormVisible = true
      this.$refs.editBill.billForm.customerId = id
    },
    selectBill (id) {
      this.$refs.billTable.dialogFormVisible = true
      this.$refs.billTable.customerId = id
    },
    handleCurrentChange (currentPage) {
      this.currentPage = currentPage
      this.loadCustomers()
    },
    handleSizeChange (size) {
      this.pageSize = size
      this.currentPage = 1
      this.loadCustomers()
    },
    handleSearch () {
      this.currentPage = 1
      this.loadCustomers()
    },
    deleteCustomer (id) {
      this.$confirm('此操作将永久删除此餐桌，是否继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$axios.delete(`/customers/${id}`)
          .then(resp => {
            if (resp && resp.data.code === 200) {
              this.$message({
                type: 'success',
                message: resp.data.message
              })
              this.loadCustomers()
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
      }).catch(() => {
      })
    }
  }
}
</script>
<style scoped>
</style>
