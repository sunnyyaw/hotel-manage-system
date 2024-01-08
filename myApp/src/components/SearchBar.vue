<template>
  <div>
    <el-input @keyup.enter.native="searchClick"
    placeholder="通过餐品名搜索"
    prefix-icon="el-icon-search"
    size="small"
    v-model="keyword">
    </el-input>
    <el-button size="small"
    type="primary"
    icon="el-icon-search"
    @click="searchClick">搜索</el-button>
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
    searchClick () {
      this.$emit('onSearch')
    }
  }
}
</script>
<style scoped>
</style>
