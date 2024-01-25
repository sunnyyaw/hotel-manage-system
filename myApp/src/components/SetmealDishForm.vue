<template>
  <div>
    <el-dialog
    title="添加套餐菜品"
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
        label="数量">
    <template slot-scope="scope">
      <el-input-number class="elInputNumber" v-model="scope.row.num"
      controls-position="right" :min="0" :max="999" @change="value => handleValueChange(value,scope.row.id,scope.row)"></el-input-number>
    </template>
    </el-table-column>
    <el-table-column
        label="总价">
      <template slot-scope="props">
        <span>{{ props.row.unitPrice * props.row.num }}</span>
      </template>
    </el-table-column>
    </el-table>
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
    <p>总价格{{ sumPrice }}</p>
    <el-button @click="clear" type="primary" >取消</el-button>
    <el-button @click="onSubmit" type="primary" >确定</el-button>
    </el-dialog>
  </div>
</template>
<script>
export default {
  name: 'SetmealDishForm',
  data () {
    return {
      dishes: [],
      setmealDishList: [],
      pageSize: 10,
      count: 0,
      currentPage: 1,
      dialogFormVisible: false
    }
  },
  computed: {
    sumPrice () {
      let sum = 0
      this.dishes.forEach(dish => {
        sum += dish.unitPrice * dish.num
      })
      return sum
    }
  },
  created () {
    this.loadDishes()
  },
  methods: {
    loadDishes () {
      this.$axios.get(`/dishes?currentPage=${this.currentPage}&pageSize=${this.pageSize}&status=1`)
        .then(resp => {
          if (resp && resp.status === 200) {
            resp.data.data.forEach(dish => {
              let setmealDish = this.setmealDishList.find(setmealDish =>
                setmealDish.dishId === dish.id)
              if (setmealDish) {
                dish.num = setmealDish.num
              } else {
                dish.num = 0
              }
            })
            this.dishes = resp.data.data
            this.count = resp.data.count
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
    },
    clear () {
      this.dialogFormVisible = false
    },
    onSubmit () {
      this.dialogFormVisible = false
    },
    handleCurrentChange (currentPage) {
      this.currentPage = currentPage
      this.loadDishes()
    },
    handleSizeChange (size) {
      this.pageSize = size
      this.currentPage = 1
      this.loadDishes()
    },
    handleValueChange (value, dishId, dish) {
      let index = this.setmealDishList.findIndex(setmealDish =>
        setmealDish.dishId === dishId)
      if (index === -1) {
        this.setmealDishList.push({dishId: dishId,
          dishName: dish.dishName,
          unitPrice: dish.unitPrice,
          num: value})
      } else if (value === 0) {
        this.setmealDishList.splice(index, 1)
      } else {
        this.setmealDishList[index].num = value
      }
    }
  }
}
</script>
<style scoped>
.elInputNumber{
  width:fit-content;
  height: fit-content;
}
</style>
