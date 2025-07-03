<template>
    <div class="rain-container" ref="Container">
        <div
            v-for="(drop,index) in drops"
            :key="index"
            class="drop"
            :style="{
                left: drop.x+'px',
                top: drop.y+'px',
                width: (parseInt(drop.size))+ 'px',
                height: (parseInt(drop.size) * 2)+'px',
                backgroundColor: drop.color,
                opacity: drop.opacity,
                transform: `rotate(${drop.rotation}deg)`                
            }"
        ></div>
    </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount } from 'vue';

const drops = ref<Array<{
    x: number;
    y: number;
    size: string;
    color: string;
    opacity: string ;
    rotation: string;
    speed: number;
}>>([]);

const maxDrops = 100; // 最大雨滴数量
const dropSpeed=0.5 // 雨滴下落速度
const color = '#f6416c'; // 雨滴颜色
const Container = ref<HTMLElement>();
const createDrop=()=> {
    if(!Container.value) return;
    const ContainerWidth = Container.value.clientWidth;
    const ContainerHeight = Container.value.clientHeight;
    const x = Math.random() * ContainerWidth ;
    const y= Math.random() * ContainerHeight;
    const size = Math.random() * 5 + 3 + 'px'; // 雨滴大小
    const opacity=0.5 + Math.random() * 0.5; // 雨滴透明度
    const rotation = -Math.random()*15; // 雨滴旋转角度

    drops.value.push({
        x,
        y,
        size,
        color: color,
        opacity: opacity.toString(),
        rotation: rotation.toString(),
        speed: dropSpeed+Math.random() * 2 // 雨滴下落速度
    });
    if (drops.value.length > maxDrops) {
        drops.value.shift(); // 删除数组第一个元素 保持最大雨滴数量
    }
}

const updataDrops=()=>{
    if(!Container.value) return;
    const ContainerHeight = Container.value.clientHeight;
    const ContainerWidth = Container.value.clientWidth;
    drops.value.forEach((drop,index)=>{
        drops.value[index].y+=drop.speed+Math.random(); // 更新雨滴位置
        drops.value[index].x+=Math.random(); // 添加随机水平漂移
        if(drop.y>ContainerHeight|| drop.x > ContainerWidth || drop.x < 0) {
            drops.value[index].y = 0.5-0.5*Math.random()*ContainerHeight; // 如果雨滴超出容器高度，重置位置
            drops.value[index].x = Math.random() * (Container.value?.clientWidth ?? 0); // 随机x位置
        }
    })
}

let animationFrameId=0

const anime=()=>{
    // createDrop(); // 创建新的雨滴
    updataDrops(); // 更新雨滴位置
    animationFrameId = requestAnimationFrame(anime); // 递归调用
}

onMounted(() => {
    if (Container.value) {
        for (let i = 0; i < maxDrops; i++) {
            createDrop(); // 初始化雨滴
        }
        anime(); // 启动雨滴动画
    }
});

onBeforeUnmount(() => {
    cancelAnimationFrame(animationFrameId); // 清除动画帧
});
</script>

<style scoped lang="css">
.rain-container {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    overflow: hidden;
    z-index: 1000;
    pointer-events: none; /* 确保不会阻止鼠标事件 */
}
.drop {
    position: absolute;
    border-radius: 50% 50% 50% 50% /60% 60% 40% 40%;
    opacity: 0.5;
    pointer-events: none; /* 确保不会阻止鼠标事件 */
    transition:transfrom 1ms ease-out;
    will-change: transfrom; /* 提升性能 */
}
</style>