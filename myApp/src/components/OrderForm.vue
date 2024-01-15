<template>
  <div>
    <el-dialog
    title="账单详情"
      :visible.sync="dialogFormVisible"
      @close="clear">
    <el-table
    row-key="id"
    :data="dishes"
    style="width: 100%">
    <el-table-column
        prop="id"
        label="餐品编号">
    </el-table-column>
    <el-table-column
        label="餐品类别">
      <template slot-scope="props">
        <span>{{ props.row.category.name }}</span>
      </template>
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
        label="总价">
      <template slot-scope="props">
        <span>{{ props.row.unitPrice * props.row.num }}</span>
      </template>
    </el-table-column>
    </el-table>
    <p>总价格{{ sumPrice }}</p>
    <el-button @click="clear" type="primary" >取消</el-button>
    <el-button @click="onSubmit" type="primary" >确定下单</el-button>
    </el-dialog>
  </div>
</template>
<script>
export default {
  name: 'OrderForm',
  data () {
    return {
      dishes: [],
      sumPrice: 0,
      dialogFormVisible: false
    }
  },
  methods: {
    clear () {
      this.dialogFormVisible = false
      this.dishes = []
    },
    onSubmit () {
      let orders = this.dishes.map(dish => {
        return {dishId: dish.id, num: dish.num}
      })
      this.$axios.post('/bills', {orders: orders}).then(resp => {
        if (resp && resp.status === 200) {
          this.$message({
            type: 'success',
            message: '下单成功'
          })
          this.$emit('onSubmit')
          this.dialogFormVisible = false
        }
      }).catch(failResponse => {
        this.$alert('下单失败', '提示', {
          confirmButtonText: '确定'
        })
      })
    }
  }
}
</script>
<style scoped>
</style>
