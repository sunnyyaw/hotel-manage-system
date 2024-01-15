<template>
  <div>
    <el-row style="height: 840px;">
      <search-bar ref="searchBar"></search-bar>
        <el-card style="width: 135px;margin-bottom: 20px;height: 233px;float: left;margin-right: 15px;"
        class="dish" dishStyle="padding:10px" shadow="hover"
        v-for="item in filteredCustomers.slice((currentPage-1)*pageSize,currentPage*pageSize)"
        :key="item.id">
        <div class="info">
            <p>{{ item.description }}</p>
            <i class="el-icon-delete" @click="deleteCustomer(item.id)"></i>
            <i class="el-icon-edit" @click="editCustomer(item)"></i>
            <el-button size="small" type="primary" @click="editBill(item.id)">添加账单</el-button>
            <el-button size="small" type="primary" @click="selectBill(item.id)">查看账单</el-button>
        </div>
        </el-card>
        <edit-customer @onSubmit="loadCustomers()" ref="edit"></edit-customer>
        <edit-bill ref="editBill"></edit-bill>
        <bill-table ref="billTable"></bill-table>
    </el-row>
    <el-row>
        <el-pagination
            @current-change="handleCurrentChange"
            :current-page="currentPage"
            :page-size="pageSize"
            :total="customers.length">
        </el-pagination>
    </el-row>
  </div>
</template>
<script>
import EditCustomer from './EditCustomer'
import SearchBar from './SearchBar'
import EditBill from './EditBill'
import BillTable from './BillTable'
export default{
  name: 'Customers',
  components: {EditCustomer, SearchBar, EditBill, BillTable},
  data () {
    return {
      customers: [],
      currentPage: 1,
      pageSize: 15
    }
  },
  computed: {
    filteredCustomers: {
      get () {
        return this.customers.filter(customer => this.$refs.searchBar.keyword === '' ||
          customer.id === Number(this.$refs.searchBar.keyword))
      },
      set (val) {}
    }
  },
  mounted: function () {
    this.loadCustomers()
  },
  methods: {
    loadCustomers () {
      this.$axios.get('/customers').then(resp => {
        if (resp && resp.status === 200) {
          this.customers = resp.data
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
    handleCurrentChange: function (currentPage) {
      this.currentPage = currentPage
    },
    deleteCustomer (id) {
      this.$confirm('此操作将永久删除此餐桌，是否继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$axios.delete('/customers/' + id)
          .then(resp => {
            if (resp && resp.status === 200) {
              this.$message({
                type: 'success',
                message: '删除成功'
              })
              this.loadCustomers()
            }
          })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        }
        )
      })
    }
  }
}
</script>
<style scoped>
img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
</style>
