<template>
    <div>
        <img src="/src/assets/png/logo2.png" alt="" class="logo"/>
    </div>
    <div class="SystemTitle_Chinese">
        糖尿病检测管理系统
    </div>
    <div class="SystemTitle_Eng">
        Diabetes detection and management system
    </div>
</template>

<script setup lang="ts">

</script>

<style scoped lang="css">
.logo {
  width: 40px;
  height: 40px;
  position: absolute;
  left: 10px;
  top: 10px;
  border-radius: 25%;
}
.SystemTitle_Chinese {
  font-size: 22px;
  font-family: "等线", monospace;
  font-weight: bold;
  color: #FFF;
  letter-spacing: 1px;
  position: absolute;
  left: 60px;
  top: -8px;
}

.SystemTitle_Eng {
  font-size: 13px;
  font-family: "Times New Roman", Times, serif;
  color: #FFF;
  letter-spacing: 0.1px;/* 字符间距 */
  position: absolute;
  left: 60px;
  top: 12px;
}
</style>