<template>
    <el-container>
        <el-main>
            <customers class="customers-area" ref="customersArea"></customers>
        </el-main>
    </el-container>
</template>
<script>
import Customers from './Customers'
export default {
  name: 'CustomerIndex',
  components: {Customers}
}
</script>
<style scoped>
</style>
