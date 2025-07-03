<template>
    <div class="trailing" ref="trailing">
        <div 
            v-for="(star, index) in stars"
            :key="index"
            class="stars"
            :style="{
                left: star.x + 'px',
                top: star.y + 'px',
                backgroundColor:star.color,
                opacity:star.opacity,
                transform: `scale(${star.scale})`
            }"
        ></div>
    </div>
</template>

<script setup lang="ts">
import { onMounted, onBeforeUnmount, ref } from 'vue';
import { useEventListener } from '@vueuse/core';

const prop=defineProps(
    {
    colors:{
        type:Array<string>,
        default:['#a8d8ea', '#aa96da', '#fcbad3', '#ffffd2']
    }}
)

const stars = ref<Array<{ 
    x: number; 
    y: number;
    time:number; // 用于记录时间戳 
    color: string; // 可选颜色属性
    opacity: number; // 透明度属性
    scale: number; // 缩放比例属性
}>>([]);

const colors=prop.colors  // 可选颜色数组
let index=0
const lenth=colors.length

const trailing = ref<HTMLElement>();
const maxTime = 800; // 点的最大存在时间（毫秒）
let lastTime=0
const minTime = 30; // 最小时间间隔（毫秒）
const createStart = (e:MouseEvent) => {
    const currentTime = Date.now();
    if (currentTime - lastTime < minTime) {
        return; // 如果时间间隔小于最小时间间隔，则不创建新点
    }
    lastTime = currentTime;
    const x =e.clientX-10; // 减去6是为了使星星的中心点对齐鼠标位置
    const y = e.clientY-6;
    const color = colors[(index % lenth)];
    index++; // 选择颜色
    stars.value.push({ 
        x, 
        y, 
        time: currentTime,
        color:color,
        opacity: 1 ,
        scale: 1 
    }); // 添加新点
     // 更新最后创建点的时间
};

const updateStars = () => {
    const currentTime = Date.now();
    stars.value = stars.value.filter(star => {// filter拦截器 拦截return false 的元素 过滤掉超过最大存在时间的星星
        const timeElapsed = currentTime - star.time;
        if (timeElapsed > maxTime) {
            return false; // 超过最大存在时间，移除星星
        }

        // 更新星星的透明度和缩放比例，使其逐渐消失
        const progress = timeElapsed / maxTime;
        star.opacity = 1 - progress;
        star.scale = 1 - progress * 0.5; // 缩小一些以增加消失效果

        return true; // 保留星星
    });
};


let animationFrameId=0

const animate = () => {
    updateStars(); // 更新星星状态
    animationFrameId = requestAnimationFrame(animate); // 继续下一帧动画
};
onMounted(() => {
    if (trailing.value) {
        // 启动雨滴动画
        animate();
        useEventListener(window, 'mousemove', createStart); // 绑定鼠标移动事件
    }
});

onBeforeUnmount(() => {
    if (animationFrameId) {
        cancelAnimationFrame(animationFrameId); // 取消动画帧
    }
    removeEventListener( 'mousemove', createStart); // 解绑鼠标移动事件
});
</script>

<style scoped lang="css">
    .trailing {
        position: absolute;
        width: 100%;
        height: 100%;
        overflow: hidden;
        pointer-events: none; /* 确保不会阻止鼠标事件 */
        z-index: 1000; /* 确保在最上层 */
    }
    .stars{
        position: absolute;
        width: 20px;
        height: 20px;
        pointer-events: none; /* 确保不会阻止鼠标事件 */
        transition: transform 0.3s ease-out;
        opacity: 0.3s ease-out;
        clip-path: polygon(
        50% 0%,
        61% 35%,
        98% 35%,
        68% 57%,
        79% 91%,
        50% 70%,
        21% 91%,
        32% 57%,
        2% 35%,
        39% 35%
        );
    }
</style>