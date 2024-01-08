<template>
  <div>
    <el-dialog
      title="添加/修改账单"
      :visible.sync="dialogFormVisible"
      @close="clear"
      @open="loadData">
      <el-table
      row-key="dishId"
      :data="dishes"
      v-loading="loading"
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
            fixed="right"
            label="数量">
          <template slot-scope="props">
            <el-input-number v-model="props.row.num"
            :min="0" :max="1000" label="下单数量"
            size="small">
            </el-input-number>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="onSubmit">下单</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
export default {
  name: 'EditBill',
  components: {},
  data () {
    return {
      dialogFormVisible: false,
      loading: true,
      billForm: {
        customerId: ''
      },
      dishes: []
    }
  },
  methods: {
    clear () {
      this.billForm = {
        customerId: ''
      }
      this.dishes = []
      this.loading = true
    },
    onSubmit () {
      var orders = []
      this.dishes.forEach(dish => {
        const dishId = dish.dishId
        const num = dish.num
        if (num === 0) return
        orders.push({dishId: dishId, num: num})
      })
      this.$axios.post('/bills', {
        customerId: this.billForm.customerId,
        orders: orders
      }).then(resp => {
        if (resp && resp.status === 200) {
          this.$message({
            type: 'success',
            message: '下单成功'
          })
          this.dialogFormVisible = false
        }
      }).catch(failResponse => {
        this.$alert('下单失败', '提示', {
          confirmButtonText: '确定'
        })
      })
    },
    loadData () {
      this.$axios.get('/dishDetails')
        .then(resp => {
          if (resp && resp.status === 200) {
            resp.data.forEach(data => {
              data.num = 0
            })
            this.dishes = resp.data
            this.loading = false
          }
        })
    }
  }
}
</script>
<style scoped>
</style>
