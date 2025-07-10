<template>
  <div class="glucose-app">
    <el-container>
      <!-- 侧边栏 - 输入表单 -->
      <el-aside width="400px" class="input-sidebar">
        <div class="sidebar-header">
          <h2>血糖预测系统</h2>
          <el-tag type="primary">v1.0.0</el-tag>
        </div>
        
        <el-tabs type="border-card" class="form-tabs">
          <!-- 饮食记录表单 -->
          <el-tab-pane label="饮食记录">
            <el-form label-position="top" class="meal-form">
              <el-form-item label="食物名称">
                <el-input v-model="mealData.food" placeholder="输入食物名称" clearable />
              </el-form-item>
              
              <el-form-item label="食物量 (份)">
                <el-input-number
                  v-model="mealData.amount"
                  :min="0.1"
                  :max="10"
                  :step="0.1"
                  controls-position="right"
                  style="width: 100%"
                />
              </el-form-item>
              
              <el-form-item label="用餐时间">
                <el-date-picker
                  v-model="mealData.meal_time"
                  type="datetime"
                  placeholder="选择用餐时间"
                  style="width: 100%"
                />
              </el-form-item>
            </el-form>
          </el-tab-pane>
          
          <!-- 预测参数表单 -->
          <el-tab-pane label="预测参数">
            <el-form label-position="top" class="prediction-form">
              <el-form-item label="年龄">
                <el-input-number
                  v-model="formData.data.age"
                  :min="18"
                  :max="100"
                  controls-position="right"
                  style="width: 100%"
                />
              </el-form-item>
              
              <el-form-item label="性别">
                <el-select v-model="formData.data.gender" placeholder="选择性别" style="width: 100%">
                  <el-option label="男" value="男" />
                  <el-option label="女" value="女" />
                </el-select>
              </el-form-item>
              
              <el-form-item label="当前血糖 (mg/dL)">
                <el-input-number
                  v-model="formData.data.current_glucose"
                  :min="50"
                  :max="300"
                  :step="0.1"
                  controls-position="right"
                  style="width: 100%"
                />
              </el-form-item>
              
              <el-form-item label="食物量 (份)">
                <el-input-number
                  v-model="formData.data.food_amount"
                  :min="0"
                  :max="10"
                  :step="0.1"
                  controls-position="right"
                  style="width: 100%"
                />
              </el-form-item>
              
              <el-form-item label="预测时长 (小时)">
                <el-input-number
                  v-model="formData.data.prediction_hours"
                  :min="1"
                  :max="24"
                  :step="0.5"
                  controls-position="right"
                  style="width: 100%"
                />
              </el-form-item>
            </el-form>
          </el-tab-pane>
        </el-tabs>
        
        <div class="form-actions">
          <el-button 
            type="primary" 
            size="large" 
            @click="runTest"
            :loading="isLoading"
            style="width: 100%"
          >
            {{ isLoading ? '预测中...' : '开始预测' }}
          </el-button>
          
          <el-button 
            @click="resetForm"
            size="large"
            style="width: 100%; margin-top: 10px"
          >
            重置表单
          </el-button>
        </div>
        
        <el-alert
          v-if="errorMessage"
          :title="errorMessage"
          type="error"
          show-icon
          class="error-alert"
        />
      </el-aside>
      
      <!-- 主内容区 - 图表和结果 -->
      <el-main class="result-main">
        <el-card v-if="apiResponse.predictions?.length" class="result-card">
          <template #header>
            <div class="card-header">
              <h3>血糖预测结果</h3>
              <div class="model-info">
                <el-tag size="small">模型版本: {{ apiResponse.model_version }}</el-tag>
                <el-tag size="small" type="info">数据源: {{ apiResponse.food_db_source }}</el-tag>
              </div>
            </div>
          </template>
          
          <!-- 主图表 -->
          <div class="chart-container">
            <VChart class="chart" :option="chartOption" autoresize />
          </div>
          
          <!-- 预测数据表格 -->
          <el-table 
            :data="apiResponse.predictions" 
            stripe
            style="width: 100%; margin-top: 20px"
          >
            <el-table-column prop="time" label="时间" width="150">
              <template #default="scope">
                {{ formatTime(scope.row.time) }}
              </template>
            </el-table-column>
            
            <el-table-column prop="glucose" label="血糖值" width="120">
              <template #default="scope">
                <span :class="{
                  'high-glucose': scope.row.glucose > 180,
                  'normal-glucose': scope.row.glucose <= 180
                }">
                  {{ scope.row.glucose.toFixed(1) }} mg/dL
                </span>
              </template>
            </el-table-column>
            
            <el-table-column prop="trend" label="趋势" width="120">
              <template #default="scope">
                <el-tag :type="getTrendTagType(scope.row.trend)">
                  {{ getTrendIcon(scope.row.trend) }} {{ scope.row.trend }}
                </el-tag>
              </template>
            </el-table-column>
            
            <el-table-column prop="confidence" label="置信度">
              <template #default="scope">
                <el-progress
                  :percentage="scope.row.confidence * 100"
                  :color="getConfidenceColor(scope.row.confidence)"
                  :show-text="false"
                />
                <span class="confidence-value">{{ (scope.row.confidence * 100).toFixed(0) }}%</span>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
        
        <!-- 空状态 -->
        <el-card v-else class="empty-card">
          <el-empty description="输入参数并点击开始预测获取结果">
            <el-button type="primary" @click="runTest" :disabled="isLoading">
              开始预测
            </el-button>
          </el-empty>
        </el-card>
      </el-main>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { LineChart } from 'echarts/charts'
