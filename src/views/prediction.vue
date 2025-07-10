<template>
  <div class="diabetes-assessment-app">
    <div class="header">
      <h1>糖尿病风险预测分析</h1>
      <p>基于多项生理指标和家族史的综合风险评估</p>
    </div>


    
    <div class="container">
            <!-- 结果展示 -->
      <div class="result-container" v-if="result">
        <h2>风险评估结果</h2>
        <div class="result-card" :class="result.risk_level">
          <div class="result-header">
            <h3>糖尿病风险评估</h3>
            <span class="risk-badge">{{ result.risk_level }}</span>
          </div>

          <div class="result-metrics">
            <div class="metric">
              <div class="metric-value">{{ (result.probability * 100).toFixed(1) }}%</div>
              <div class="metric-label">患病概率</div>
            </div>
            <div class="metric">
              <div class="metric-value">{{ result.bmi }}</div>
              <div class="metric-label">BMI指数</div>
            </div>
            <div class="metric">
              <div class="metric-value">{{ result.prediction === 1 ? '阳性' : '阴性' }}</div>
              <div class="metric-label">预测结果</div>
            </div>
          </div>

          <div class="result-details">
            <div class="detail-item">
              <label>皮肤褶厚度估算:</label>
              <span>{{ result.skin_thickness }} mm</span>
            </div>
            <div class="detail-item">
              <label>糖尿病遗传系数:</label>
              <span>{{ result.dpf.toFixed(2) }}</span>
            </div>
          </div>

          <div class="medical-advice">
            <h4>医疗建议</h4>
            <pre>{{ result.medical_advice }}</pre>
          </div>
        </div>
      </div>
      <!-- 输入表单 -->
      <div class="form-container">
        <h2>患者信息录入</h2>
        <form @submit.prevent="submitForm">
          <div class="form-grid">
            <!-- 基础信息 -->
            <div class="form-section">
              <h3>基础信息</h3>
              <div class="form-group">
                <label for="age">年龄(岁)*</label>
                <input type="number" id="age" v-model.number="patientData.age" min="1" step="1" required>
              </div>
              <div class="form-group">
                <label for="weight">体重(kg)*</label>
                <input type="number" id="weight" v-model.number="patientData.weight" min="0.1" step="0.1" required>
              </div>
              <div class="form-group">
                <label for="height">身高(m)*</label>
                <input type="number" id="height" v-model.number="patientData.height" min="0.1" step="0.01" required>
              </div>
              <div class="form-group">
                <label for="waist_circumference">腰围(cm)*</label>
                <input type="number" id="waist_circumference" v-model.number="patientData.waist_circumference" min="1"
                  step="1" required>
              </div>
              <div class="form-group">
                <label for="pregnancies">怀孕次数</label>
                <input type="number" id="pregnancies" v-model.number="patientData.pregnancies" min="0" step="1">
              </div>
            </div>

            <!-- 生理指标 -->
            <div class="form-section">
              <h3>生理指标</h3>
              <div class="form-group">
                <label for="glucose">血糖(mg/dL)*</label>
                <input type="number" id="glucose" v-model.number="patientData.glucose" min="1" step="1" required>
              </div>
              <div class="form-group">
                <label for="blood_pressure">舒张压(mm Hg)*</label>
                <input type="number" id="blood_pressure" v-model.number="patientData.blood_pressure" min="1" step="1"
                  required>
              </div>
              <div class="form-group">
                <label for="insulin">血清胰岛素(μU/mL)*</label>
                <input type="number" id="insulin" v-model.number="patientData.insulin" min="1" step="1" required>
              </div>
            </div>

            <!-- 家族史 -->
            <div class="form-section">
              <h3>家族史信息</h3>
              <div class="form-group">
                <label for="parents">父母患病人数</label>
                <input type="number" id="parents" v-model.number="patientData.family_history.parents" min="0" max="2"
                  step="1">
              </div>
              <div class="form-group">
                <label for="siblings">兄弟姐妹患病人数</label>
                <input type="number" id="siblings" v-model.number="patientData.family_history.siblings" min="0"
                  step="1">
              </div>
              <div class="form-group">
                <label for="grandparents">(外)祖父母患病人数</label>
                <input type="number" id="grandparents" v-model.number="patientData.family_history.grandparents" min="0"
                  max="4" step="1">
              </div>
              <div class="form-group">
                <label for="aunts_uncles">叔伯姑姨舅患病人数</label>
                <input type="number" id="aunts_uncles" v-model.number="patientData.family_history.aunts_uncles" min="0"
                  step="1">
              </div>
              <div class="form-group checkbox-group">
                <label>
                  <input type="checkbox" v-model="patientData.family_history.has_gestational" :true-value=1
                    :false-value=0>
                  是否有妊娠糖尿病史
                </label>
              </div>
            </div>
          </div>

          <div class="form-actions">
            <button type="submit" :disabled="isLoading">
              {{ isLoading ? '评估中...' : '开始评估' }}
            </button>
            <button type="button" @click="resetForm">重置表单</button>
          </div>
        </form>
      </div>

      <!-- 加载状态 -->
      <div class="loading-container" v-if="isLoading">
        <div class="spinner"></div>
        <p>正在评估中，请稍候...</p>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, reactive, ref } from 'vue'
