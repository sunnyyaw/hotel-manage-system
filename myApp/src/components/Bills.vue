<template>
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
            label="餐桌">
          <template slot-scope="props">
            <span v-if="props.row.customer!=null">{{ props.row.customer.description }}</span>
            <span v-else>未知</span>
          </template>
        </el-table-column>
        <el-table-column
            label="用户">
          <template slot-scope="props">
            <span v-if="props.row.user!=null">{{ props.row.user.username }}</span>
            <span v-else>匿名用户</span>
          </template>
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
</template>
<script>
export default{
  name: 'Bills',
  data () {
    return {
      bills: []
    }
  },
  mounted: function () {
    this.loadBills()
  },
  methods: {
    loadBills () {
      this.$axios.get('/bills').then(resp => {
        if (resp && resp.status === 200) {
          resp.data.forEach(bill => {
            bill.details = []
            bill.isLoad = false
          })
          this.bills = resp.data
        }
      }).catch(failresponse => {
        this.$message({
          type: 'warning',
          message: '获取账单失败'
        })
      })
    },
    loadDetails (row) {
      if (row.isLoad === true) return
      let path = (row.id === '' || row.id === undefined)
        ? '/bills_dishes' : '/bills/' + row.id + '/dishes'
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
    deleteBill (id) {
      let path = '/bills/' + id
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
    },
    handleClick (row) {
      this.$refs.expandTable.toggleRowExpansion(row)
    }
  }
}
</script>
<style scoped>
</style>