import {
  GridComponent,
  TooltipComponent,
  TitleComponent,
  LegendComponent,
  DataZoomComponent
} from 'echarts/components'
import VChart from 'vue-echarts'
import axios from 'axios'
import { ElMessage } from 'element-plus'

// 初始化 ECharts 组件
use([
  CanvasRenderer,
  LineChart,
  GridComponent,
  TooltipComponent,
  TitleComponent,
  LegendComponent,
  DataZoomComponent
])

// API配置
const API_BASE_URL = 'http://10.70.5.94:8000'
const HEADERS = { 'Content-Type': 'application/json' }

// 表单数据
const formData = ref({
  data: {
    age: 35,
    gender: '女',
    current_glucose: 120.5,
    food_amount: 1.5,
    prediction_hours: 3.0
  }
})

// 饮食记录数据
const mealData = ref({
  food: '苹果',
  amount: 1.5,
  meal_time: new Date().toISOString()
})

// API响应数据
const apiResponse = ref({
  predictions: [],
  model_version: '',
  food_db_source: ''
})

// 状态管理
const isLoading = ref(false)
const errorMessage = ref('')

// 图表配置
const chartOption = ref({
  title: {
    text: '血糖变化趋势预测',
    left: 'center',
    textStyle: {
      color: '#333',
      fontSize: 16,
      fontWeight: 'normal'
    }
  },
  tooltip: {
    trigger: 'axis',
    formatter: (params: any) => {
      const data = params[0].data
      return `
        <div style="font-weight:bold;margin-bottom:5px">${formatTime(data.time)}</div>
        <div style="display:flex;justify-content:space-between">
          <span>血糖值:</span>
          <span style="font-weight:bold">${data.glucose.toFixed(1)} mg/dL</span>
        </div>
        <div style="display:flex;justify-content:space-between">
          <span>趋势:</span>
          <span style="color:${getTrendColor(data.trend)}">${getTrendIcon(data.trend)} ${data.trend}</span>
        </div>
        <div style="display:flex;justify-content:space-between">
          <span>置信度:</span>
          <span style="color:${getConfidenceColor(data.confidence)}">${(data.confidence * 100).toFixed(0)}%</span>
        </div>
      `
    }
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    containLabel: true
  },
  xAxis: {
    type: 'category',
    boundaryGap: false,
    data: [],
    axisLabel: {
      formatter: (value: string) => formatTime(value)
    }
  },
  yAxis: {
    type: 'value',
    name: '血糖值 (mg/dL)',
    min: (value: { min: number }) => Math.max(0, Math.floor(value.min - 10)),
    max: (value: { max: number }) => Math.ceil(value.max + 10),
    axisLine: {
      show: true
    },
    splitLine: {
      lineStyle: {
        type: 'dashed'
      }
    }
  },
  series: [
    {
      name: '血糖值',
      type: 'line',
      smooth: true,
      symbol: 'circle',
      symbolSize: 8,
      itemStyle: {
        color: '#36a2eb'
      },
      lineStyle: {
        width: 3,
        color: '#36a2eb'
      },
      areaStyle: {
        color: {
          type: 'linear',
          x: 0,
          y: 0,
          x2: 0,
          y2: 1,
          colorStops: [{
            offset: 0,
            color: 'rgba(54, 162, 235, 0.3)'
          }, {
            offset: 1,
            color: 'rgba(54, 162, 235, 0.1)'
          }]
        }
      },
      data: []
    }
  ],
  dataZoom: [
    {
      type: 'inside',
      start: 0,
      end: 100
    },
    {
      start: 0,
      end: 100,
      handleSize: '80%',
      handleStyle: {
        color: '#fff',
        shadowBlur: 3,
        shadowColor: 'rgba(0, 0, 0, 0.6)',
        shadowOffsetX: 2,
        shadowOffsetY: 2
      }
    }
  ]
})