import { PatientData, Prediction, predic } from '../api/prediction'

export default defineComponent({
  name: 'DiabetesAssessmentApp',
  setup() {
    // 初始化患者数据
    const patientData = reactive<PatientData>({
      age: 20,
      weight: 60,
      height: 1.8,
      waist_circumference: 80,
      pregnancies: 0,
      glucose: 100,
      blood_pressure: 90,
      insulin: 10,
      family_history: {
        parents: 0,
        siblings: 0,
        grandparents: 0,
        aunts_uncles: 0,
        has_gestational: 0
      }
    })

    const result = ref<Prediction | null>(null)
    const isLoading = ref(false)
    const errorMessage = ref('')

    // 提交表单
    const submitForm = async () => {
      isLoading.value = true
      errorMessage.value = ''
      result.value = null

      try {
        // 调用预测接口
        const prediction = await predic(patientData)
        if (prediction) {
          result.value = prediction
        } else {
          errorMessage.value = '评估失败，请检查输入数据'
        }
      } catch (error) {
        console.error('评估错误:', error)
        errorMessage.value = '评估过程中发生错误，请重试'
      } finally {
        isLoading.value = false
      }
    }

    // 重置表单
    const resetForm = () => {
      Object.assign(patientData, {
        age: 0,
        weight: 0,
        height: 0,
        waist_circumference: 0,
        pregnancies: 0,
        glucose: 0,
        blood_pressure: 0,
        insulin: 0,
        family_history: {
          parents: 0,
          siblings: 0,
          grandparents: 0,
          aunts_uncles: 0,
          has_gestational: 0
        }
      })
      result.value = null
      errorMessage.value = ''
    }

    return {
      patientData,
      result,
      isLoading,
      errorMessage,
      submitForm,
      resetForm
    }
  }
})
</script>

<style scoped>
.diabetes-assessment-app {
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  max-width: 80%;
  margin: 0 auto;
  padding: 20px;
  color: #333;
}

