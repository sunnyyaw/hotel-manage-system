<template>
    <div id="all">
        <nav-menu id="navMenu"></nav-menu>
        <router-view/>
    </div>
</template>
<script>
import NavMenu from '@/components/NavMenu.vue'
export default {
  name: 'Home',
  components: {NavMenu}
}
</script>
<style scoped>
#id {
  top: 0
}
#navMenu {
  top: 0
}
</style>
