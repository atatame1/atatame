<template>
  <particle />
  <trailing />
  <img class="home" ref="home"/>
  <submit class="submit" />
  <span 
    class="rain" 
    v-for="(text, index) in rainTexts" 
    :key="index" 
    :style="{ color: currentColor, left: text.left, top: text.top, fontSize: text.fontSize }"
  >
    {{ text.content }}
  </span>
</template>

<script setup lang="ts">

import { onMounted, ref, onBeforeUnmount, type Ref } from 'vue';
import submit from './components/submit.vue';
import useBackgroundMove from './hooks/useBackgroundMove';
import particle from './页面效果/particle.vue';
import trailing from './页面效果/trailing.vue';

const home = ref<HTMLElement>();
const { bindEvent,unBindEvent } = useBackgroundMove(home as Ref<HTMLElement>);

const colors = ['#a8e6cf', '#dcedc1', '#ffd3b6', '#ffaaa5'];
const currentColor = ref(colors[0]);
let intervalId: ReturnType<typeof setInterval>;

// const rainTexts = [
//   { content: '胰\n岛\n素\n终\n将\n落\n下', left: '85%', top: '20%', fontSize: '50px' },
//   { content: '我\n为\n糖\n尿\n病\n哀\n哭', left: '90%', top: '10%', fontSize: '40px' },
//   // { content:'点击屏幕进入',left:'45%',top:'90%',fontSize:'40px'}
// ];

const changeColor = () => {
  const currentIndex = colors.indexOf(currentColor.value);
  const nextIndex = (currentIndex + 1) % colors.length;
  currentColor.value = colors[nextIndex];
};


onMounted(() => {
  bindEvent();
  intervalId = setInterval(changeColor, 1000); // 每1000毫秒改变一次颜色
});

onBeforeUnmount(() => {
  unBindEvent;
  clearInterval(intervalId);    
});
</script>

<style scoped lang="css">
.home {
  z-index: -1;
  height: 102%; 
  width: 102%;
  background-image: url("@/assets/png/屏幕截图 2024-05-27 153617.png");
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  position: absolute;
  transition: transform 0.1s ease-out; 
  margin: 0 auto;
  left: -1%;
  top: -1%;
  user-select: none;
}
.submit {
  /* background-color: rgba(196, 86, 86, 0.664); */
  background-color: rgba(248, 242, 236, 0.664);
  color: aliceblue;
}
.rain {
  font-family: 'kaiti', sans-serif;
  position: absolute;
  white-space: pre; /* 保持换行 */
  transition: color 2s ease-in-out; /* 添加颜色过渡效果 */
  user-select: none; /* 禁止选中 */
}
</style>