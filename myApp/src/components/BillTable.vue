<template>
    <el-dialog
      title="所有账单"
      :visible.sync="dialogFormVisible"
      @close="clear"
      @open="loadBills">
        <el-table
        row-key="id"
        :data="bills"
        style="width: 100%"
        ref="expandTable"
        @expand-change="loadDetails">
          <el-table-column type="expand">
            <template slot-scope="props">
              <el-table
              row-key="dishId"
              :data="props.row.details"
              v-loading="!props.row.isLoad"
              style="width: 100%">
                <el-table-column
                    prop="dishId"
                    label="餐品编号">
                </el-table-column>
                <el-table-column
                    prop="category"
                    label="餐品类别">
                </el-table-column>
                <el-table-column
                    prop="dishName"
                    label="餐品名">
                </el-table-column>
                <el-table-column
                    prop="unitPrice"
                    label="单价">
                </el-table-column>
                <el-table-column
                    prop="num"
                    label="数量">
                </el-table-column>
                <el-table-column
                    prop="price"
                    label="总价">
                </el-table-column>
              </el-table>
              <p>总价格{{ props.row.totalPrice }}</p>
            </template>
          </el-table-column>
          <el-table-column
              prop="id"
              label="账单编号">
          </el-table-column>
          <el-table-column
              prop="genTime"
              label="生成时间">
          </el-table-column>
          <el-table-column
          fixed="right"
          label="操作"
          width="100">
          <template slot-scope="scope">
              <el-button @click="handleClick(scope.row)" type="text" size="small">查看明细</el-button>
              <el-button @click="deleteBill(scope.row.id)" type="text" size="small">删除</el-button>
          </template>
          </el-table-column>
        </el-table>
    </el-dialog>
</template>

<script>
export default {
  name: 'BillTable',
  data () {
    return {
      dialogFormVisible: false,
      bills: [],
      customerId: ''
    }
  },
  methods: {
    clear () {
      this.bills = []
      this.customerId = ''
    },
    loadBills () {
      var path = ''
      if (this.customerId === '') {
        path = '/bills'
      } else {
        path = '/customers/' + this.customerId + '/bills'
      }
      this.$axios.get(path)
        .then(resp => {
          if (resp && resp.status === 200) {
            resp.data.forEach(data => {
              data.details = []
              data.isLoad = false
            })
            this.bills = resp.data
          }
        }).catch(failresponse => {
          this.$alert('获取账单信息失败', '提示', {
            confirmButtonText: '确定'})
        })
    },
    loadDetails (row) {
      if (row.isLoad) return
      var path = ''
      if (row.id === '' || row.id === undefined) {
        path = '/bills_dishes'
      } else {
        path = '/bills/' + row.id + '/dishes'
      }
      this.$axios.get(path)
        .then(resp => {
          if (resp && resp.status === 200) {
            row.details = resp.data.data.dishDetail
            row.totalPrice = resp.data.data.totalPrice
            row.isLoad = true
          }
        }).catch(failresponse => {
          this.$alert('获取账单明细失败', '提示', {
            confirmButtonText: '确定'})
        })
    },
    handleClick (row) {
      this.$refs.expandTable.toggleRowExpansion(row)
    },
    deleteBill (id) {
      var path = '/bills/' + id
      this.$confirm('此操作将永久删除此账单，是否继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$axios.delete(path)
          .then(resp => {
            if (resp && resp.status === 200) {
              this.$message({
                type: 'success',
                message: '删除成功'
              })
              this.loadBills()
            }
          })
          .catch(failresponse => {
            this.$alert('删除失败', '提示', {
              confirmButtonText: '确定'
            })
          })
      })
    }
  }
}
</script>
<style scoped>
</style>
