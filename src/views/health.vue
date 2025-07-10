<template>
  <div class="health-container">
    <el-row :gutter="20">
      <!-- 数据输入表单 -->
      <el-col :span="12">
        <el-card class="input-card">
          <template #header>
            <div class="card-header">
              <span>健康数据录入</span>
            </div>
          </template>
          
          <el-form 
            :model="healthForm" 
            :rules="rules" 
            ref="healthFormRef" 
            label-position="top"
            @submit.prevent="submitForm"
            style="margin-left: 50px;"
          >
            <el-form-item label="体重 (kg)" prop="weight">
              <el-input-number 
                v-model="healthForm.weight" 
                :min="30" 
                :max="200" 
                :precision="1" 
                controls-position="right"
              />
            </el-form-item>
            
            <el-form-item label="身高 (cm)" prop="height">
              <el-input-number 
                v-model="healthForm.height" 
                :min="100" 
                :max="250" 
                :precision="1" 
                controls-position="right"
              />
            </el-form-item>
            
            <el-form-item label="血压 (mmHg)" prop="blood_pressure">
              <el-input-number 
                v-model="healthForm.bloodPressure" 
                :min="50" 
                :max="250" 
                controls-position="right"
              />
            </el-form-item>
            
            <el-form-item label="血糖 (mg/dL)" prop="glucose">
              <el-input-number 
                v-model="healthForm.glucose" 
                :min="20" 
                :max="500" 
                controls-position="right"
              />
            </el-form-item>
            
            <el-form-item label="胰岛素 (unit)" prop="insulin">
              <el-input-number 
                v-model="healthForm.insulin" 
                :min="1" 
                :max="100" 
                controls-position="right"
              />
            </el-form-item>
            
            <el-form-item>
              <el-button type="primary" @click="submitForm">提交数据</el-button>
              <el-button @click="resetForm">重置</el-button>
            </el-form-item>
          </el-form>
          
          <!-- 健康指标计算结果 -->
          <div class="health-indicators" v-if="bmi">
            <el-divider />
            <h3>健康指标</h3>
            <div class="indicator-item">
              <span class="indicator-label">BMI指数:</span>
              <span class="indicator-value">{{ bmi.toFixed(1) }}</span>
              <span class="indicator-level" :class="getBmiLevelClass(bmi)">
                {{ getBmiLevel(bmi) }}
              </span>
            </div>
            <div class="indicator-item">
              <span class="indicator-label">血压状态:</span>
              <span class="indicator-value">{{ healthForm.bloodPressure }} mmHg</span>
              <span class="indicator-level" :class="getBloodPressureLevelClass(healthForm.bloodPressure)">
                {{ getBloodPressureLevel(healthForm.bloodPressure) }}
              </span>
            </div>
            <div class="indicator-item">
              <span class="indicator-label">血糖状态:</span>
              <span class="indicator-value">{{ healthForm.glucose }} mg/dL</span>
              <span class="indicator-level" :class="getGlucoseLevelClass(healthForm.glucose)">
                {{ getGlucoseLevel(healthForm.glucose) }}
              </span>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <!-- 数据图表展示 -->
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>健康数据趋势</span>
              <el-button type="primary" size="small" @click="fetchData">刷新数据</el-button>
            </div>
          </template>
          
          <el-tabs v-model="activeChart" type="card">
            <el-tab-pane label="体重" name="weight">
              <line-chart :chart-data="weightChartData" />
            </el-tab-pane>
            <el-tab-pane label="BMI" name="bmi">
              <line-chart :chart-data="bmiChartData" />
            </el-tab-pane>
            <el-tab-pane label="血压" name="bloodPressure">
              <line-chart :chart-data="bloodPressureChartData" />
            </el-tab-pane>
            <el-tab-pane label="血糖" name="glucose">
              <line-chart :chart-data="glucoseChartData" />
            </el-tab-pane>
            <el-tab-pane label="胰岛素" name="insulin">
              <line-chart :chart-data="insulinChartData" />
            </el-tab-pane>
          </el-tabs>
          
          <!-- 历史数据表格 -->
          <el-divider />
          <h3>历史记录</h3>
          <el-table :data="sortedHealthData" border style="width: 100%">
            <el-table-column prop="formattedDate" label="日期" width="120" sortable />
            <el-table-column prop="weight" label="体重(kg)" width="100" sortable />
            <el-table-column prop="height" label="身高(cm)" width="100" sortable />
            <el-table-column label="BMI" width="100" sortable>
              <template #default="{row}">
                {{ calculateBmi(row.weight, row.height).toFixed(1) }}
              </template>
            </el-table-column>
            <el-table-column prop="bloodPressure" label="血压(mmHg)" width="120" sortable />
            <el-table-column prop="glucose" label="血糖(mg/dL)" width="120" sortable />
            <el-table-column prop="insulin" label="胰岛素(unit)" width="120" sortable />
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script lang="ts" setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { storeData, getData, type intputHeath,outputHeath } from '../api/health'
import LineChart from '../components/LineChart.vue'

