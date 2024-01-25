<template>
  <div>
    <el-input
    placeholder="输入菜品/套餐名搜索"
    prefix-icon="el-icon-search"
    size="small"
    v-model="keyword"
    @blur="loadDishes"
    @keyup.enter.native="loadDishes">
    </el-input>
    <el-row>
      <el-col :span="22">
        <el-card v-for="item in dishes"
        :key="item.id"
        style="width: 135px;margin-bottom: 20px;height: 233px;float: left;margin-right: 15px;"
        class="dish" dishStyle="padding:10px" shadow="hover" >
        <div class="cover">
            <img v-if="item.cover && item.cover!=''" :src="item.cover" alt="封面">
            <span v-else>无封面</span>
        </div>
        <div class="info">
            <div class="name">
                <router-link :to="`/dishes/${item.id}/comments`">{{ item.dishName }}</router-link>
            </div>
            <router-link :to="`/dishes/${item.id}/comments`">
              <i class="el-icon-chat-dot-round"></i>
            </router-link>
            <div class="price">{{ item.unitPrice }}元</div>
        </div>
        <el-input-number
          class="elInputNumber"
          v-model="item.num"
          size="mini"
          :min="0"
          :max="999"
          label="描述文字"
        ></el-input-number>
        </el-card>
        <order-form @onSubmit="dishes.forEach(dish => dish.num = 0)" ref="orderForm"></order-form>
      </el-col>
      <el-col class="order" :span="2">
        <p>总价格:{{ sumPrice }}</p>
        <p>菜品数:{{ sumNum }}</p>
        <el-button type="warning" @click="dishes.forEach(dish => dish.num = 0)">清除</el-button>
        <el-button type="primary" @click="orderDishes">下单</el-button>
      </el-col>
    </el-row>
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
import OrderForm from './OrderForm'
export default{
  name: 'Dishes',
  components: {OrderForm},
  data () {
    return {
      dishes: [],
      category: null,
      bill: '',
      keyword: '',
      currentPage: 1,
      count: 0,
      pageSize: 10
    }
  },
  created () {
    this.loadDishes()
  },
  computed: {
    sumPrice () {
      let sum = 0
      this.dishes.forEach(dish => {
        sum += dish.unitPrice * dish.num
      })
      return sum
    },
    sumNum () {
      let sum = 0
      this.dishes.forEach(dish => {
        sum += dish.num
      })
      return sum
    }
  },
  methods: {
    loadDishes () {
      if (this.category == null || this.category.type === 3) {
        this.$axios.get(`/dishes?currentPage=${this.currentPage}&pageSize=${this.pageSize}&status=1&dishName=${this.keyword}`).then(resp => {
          if (resp && resp.status === 200) {
            resp.data.data.forEach(dish => {
              dish.num = 0
            })
            this.dishes = resp.data.data
            this.count = resp.data.count
          }
        })
      } else if (this.category.type === 4) {
        this.$axios.get(`/setmeals?currentPage=${this.currentPage}&pageSize=${this.pageSize}&status=1&name=${this.keyword}`).then(resp => {
          if (resp && resp.status === 200) {
            resp.data.data.forEach(setmeal => {
              setmeal.num = 0
              setmeal.dishName = setmeal.name
              let sumPrice = 0
              setmeal.dishList.forEach(dish => {
                let num = setmeal.setmeal_dishList.find(setmealDish =>
                  setmealDish.dishId === dish.id).num
                sumPrice += dish.unitPrice * num
              })
              setmeal.unitPrice = sumPrice
            })
            this.dishes = resp.data.data
            this.count = resp.data.count
          }
        })
      } else if (this.category.type === 0) {
        this.$axios.get(`/dishes?currentPage=${this.currentPage}&pageSize=${this.pageSize}&categoryId=${this.category.id}&status=1&dishName=${this.keyword}`).then(resp => {
          if (resp && resp.status === 200) {
            resp.data.data.forEach(dish => {
              dish.num = 0
            })
            this.dishes = resp.data.data
            this.count = resp.data.count
          }
        })
      } else {
        this.$axios.get(`/setmeals?currentPage=${this.currentPage}&pageSize=${this.pageSize}&categoryId=${this.category.id}&status=1&name=${this.keyword}`).then(resp => {
          if (resp && resp.status === 200) {
            resp.data.data.forEach(setmeal => {
              setmeal.num = 0
              setmeal.dishName = setmeal.name
              let sumPrice = 0
              setmeal.dishList.forEach(dish => {
                let num = setmeal.setmeal_dishList.find(setmealDish =>
                  setmealDish.dishId === dish.id).num
                sumPrice += dish.unitPrice * num
              })
              setmeal.unitPrice = sumPrice
            })
            this.dishes = resp.data.data
            this.count = resp.data.count
          }
        })
      }
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
    handleSelect () {
      this.currentPage = 1
      this.loadDishes()
    },
    orderDishes () {
      this.$refs.orderForm.sumPrice = this.sumPrice
      this.$refs.orderForm.dishes = this.dishes.filter(dish => dish.num !== 0)
      this.$refs.orderForm.dialogFormVisible = true
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
.order{
  position:relative;
  right: 0;
}
.elInputNumber{
  width:100%;
  height:100%;
}
</style>
