<template>
  <div>
    <el-breadcrumb separator="/">
      <el-breadcrumb-item :to="{path:'/index'}">首页</el-breadcrumb-item>
      <el-breadcrumb-item :to="{path:'/dish'}">菜单</el-breadcrumb-item>
      <el-breadcrumb-item>餐品详情</el-breadcrumb-item>
    </el-breadcrumb>
    <el-descriptions class="margin-top" title="餐品信息" :column="3" border>
      <el-descriptions-item label="餐品名">{{ dish.dishName }}</el-descriptions-item>
      <el-descriptions-item label="类别">
        <span v-if="dish.category!=null">{{ dish.category.name }}</span>
        <span v-else>未分配</span>
      </el-descriptions-item>
      <el-descriptions-item label="描述">{{ dish.description }}</el-descriptions-item>
      <el-descriptions-item label="单价">{{ dish.unitPrice }}元</el-descriptions-item>
      <el-descriptions-item label="用户均分">
      <el-rate v-model="dish.averageScore" disabled show-score score-template="{value}分"></el-rate>
      </el-descriptions-item>
    </el-descriptions>
    <div v-for="item in dishComments" :key="item.id" v-loading="loading">
      <span v-if="item.user!=null">用户{{ item.user.username }}:</span>
      <span v-else>匿名用户:</span>
      {{ item.comment }}
      <el-rate v-model="item.rate" disabled show-score score-template="{value}"></el-rate>
      <span v-if="item.time!=null">时间:{{ item.time }}</span>
      <span v-else>时间:未知</span>
      <el-divider></el-divider>
    </div>
  <el-input type="textarea" :rows="2" placeholder="请输入内容" v-model="dishComment.comment"></el-input>
  <div class="block">
    <el-rate v-model="dishComment.rate"></el-rate>
  </div>
  <el-button v-loading="loading"
  @click="submitComment" type="primary" size="small">发布评论</el-button>
  </div>
</template>
<script>
export default {
  name: 'DishInfo',
  data () {
    return {
      dish: '',
      dishComments: [],
      dishComment: {
        rate: '',
        comment: ''
      },
      loading: true
    }
  },
  mounted: function () {
    this.loadComments()
  },
  methods: {
    submitComment () {
      this.axios.post('/dishes/' + this.$route.params.dishId + '/comments', this.dishComment)
        .then(successResponse => {
          if (successResponse.data.code === 200) {
            this.loadComments()
            this.dishComment = {
              rate: '',
              comment: ''
            }
            this.$message({
              type: 'success',
              message: successResponse.data.message
            })
          } else {
            this.$message({
              type: 'warning',
              message: successResponse.data.message
            })
          }
          this.loading = false
        })
        .catch(failResponse => {
          this.$message({
            type: 'warning',
            message: '上传失败'
          })
          this.loading = false
        })
    },
    loadComments () {
      this.axios.get('/dishes/' + this.$route.params.dishId)
        .then(successResponse => {
          if (successResponse.data.code === 200) {
            this.dish = successResponse.data.data
          } else {
            this.$message({
              type: 'warning',
              message: successResponse.data.message
            })
          }
          this.loading = false
        }).catch(failResponse => {
          this.$message({
            type: 'warning',
            message: '读取失败'
          })
          this.loading = false
        })
      this.axios.get('/dishes/' + this.$route.params.dishId + '/comments')
        .then(successResponse => {
          if (successResponse.data.code === 200) {
            this.dishComments = successResponse.data.data
          } else {
            this.$message({
              type: 'warning',
              message: successResponse.data.message
            })
          }
          this.loading = false
        })
        .catch(failResponse => {
          this.$message({
            type: 'warning',
            message: '读取失败'
          })
          this.loading = false
        })
    }
  }
}
</script>
<style scoped>
</style>
