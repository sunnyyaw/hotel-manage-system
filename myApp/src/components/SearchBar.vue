<template>
  <div>
    <el-input
    placeholder="输入关键词搜索"
    prefix-icon="el-icon-search"
    size="small"
    v-model="keyword">
    </el-input>
  </div>
</template>
<script>
export default {
  name: 'SearchBar',
  data () {
    return {
      keyword: ''
    }
  },
  methods: {
  }
}
</script>
<style scoped>
</style>