.header {
  text-align: center;
  margin-bottom: 30px;
  padding: 20px;
  background: linear-gradient(135deg, #6b73ff 0%, #000dff 100%);
  color: white;
  border-radius: 10px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  opacity: 0.8;
}

.header h1 {
  margin: 0;
  font-size: 2.2rem;
}

.header p {
  margin: 10px 0 0;
  opacity: 0.9;
}

.container {
  display: flex;
  gap: 30px;
}

.form-container {
  flex: 2;
  background: #fff;
  padding: 25px;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.form-container h2 {
  margin-top: 0;
  color: #2c3e50;
  border-bottom: 2px solid #eee;
  padding-bottom: 10px;
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 20px;
}

.form-section {
  margin-bottom: 20px;
}

.form-section h3 {
  margin: 0 0 15px;
  color: #3498db;
  font-size: 1.1rem;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  font-weight: 500;
  color: #555;
}

.form-group input[type="number"] {
  width: 80%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 5px;
  font-size: 1rem;
  transition: border-color 0.3s;
}

.form-group input[type="number"]:focus {
  border-color: #3498db;
  outline: none;
  box-shadow: 0 0 0 2px rgba(52, 152, 219, 0.2);
}

.checkbox-group label {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
}

.checkbox-group input[type="checkbox"] {
  width: auto;
}

.form-actions {
  margin-top: 25px;
  display: flex;
  gap: 15px;
}

.form-actions button {
  padding: 12px 20px;
  border: none;
  border-radius: 5px;
  font-size: 1rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s;
}

.form-actions button:first-child {
  background-color: #3498db;
  color: white;
}

.form-actions button:first-child:hover {
  background-color: #2980b9;
}

.form-actions button:last-child {
  background-color: #f1f1f1;
  color: #333;
}

.form-actions button:last-child:hover {
  background-color: #e1e1e1;
}

.form-actions button:disabled {
  background-color: #95a5a6;
  cursor: not-allowed;
}

.result-container {
  flex: 1;
}

.result-container h2 {
  margin-top: 0;
  color: #2c3e50;
  padding-bottom: 10px;
  border-bottom: 2px solid #eee;
}

.result-card {
  background: white;
  border-radius: 10px;
  padding: 25px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
  border-top: 5px solid;
}

.result-card.高风险 {
  border-color: #e74c3c;
}

.result-card.中风险 {
  border-color: #f39c12;
}

.result-card.低风险 {
  border-color: #2ecc71;
}

.result-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.result-header h3 {
  margin: 0;
  font-size: 1.3rem;
  color: #2c3e50;
}

.risk-badge {
  padding: 5px 15px;
  border-radius: 20px;
  font-weight: bold;
  text-transform: uppercase;
  font-size: 0.8rem;
  letter-spacing: 1px;
}

.result-card.高风险 .risk-badge {
  background-color: #e74c3c;
  color: white;
}

.result-card.中风险 .risk-badge {
  background-color: #f39c12;
  color: white;
}

.result-card.低风险 .risk-badge {
  background-color: #2ecc71;
  color: white;
}

.result-metrics {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 15px;
  margin-bottom: 25px;
}

.metric {
  text-align: center;
  padding: 15px;
  background: #f8f9fa;
  border-radius: 8px;
}

.metric-value {
  font-size: 1.8rem;
  font-weight: bold;
  margin-bottom: 5px;
}

.result-card.高风险 .metric-value {
  color: #e74c3c;
}

.result-card.中风险 .metric-value {
  color: #f39c12;
}

.result-card.低风险 .metric-value {
  color: #2ecc71;
}

.metric-label {
  font-size: 0.9rem;
  color: #7f8c8d;
}

.result-details {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 15px;
  margin-bottom: 25px;
}

.detail-item {
  display: flex;
  justify-content: space-between;
  padding: 10px 0;
  border-bottom: 1px dashed #eee;
}

.detail-item label {
  font-weight: 500;
  color: #555;
}

.medical-advice {
  background: #f8f9fa;
  padding: 15px;
  border-radius: 8px;
  margin-top: 20px;
}

.medical-advice h4 {
  margin-top: 0;
  color: #2c3e50;
  border-bottom: 1px solid #ddd;
  padding-bottom: 8px;
}

.medical-advice pre {
  white-space: pre-wrap;
  font-family: inherit;
  line-height: 1.5;
  margin: 0;
}

.loading-container {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(255, 255, 255, 0.8);
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.spinner {
  width: 50px;
  height: 50px;
  border: 5px solid #f3f3f3;
  border-top: 5px solid #3498db;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 15px;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }

  100% {
    transform: rotate(360deg);
  }
}

.error-message {
  color: #e74c3c;
  background: #fadbd8;
  padding: 15px;
  border-radius: 5px;
  margin-top: 20px;
}

@media (max-width: 768px) {
  .container {
    flex-direction: column;
  }

  .form-grid {
    grid-template-columns: 1fr;
  }

  .result-metrics,
  .result-details {
    grid-template-columns: 1fr;
  }
}
</style>