
<template>
  <div style="text-align:center;">
    <img src="../assets/pic_404.jpg" height="420" width="650" alt=""/>
    <div>
      <el-button type="primary" style="width:150px; height:60px; font-size:20px" @click="goHome">返回主页</el-button>
    </div>
  </div>
</template>
  
<script setup>

  const goHome = () => {
    location.href = '/manager/home'
  }
</script>

<style scoped>
img {
  max-width: 90%;
  height: auto;
}

@media (max-width: 768px) {
  div > div > .el-button {
    width: 120px !important;
    height: 45px !important;
    font-size: 16px !important;
  }
}
</style>