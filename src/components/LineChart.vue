<template>
  <div class="chart-container">
    <canvas ref="chartCanvas"></canvas>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted, watch, onBeforeUnmount } from 'vue'
import { Chart, type ChartData, type ChartOptions, registerables } from 'chart.js'

// 注册Chart.js组件
Chart.register(...registerables)

const props = defineProps<{
  chartData: ChartData
  options?: ChartOptions
}>()

const chartCanvas = ref<HTMLCanvasElement | null>(null)
const chart = ref<Chart | null>(null)

// 初始化图表
const initChart = () => {
  if (!chartCanvas.value) return
  
  // 销毁旧图表
  if (chart.value) {
    chart.value.destroy()
  }
  
  chart.value = new Chart(chartCanvas.value, {
    type: 'line',
    data: props.chartData,
    options: props.options || {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: {
          position: 'top',
        },
        tooltip: {
          mode: 'index',
          intersect: false,
        },
      },
      hover: {
        mode: 'nearest',
        intersect: true,
      },
      scales: {
        y: {
          beginAtZero: false,
        },
      },
    },
  })
}

// 监听数据变化
watch(
  () => props.chartData,
  () => {
    initChart()
  },
  { deep: true }
)

// 组件挂载时初始化图表
onMounted(() => {
  initChart()
})

// 组件卸载前销毁图表
onBeforeUnmount(() => {
  if (chart.value) {
    chart.value.destroy()
  }
})
</script>

<style scoped>
.chart-container {
  position: relative;
  height: 300px;
  width: 100%;
}
</style>