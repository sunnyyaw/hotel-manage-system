<template>
  <div>
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
            label="状态">
            <template slot-scope="props">
              <span >{{ props.row.status === 0 ? '未结算' : props.row.status === 1 ? '已结算' : '已撤销' }}</span>
          </template>
        </el-table-column>
        <el-table-column
        fixed="right"
        label="操作">
        <template slot-scope="scope">
            <el-button @click="handleClick(scope.row)" type="text" size="small">查看明细</el-button>
            <el-button v-if="scope.row.links.some(item => item.rel === 'complete')" @click="handleComplete(scope.row)"
            type="text" size="small">结算</el-button>
            <el-button v-if="scope.row.links.some(item => item.rel === 'cancel')" @click="handleCancel(scope.row)" type="text" size="small">撤销</el-button>
            <el-button @click="deleteBill(scope.row.id)" type="text" size="small">删除</el-button>
        </template>
        </el-table-column>
    </el-table>
    <el-pagination
    @size-change="handleSizeChange"
    @current-change="handleCurrentChange"
    :current-page="currentPage"
    :page-size="pageSize"
    :total="count"
    layout="total, sizes, prev, pager, next, jumper">
    </el-pagination>
  </div>
</template>
<script>
export default{
  name: 'Bills',
  data () {
    return {
      bills: [],
      currentPage: 1,
      totalPage: 1,
      pageSize: 10,
      count: 0
    }
  },
  mounted: function () {
    this.loadBills()
  },
  methods: {
    loadBills () {
      this.$axios.get(`/bills?currentPage=${this.currentPage}&pageSize=${this.pageSize}`).then(resp => {
        if (resp && resp.status === 200) {
          resp.data.data.forEach(bill => {
            bill.details = []
            bill.isLoad = false
          })
          this.bills = resp.data.data
          this.totalPage = resp.data.totalPage
          this.count = resp.data.count
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
      this.$confirm('此操作将永久删除此账单，是否继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$axios.delete(`/bills/${id}`)
          .then(resp => {
            if (resp && resp.data.code === 200) {
              this.$message({
                type: 'success',
                message: resp.data.message
              })
              this.loadBills()
            } else {
              this.$message({
                type: 'warning',
                message: resp.data.message
              })
            }
          })
          .catch(error => {
            this.$message({
              type: 'error',
              message: `系统接口${error.response.status}错误`
            })
          })
      })
    },
    handleClick (row) {
      this.$refs.expandTable.toggleRowExpansion(row)
    },
    handleComplete (row) {
      let path = row.links.find(link => link.rel === 'complete').href
      this.$axios.post(path)
        .then(resp => {
          if (resp && resp.status === 200) {
            this.$message({
              type: 'success',
              message: resp.data.message
            })
            this.loadBills()
          } else {
            this.$message({
              type: 'warning',
              message: resp.data.message
            })
          }
        }).catch(failresponse => {
          this.$message({
            type: 'danger',
            message: failresponse.data
          })
        })
    },
    handleCancel (row) {
      let path = row.links.find(link => link.rel === 'cancel').href
      this.$axios.post(path)
        .then(resp => {
          if (resp && resp.status === 200) {
            this.$message({
              type: 'success',
              message: resp.data.message
            })
            this.loadBills()
          } else {
            this.$message({
              type: 'warning',
              message: resp.data.message
            })
          }
        }).catch(failresponse => {
          this.$message({
            type: 'danger',
            message: failresponse.data
          })
        })
    },
    handleCurrentChange (currentPage) {
      this.currentPage = currentPage
      this.loadBills()
    },
    handleSizeChange (pageSize) {
      this.pageSize = pageSize
      this.loadBills()
    }
  }
}
</script>
<style scoped>
</style>
