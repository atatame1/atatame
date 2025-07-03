<template>
    <trailing :colors="colors"/>
    <div class="top">
        <maintop />
    </div>

    <div class="menu">
        <functionMenu />
    </div>

    <div class="middle">
        <RouterView />
    </div>

</template>

<script setup lang="ts">
import { RouterView } from 'vue-router';
import maintop from './components/maintop.vue';
import functionMenu from './components/functionMenu.vue';
import trailing from './页面效果/trailing.vue';
import { ref } from 'vue';
 const colors=ref([
    'rgb(020, 054, 095)',
    'rgb(118, 162, 185)',
    'rgb(191, 217, 299)',
    'rgb(248, 242, 236)',
    'rgb(214, 079, 056)',
  ]);
</script>

<style scoped lang="css">
.top {
  background-color: rgb(118, 162, 185);
  height: 5vh;
  line-height: 60px;
}
.menu {
    background-color: #3c414a;
    color: #fff;
    height: 95vh;
    width: 220px;
    float: left;
    font-size: 20px;
    font-family: "等线", "宋体", "Times New Roman", Times, serif;
}
.middle{
    background-color: #fffcfc;
    float: left;
    width: calc(100% - 220px);
    height: 95vh;
}
</style>