// 健康数据表单
const healthForm = ref<intputHeath>({
  weight: 60,
  height: 170,
  bloodPressure: 120,
  glucose: 100,
  insulin: 10
})

// 表单验证规则
const rules = ref<FormRules>({
  weight: [
    { required: true, message: '请输入体重', trigger: 'blur' },
    { type: 'number', min: 30, message: '体重不能小于30kg', trigger: 'blur' },
    { type: 'number', max: 200, message: '体重不能大于200kg', trigger: 'blur' }
  ],
  height: [
    { required: true, message: '请输入身高', trigger: 'blur' },
    { type: 'number', min: 100, message: '身高不能小于100cm', trigger: 'blur' },
    { type: 'number', max: 250, message: '身高不能大于250cm', trigger: 'blur' }
  ],
  bloodPressure: [
    { required: true, message: '请输入血压', trigger: 'blur' },
    { type: 'number', min: 50, message: '血压不能小于50mmHg', trigger: 'blur' },
    { type: 'number', max: 250, message: '血压不能大于250mmHg', trigger: 'blur' }
  ],
  glucose: [
    { required: true, message: '请输入血糖', trigger: 'blur' },
    { type: 'number', min: 20, message: '血糖不能小于20mg/dL', trigger: 'blur' },
    { type: 'number', max: 500, message: '血糖不能大于500mg/dL', trigger: 'blur' }
  ],
  insulin: [
    { required: true, message: '请输入胰岛素', trigger: 'blur' },
    { type: 'number', min: 1, message: '胰岛素不能小于1unit', trigger: 'blur' },
    { type: 'number', max: 100, message: '胰岛素不能大于100unit', trigger: 'blur' }
  ]
})

// 表单引用
const healthFormRef = ref<FormInstance>()

// 当前激活的图表
const activeChart = ref('weight')

// 健康数据
const healthData = ref<Array<outputHeath>>([])

// 格式化后的健康数据（按时间排序）
const sortedHealthData = computed(() => {
  return [...healthData.value]
    .sort((a, b) => new Date(b.createdTime).getTime() - new Date(a.createdTime).getTime())
    .map(item => ({
      ...item,
      formattedDate: formatDate(item.createdTime)
    }))
})

// 日期格式化
const formatDate = (date: Date | string) => {
  const d = new Date(date)
  return `${d.getFullYear()}-${(d.getMonth() + 1).toString().padStart(2, '0')}-${d.getDate().toString().padStart(2, '0')}`
}

// 计算BMI
const calculateBmi = (weight: number, height: number): number => {
  return weight / Math.pow(height / 100, 2)
}

// 当前表单的BMI
const bmi = computed(() => {
  return calculateBmi(healthForm.value.weight, healthForm.value.height)
})

// BMI等级
const getBmiLevel = (bmi: number): string => {
  if (bmi < 18.5) return '偏瘦'
  if (bmi < 24) return '正常'
  if (bmi < 28) return '超重'
  return '肥胖'
}

// BMI等级样式
const getBmiLevelClass = (bmi: number): string => {
  if (bmi < 18.5) return 'warning'
  if (bmi < 24) return 'success'
  if (bmi < 28) return 'warning'
  return 'danger'
}

