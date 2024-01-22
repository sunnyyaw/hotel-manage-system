<template>
  <el-upload
  class="avatar-uploader"
  :action="axios.defaults.baseURL + '/covers'"
  :show-file-list="false"
  :with-credentials="true"
  :on-success="handleAvatarSuccess">
  <img v-if="url && url!=''"
  :src="url"
  class="avatar">
  <i v-else class="el-icon-plus avatar-uploader-icon"></i>
  </el-upload>
</template>
<script>
export default {
  name: 'ImgUpload',
  data () {
    return {
      fileList: [],
      url: ''
    }
  },
  methods: {
    handleAvatarSuccess (response, file, fileList) {
      this.url = response
      this.$emit('onUpload')
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
