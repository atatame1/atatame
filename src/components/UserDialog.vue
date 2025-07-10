<template>
  <div class="user-info-edit-container">
    <el-card shadow="hover" class="form-card">
      <template #title>
        <span>
          {{ formType?'编辑用户信息' :'添加用户' }}
        </span>
      </template>
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="120px"
        label-position="top"
        class="user-info-form"
      >
        <!-- 基础信息 -->
        <div class="form-section">
          <h3 class="section-title">基础信息</h3>
          
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="用户名" prop="username">
                <el-input v-model="formData.username" placeholder="请输入用户名" />
              </el-form-item>
            </el-col>
            
            <el-col :span="12">
              <el-form-item label="真实姓名" prop="realName">
                <el-input v-model="formData.realName" placeholder="请输入真实姓名" />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="密码" prop="password">
                <el-input
                  v-model="formData.password"
                  type="password"
                  placeholder="请输入密码"
                  show-password
                />
              </el-form-item>
            </el-col>
            
            <el-col :span="12">
              <el-form-item label="身份" prop="role">
                <el-select
                  v-model="formData.role"
                  placeholder="请选择身份"
                  style="width: 100%"
                >
                  <el-option label="普通用户" :value="0" />
                  <el-option label="管理员" :value="1" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </div>

        <!-- 个人资料 -->
        <div class="form-section">
          <h3 class="section-title">个人资料</h3>
          
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="生日" prop="birthday">
                <el-date-picker
                  v-model="formData.birthday"
                  type="date"
                  placeholder="选择日期时间"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
            
            <el-col :span="12">
              <el-form-item label="性别" prop="gender">
                <el-radio-group v-model="formData.gender">
                  <el-radio :label=1>女</el-radio>
                  <el-radio :label=2>男</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="职业" prop="occupation">
                <el-input v-model="formData.occupation" placeholder="请输入职业" />
              </el-form-item>
            </el-col>
          </el-row>
        </div>

        <!-- 表单操作按钮 -->
        <div class="form-actions">
          <el-button type="primary" @click="submitForm" :loading="submitting">
            {{ submitText }}
          </el-button>
          <el-button @click="cancelEdit">取消</el-button>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, watch } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'
import upload from '../components/upload.vue' // 根据你的实际路径调整
import { ElMessage } from 'element-plus'
import {UserAdd,UserUpdate, UserInfoVo, InitUserInfo} from '../api/user'


// 组件属性
const props = defineProps({
  // 如果是编辑模式，传入用户数据
  userData: {
    type: Object as () => UserInfoVo,
    default: null
  }
})
console.log(props.userData)
const formType = computed(() => props.userData!=null ? true : false)//ture为编辑模式，false为新增模式
const submitText = computed(() => formType.value ? '更新信息' : '添加')
// 表单引用
const formRef = ref<FormInstance>()
// 初始化表单数据
const formData = reactive<UserInfoVo>({ ...InitUserInfo });

watch(() => props.userData, (newVal) => {
  if (newVal) {
    Object.assign(formData, {
      ...InitUserInfo,
      ...newVal,
    });
  } else {
    Object.assign(formData, InitUserInfo);
  }
}, { immediate: true });

const submitting = ref(false)

// 表单验证规则
const formRules = reactive<FormRules>({
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  role: [
    { required: true, message: '请选择角色', trigger: 'change' }
  ],
})


// 提交表单
const emit = defineEmits(['submit', 'cancel'])

const submitForm = async () => {
  if (!formRef.value) return
  let flag=false
  try {
    if(formType.value){//编辑模式
      flag = await UserUpdate(formData)
    }else{//添加模式
      flag= await UserAdd(formData)
    }
    if(flag){
      cancelEdit()
    }
  } catch (error) {
    submitting.value=true
    console.error('表单验证失败:', error)
    ElMessage.warning('请填写完整且正确的表单信息')
  } finally {
    submitting.value = false
  }
}

// 取消编辑
const cancelEdit = () => {
  emit('cancel')
}
const submit = () => {
  emit('submit')
}

</script>

<style scoped>
.user-info-edit-container {
  padding: 20px;
  background-color: #f5f7fa;
}

.form-card {
  max-width: 1000px;
  margin: 0 auto;
  border-radius: 4px;
  border: none;
}

.card-header {
  font-size: 18px;
  font-weight: 500;
  color: #303133;
}

.user-info-form {
  padding: 10px 20px;
}

.form-section {
  margin-bottom: 24px;
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 4px;
}

.section-title {
  margin-top: 0;
  margin-bottom: 20px;
  color: #409eff;
  font-size: 16px;
}

.form-actions {
  margin-top: 30px;
  text-align: center;
}

.el-upload__tip {
  font-size: 12px;
  color: #909399;
  margin-top: 7px;
  line-height: 1.5;
}
</style>