// 辅助函数
const formatTime = (timeString: string) => {
  return new Date(timeString).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
}

const getTrendIcon = (trend: string) => {
  const icons: Record<string, string> = { '上升': '↑', '下降': '↓', '平稳': '→' }
  return icons[trend] || ''
}

const getTrendColor = (trend: string) => {
  const colors: Record<string, string> = { 
    '上升': '#f56c6c', 
    '下降': '#67c23a', 
    '平稳': '#e6a23c' 
  }
  return colors[trend] || '#909399'
}

const getTrendTagType = (trend: string) => {
  const types: Record<string, string> = { 
    '上升': 'danger', 
    '下降': 'success', 
    '平稳': 'warning' 
  }
  return types[trend] || 'info'
}

const getConfidenceColor = (confidence: number) => {
  const percent = confidence * 100
  if (percent >= 80) return '#67C23A'
  if (percent >= 60) return '#E6A23C'
  return '#F56C6C'
}

// 更新图表数据
const updateChart = () => {
  if (!apiResponse.value.predictions?.length) return

  const times = apiResponse.value.predictions.map(p => p.time)
  const values = apiResponse.value.predictions.map(p => ({
    time: p.time,
    glucose: p.glucose,
    trend: p.trend,
    confidence: p.confidence
  }))

  chartOption.value.xAxis.data = times
  chartOption.value.series[0].data = values
}

// API调用
const runTest = async () => {
  isLoading.value = true
  errorMessage.value = ''

  try {
    // 1. 记录饮食
    await axios.post(
      `${API_BASE_URL}/glucose/record-meal`,
      mealData.value,
      { headers: HEADERS }
    )

    // 2. 血糖预测
    const response = await axios.post(
      `${API_BASE_URL}/glucose/predict`,
      formData.value,
      { headers: HEADERS }
    )

    apiResponse.value = response.data
    updateChart()
    ElMessage.success('预测成功')

  } catch (error: any) {
    console.error('API调用失败:', error)
    errorMessage.value = error.response?.data?.message || error.message
    ElMessage.error('预测失败: ' + errorMessage.value)
  } finally {
    isLoading.value = false
  }
}

// 重置表单
const resetForm = () => {
  formData.value = {
    data: {
      age: 35,
      gender: '女',
      current_glucose: 120.5,
      food_amount: 1.5,
      prediction_hours: 3.0
    }
  }
  mealData.value = {
    food: '苹果',
    amount: 1.5,
    meal_time: new Date().toISOString()
  }
  apiResponse.value = {
    predictions: [],
    model_version: '',
    food_db_source: ''
  }
  errorMessage.value = ''
  ElMessage.info('已重置表单')
}
</script>

<style scoped>
.glucose-app {
  height: 100vh;
  display: flex;
  background-color: #f5f7fa;
}

.el-container {
  height: 100%;
}

.input-sidebar {
  height: 800px;
  background-color: #fff;
  padding: 20px;
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.05);
  display: flex;
  flex-direction: column;
}

.sidebar-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #ebeef5;
}

.sidebar-header h2 {
  margin: 0;
  font-size: 18px;
  color: #303133;
}

.form-tabs {
  flex: 1;
  margin-bottom: 20px;
  border-radius: 4px;
  overflow: hidden;
}

.form-actions {
  margin-top: 20px;
}

.error-alert {
  margin-top: 20px;
}

.result-main {
  padding: 20px;
  background-color: #f5f7fa;
}

.result-card {
  height: 80%;
  display: flex;
  flex-direction: column;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header h3 {
  margin: 0;
  font-size: 16px;
}

.model-info {
  display: flex;
  gap: 8px;
}

.chart-container {
  height: 400px;
  margin-top: 10px;
}

.chart {
  width: 100%;
  height: 100%;
}

.empty-card {
  height: 80%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.high-glucose {
  color: #f56c6c;
  font-weight: bold;
}

.normal-glucose {
  color: #67c23a;
}

.confidence-value {
  display: inline-block;
  margin-left: 10px;
  font-size: 14px;
  color: #606266;
}

:deep(.el-tabs__content) {
  padding: 20px;
}

:deep(.el-form-item__label) {
  font-weight: 500;
  color: #606266;
}
</style>