<template>
  <el-upload
  class="img-upload"
  ref="upload"
  action="#"
  :http-request="handleFileUpload"
  :on-preview="handlePreview"
  :on-remove="handleRemove"
  :before-remove="beforeRemove"
  multiple
  :limit="1"
  :on-exceed="handleExceed"
  :file-list="fileList">
  <el-button size="small" type="primary">点击上传</el-button>
  <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>
  </el-upload>
</template>
<script>
import axios from 'axios'

export default {
  name: 'ImgUpload',
  data () {
    return {
      fileList: [],
      url: ''
    }
  },
  methods: {
    handleRemove (file, fileList) {

    },
    handlePreview () {

    },
    handleExceed (files, fileList) {
      this.$message.warning(`当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length}个文件`)
    },
    beforeRemove (file, fileList) {
      return this.$confirm(`确定移除 ${file.name}?`)
    },
    handleFileUpload (file) {
      var filelist = new FormData()
      filelist.append('file', file.file)
      axios.post('/covers', filelist, {
        'Content-Type': 'multipart/form-data'
      }).then(resp => {
        if (resp.status === 200) {
          this.url = resp.data
          this.$emit('onUpload')
          this.$message.warning('上传成功')
        }
      }).catch(failresponse => {
        this.$alert('上传失败', '提示', {
          confirmButtonText: '确定'})
      })
    },
    clear () {
      this.$refs.upload.clearFiles()
    }
  }
}
</script>
<style scoped>
</style>
