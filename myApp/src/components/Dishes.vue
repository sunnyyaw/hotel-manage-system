<template>
  <div>
    <el-row>
      <search-bar ref="searchBar"></search-bar>
        <el-card v-for="item in dishes"
        :key="item.id"
        style="width: 135px;margin-bottom: 20px;height: 233px;float: left;margin-right: 15px;"
        class="dish" dishStyle="padding:10px" shadow="hover" >
        <div class="cover">
            <img :src="item.cover" alt="封面">
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
          v-model="item.num"
          size="mini"
          :min="0"
          :max="999"
          label="描述文字"
        ></el-input-number>
        </el-card>
        <order-form @onSubmit="dishes.forEach(dish => dish.num = 0)" ref="orderForm"></order-form>
        <p>总价格:{{ sumPrice }}</p>
        <p>菜品数:{{ sumNum }}</p>
        <el-button type="danger" @click="dishes.forEach(dish => dish.num = 0)">清除</el-button>
        <el-button type="primary" @click="orderDishes">下单</el-button>
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
import SearchBar from './SearchBar'
export default{
  name: 'Dishes',
  components: {SearchBar, OrderForm},
  data () {
    return {
      dishes: [],
      cid: '',
      bill: '',
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
      this.$axios.get(`/dishes?currentPage=${this.currentPage}&pageSize=${this.pageSize}&categoryId=${this.cid}`).then(resp => {
        if (resp && resp.status === 200) {
          resp.data.data.forEach(dish => {
            dish.num = 0
          })
          this.dishes = resp.data.data
          this.count = resp.data.count
        }
      })
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
</style>