// 血压等级
const getBloodPressureLevel = (bp: number): string => {
  if (bp < 90) return '低血压'
  if (bp < 120) return '正常'
  if (bp < 140) return '正常高值'
  return '高血压'
}

// 血压等级样式
const getBloodPressureLevelClass = (bp: number): string => {
  if (bp < 90) return 'warning'
  if (bp < 120) return 'success'
  if (bp < 140) return 'warning'
  return 'danger'
}

// 血糖等级
const getGlucoseLevel = (glucose: number): string => {
  if (glucose < 70) return '低血糖'
  if (glucose < 100) return '正常'
  if (glucose < 126) return '偏高'
  return '高血糖'
}

// 血糖等级样式
const getGlucoseLevelClass = (glucose: number): string => {
  if (glucose < 70) return 'warning'
  if (glucose < 100) return 'success'
  if (glucose < 126) return 'warning'
  return 'danger'
}

// 提交表单
const submitForm = async () => {
  try {
    await healthFormRef.value?.validate()

    console.log("表单为：",healthForm.value)
    const success = await storeData(healthForm.value)
    if (success) {
      ElMessage.success('数据保存成功')
      await fetchData()
    }
  } catch (error) {
    console.error('表单验证失败:', error)
  }
}

// 重置表单
const resetForm = () => {
  healthFormRef.value?.resetFields()
}

// 获取健康数据
const fetchData = async () => {
  try {
    const data = await getData()
    console.log('数据为：',data)
    if (data) {
      healthData.value = data
    }
  } catch (error) {
    console.error('获取数据失败:', error)
  }
}

// 图表数据
const chartLabels = computed(() => {
  return sortedHealthData.value.map(item => item.formattedDate)
})

const weightChartData = computed(() => ({
  labels: chartLabels.value,
  datasets: [{
    label: '体重 (kg)',
    data: sortedHealthData.value.map(item => item.weight),
    borderColor: '#409EFF',
    backgroundColor: 'rgba(64, 158, 255, 0.1)',
    tension: 0.1
  }]
}))

const bmiChartData = computed(() => ({
  labels: chartLabels.value,
  datasets: [{
    label: 'BMI',
    data: sortedHealthData.value.map(item => calculateBmi(item.weight, item.height)),
    borderColor: '#67C23A',
    backgroundColor: 'rgba(103, 194, 58, 0.1)',
    tension: 0.1
  }]
}))

const bloodPressureChartData = computed(() => ({
  labels: chartLabels.value,
  datasets: [{
    label: '血压 (mmHg)',
    data: sortedHealthData.value.map(item => item.bloodPressure),
    borderColor: '#E6A23C',
    backgroundColor: 'rgba(230, 162, 60, 0.1)',
    tension: 0.1
  }]
}))

const glucoseChartData = computed(() => ({
  labels: chartLabels.value,
  datasets: [{
    label: '血糖 (mg/dL)',
    data: sortedHealthData.value.map(item => item.glucose),
    borderColor: '#F56C6C',
    backgroundColor: 'rgba(245, 108, 108, 0.1)',
    tension: 0.1
  }]
}))

const insulinChartData = computed(() => ({
  labels: chartLabels.value,
  datasets: [{
    label: '胰岛素 (unit)',
    data: sortedHealthData.value.map(item => item.insulin),
    borderColor: '#909399',
    backgroundColor: 'rgba(144, 147, 153, 0.1)',
    tension: 0.1
  }]
}))

// 初始化时获取数据
onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.health-container {
  max-width: 1500px;
  margin: auto;
  padding: 20px;
  margin-top: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.input-card {
  height: 100%;
}

.health-indicators {
  margin-top: 20px;
}

.indicator-item {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.indicator-label {
  width: 100px;
  font-weight: bold;
}

.indicator-value {
  width: 120px;
}

.indicator-level {
  margin-left: 10px;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
}

.success {
  background-color: #f0f9eb;
  color: #67c23a;
}

.warning {
  background-color: #fdf6ec;
  color: #e6a23c;
}

.danger {
  background-color: #fef0f0;
  color: #f56c6c;
}
</style>