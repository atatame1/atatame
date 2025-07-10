<template>
  <div class="diabetes-risk-form">
    <el-form :model="formData" label-width="180px" label-position="left">
      <el-card shadow="hover">
        <template #header>
          <h3>基础信息</h3>
        </template>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="年龄(岁)" prop="age" required>
              <el-input-number v-model="formData.age" :min="1" :controls="false" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="体重(kg)" prop="weight" required>
              <el-input-number v-model="formData.weight" :min="1" :controls="false" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="身高(cm)" prop="height" required>
              <el-input-number v-model="formData.height" :min="50" :max="250" :controls="false" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="腰围(cm)" prop="waist_circumference" required>
              <el-input-number v-model="formData.waist_circumference" :min="40" :max="200" :controls="false" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>

      <el-card shadow="hover" class="mt-4">
        <template #header>
          <h3>临床指标</h3>
        </template>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="怀孕次数" prop="pregnancies">
              <el-input-number v-model="formData.pregnancies" :min="0" :controls="false" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="血糖(mg/dL)" prop="glucose" required>
              <el-input-number v-model="formData.glucose" :min="0" :controls="false" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="舒张压(mm Hg)" prop="blood_pressure" required>
              <el-input-number v-model="formData.blood_pressure" :min="0" :controls="false" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="血清胰岛素(μU/mL)" prop="insulin" required>
              <el-input-number v-model="formData.insulin" :min="0" :controls="false" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>

      <el-card shadow="hover" class="mt-4">
        <template #header>
          <h3>家族史</h3>
        </template>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="父母患病人数" prop="family_history.parents">
              <el-input-number 
                v-model="formData.family_history.parents" 
                :min="0" 
                :max="2" 
                :controls="false" 
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="兄弟姐妹患病人数" prop="family_history.siblings">
              <el-input-number v-model="formData.family_history.siblings" :min="0" :controls="false" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="(外)祖父母患病人数" prop="family_history.grandparents">
              <el-input-number v-model="formData.family_history.grandparents" :min="0" :controls="false" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="叔伯姑姨舅患病人数" prop="family_history.aunts_uncles">
              <el-input-number v-model="formData.family_history.aunts_uncles" :min="0" :controls="false" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="是否有妊娠糖尿病史" prop="family_history.has_gestational">
              <el-switch 
                v-model="formData.family_history.has_gestational" 
                :active-value="1" 
                :inactive-value="0" 
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>

      <div class="form-actions mt-4">
        <el-button type="primary" @click="submitForm">提交评估</el-button>
        <el-button @click="resetForm">重置表单</el-button>
      </div>
    </el-form>

    <!-- 计算结果展示 -->
    <el-card v-if="calculatedData" shadow="hover" class="mt-4">
      <template #header>
        <h3>计算结果</h3>
      </template>
      <el-descriptions :column="1" border>
        <el-descriptions-item label="BMI">
          {{ calculatedData.bmi?.toFixed(2) }} kg/m²
        </el-descriptions-item>
        <el-descriptions-item label="皮肤褶厚度" v-if="calculatedData.skin_thickness">
          {{ calculatedData.skin_thickness?.toFixed(2) }} mm
        </el-descriptions-item>
        <el-descriptions-item label="糖尿病遗传系数" v-if="calculatedData.dpf">
          {{ calculatedData.dpf?.toFixed(2) }}
        </el-descriptions-item>
      </el-descriptions>
    </el-card>
  </div>
</template>

<script>
import { ref, reactive, computed } from 'vue'

export default {
  name: 'DiabetesRiskForm',
  setup() {
    // 表单数据
    const formData = reactive({
        age: null,
        weight: null,
        height: null,
        waist_circumference: null,
        pregnancies: 0,
        glucose: null,
        blood_pressure: null,
        insulin: null,
        family_history: {
        parents: 0,
        siblings: 0,
        grandparents: 0,
        aunts_uncles: 0,
        has_gestational: 0
      }
    })

    // 计算字段
    const calculatedData = computed(() => {
      if (!formData.height || !formData.weight) return null
      
      // 计算BMI (体重(kg) / 身高(m)^2)
      const heightInMeters = formData.height / 100
      const bmi = formData.weight / (heightInMeters * heightInMeters)
      
      // 这里可以添加其他计算逻辑
      // 示例: 简单的糖尿病遗传系数计算 (实际应根据具体算法)
      const familyScore = 
        formData.family_history.parents * 0.5 + 
        formData.family_history.siblings * 0.3 + 
        formData.family_history.grandparents * 0.2 + 
        formData.family_history.aunts_uncles * 0.1
      
      const dpf = Math.min(Math.max(0.1, familyScore / 10 + 0.5), 2.5)
      
      return {
        bmi,
        // 示例: 皮肤褶厚度计算 (实际应根据具体算法)
        skin_thickness: formData.waist_circumference * 0.2,
        dpf
      }
    })

    // 提交表单
    const submitForm = () => {
      console.log('提交数据:', {
        ...formData,
        ...calculatedData.value
      })
      // 这里可以添加API调用逻辑
    }

    // 重置表单
    const resetForm = () => {
      Object.assign(formData, {
        age: null,
        weight: null,
        height: null,
        waist_circumference: null,
        pregnancies: 0,
        glucose: null,
        blood_pressure: null,
        insulin: null,
        family_history: {
          parents: 0,
          siblings: 0,
          grandparents: 0,
          aunts_uncles: 0,
          has_gestational: 0
        }
      })
    }

    return {
      formData,
      calculatedData,
      submitForm,
      resetForm
    }
  }
}
</script>

<style scoped>
.diabetes-risk-form {
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px;
}

.mt-4 {
  margin-top: 20px;
}

.form-actions {
  display: flex;
  justify-content: center;
  gap: 20px;
}

.el-card {
  border-radius: 8px;
}

.el-card :deep(.el-card__header) {
  padding: 15px 20px;
  background-color: #f5f7fa;
}
</style>