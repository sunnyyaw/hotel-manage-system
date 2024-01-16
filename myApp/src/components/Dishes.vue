<template>
  <div>
    <el-row style="height: 840px;">
      <search-bar ref="searchBar"></search-bar>
        <el-card v-for="item in filteredDishes.slice((currentPage-1)*pageSize,currentPage*pageSize)"
        :key="item.id"
        style="width: 135px;margin-bottom: 20px;height: 233px;float: left;margin-right: 15px;"
        class="dish" dishStyle="padding:10px" shadow="hover" >
        <div class="cover" @click="editDish(item)">
            <img :src="item.cover" alt="封面">
        </div>
        <div class="info">
            <div class="name">
                <router-link :to="'/dishes/' + item.id + '/comments'">{{ item.dishName }}</router-link>
            </div>
            <i class="el-icon-edit" @click="editDish(item)"></i>
            <i class="el-icon-delete" @click="deleteDish(item.id)"></i>
            <router-link :to="'/dishes/' + item.id + '/comments'">
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
        <edit-form @onSubmit="loadDishes()" ref="edit"></edit-form>
        <order-form @onSubmit="dishes.forEach(dish => dish.num = 0)" ref="orderForm"></order-form>
        <p>总价格:{{ sumPrice }}</p>
        <p>菜品数:{{ sumNum }}</p>
        <el-button type="danger" @click="dishes.forEach(dish => dish.num = 0)">清除</el-button>
        <el-button type="primary" @click="orderDishes">下单</el-button>
    </el-row>
    <el-row>
        <el-pagination
            @current-change="handleCurrentChange"
            :current-page="currentPage"
            :page-size="pageSize"
            :total="dishes.length">
        </el-pagination>
    </el-row>
  </div>
</template>
<script>
import OrderForm from './OrderForm'
import EditForm from './EditForm'
import SearchBar from './SearchBar'
export default{
  name: 'Dishes',
  components: {EditForm, SearchBar, OrderForm},
  data () {
    return {
      dishes: [],
      cid: 0,
      bill: '',
      currentPage: 1,
      pageSize: 15
    }
  },
  mounted: function () {
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
    },
    filteredDishes: {
      get () {
        return this.dishes.filter(dish => this.cid === 0 || dish.categoryId === this.cid)
          .filter(dish => this.$refs.searchBar.keyword === '' || dish.dishName.includes(this.$refs.searchBar.keyword))
      },
      set (val) {}
    }
  },
  methods: {
    checkComment (dishId) {
      this.$router.replace({path: '/dishes/' + dishId + '/comments'})
    },
    loadDishes () {
      this.$axios.get('/dishes').then(resp => {
        if (resp && resp.status === 200) {
          resp.data.forEach(dish => {
            dish.num = 0
          })
          this.categorizedDishes = resp.data
          this.dishes = resp.data
        }
      })
    },
    editDish (item) {
      this.$refs.edit.dialogFormVisible = true
      this.$refs.edit.dish = item
    },
    handleCurrentChange: function (currentPage) {
      this.currentPage = currentPage
    },
    deleteDish (id) {
      this.$confirm('此操作将永久删除此餐品，是否继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$axios.delete('/dishes/' + id)
          .then(resp => {
            if (resp && resp.status === 200) {
              this.loadDishes()
            }
          })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        }
        )
      })
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
