<template>
    <div class="profile-edit-container">
        <el-card shadow="hover" class="form-card">
            <div class="profile-header">
                <h2>个人资料</h2>
                <el-button v-if="!isEditing" type="primary" @click="startEditing" :icon="Edit">
                    编辑资料
                </el-button>
            </div>

            <!-- 头像展示区域 -->
            <div class="avatar-section">
                <upload ref="avatarRef" :model-value="formData.faceImg ?? undefined" :isEditing="isEditing"
                    @change="handleAvatarChange" />
            </div>

            <!-- 表单区域 -->
            <el-form ref="formRef" :model="formData" :rules="formRules" label-width="120px" label-position="top"
                class="profile-form" :disabled="!isEditing">
                <!-- 基础信息 -->
                <div class="form-section">
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
                                <el-input v-model="formData.password" type="password" placeholder="留空则不修改密码"
                                    show-password />
                            </el-form-item>
                        </el-col>
                    </el-row>
                </div>

                <!-- 个人资料 -->
                <div class="form-section">
                    <el-row :gutter="20">
                        <el-col :span="12">
                            <el-form-item label="生日" prop="birthday">
                                <el-date-picker v-model="formData.birthday" type="date" placeholder="选择日期"
                                    style="width: 100%" value-format="YYYY-MM-DD" />
                            </el-form-item>
                        </el-col>

                        <el-col :span="12">
                            <el-form-item label="性别" prop="gender">
                                <el-radio-group v-model="formData.gender">
                                    <el-radio :label="1">女</el-radio>
                                    <el-radio :label="2">男</el-radio>
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

                <!-- 表单操作按钮（仅编辑模式显示） -->
                <div v-if="isEditing" class="form-actions">
                    <el-button type="primary" @click="submitForm" :loading="submitting">
                        保存修改
                    </el-button>
                    <el-button @click="cancelEditing">取消</el-button>
                </div>
            </el-form>
        </el-card>
    </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'
import { ElMessage } from 'element-plus'
import { Edit } from '@element-plus/icons-vue'
import { getUser, uploadImage, UserInfoVo, InitUserInfo, UserUpdateSelf } from '../api/user'
import upload from '../components/upload.vue'

const formRef = ref<FormInstance>()
const submitting = ref(false)
const isEditing = ref(false)
const avatarRef = ref()
const avatarChanged = ref(false)
// 表单数据
const formData = reactive<UserInfoVo>(InitUserInfo)

// 表单验证规则
const formRules = reactive<FormRules>({
    username: [
        { required: true, message: '请输入用户名', trigger: 'blur' },
        { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
    ],
    password: [
        { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
    ]
})

const emit = defineEmits(['submit'])

// 初始化表单数据
const initFormData = async () => {
    try {
        const res = await getUser()
        if (res != null) {
            Object.assign(formData, res)
            console.log(formData)
        }
    } catch (error) {
        ElMessage.error('获取用户信息失败')
        console.error('获取用户信息失败:', error)
    }
}

// 开始编辑
const startEditing = () => {
    isEditing.value = true
    avatarChanged.value = false
}

// 取消编辑
const cancelEditing = () => {
    isEditing.value = false
    avatarRef.value.reset()
    initFormData() // 重新加载原始数据
}

const handleAvatarChange = () => [
    avatarChanged.value = true
]

const submitForm = async () => {
    if (!formRef.value) return
    let url
    if (avatarChanged.value) {
        url=await avatarRef.value?.upload()
    }
    formData.faceImg=url
    await UserUpdateSelf(formData)
    cancelEditing()
    emit('submit',{
        username:formData.username,
        faceImg:formData.faceImg
    })
}

onMounted(() => {
    initFormData()
})
</script>

<style scoped>
.profile-edit-container {
    padding: 20px;
    background-color: #f5f7fa;
}

.form-card {
    max-width: 800px;
    margin: 0 auto;
    border-radius: 4px;
    border: none;
}

.profile-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
}

.profile-header h2 {
    margin: 0;
    color: #303133;
}

.avatar-section {
    display: flex;
    justify-content: center;
    margin-bottom: 30px;
}

.avatar-uploader {
    position: relative;
}

.avatar-uploader .avatar {
    width: 120px;
    height: 120px;
    display: block;
    border-radius: 50%;
    object-fit: cover;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 50%;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
    border-color: var(--el-color-primary);
}

.avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 120px;
    height: 120px;
    text-align: center;
    line-height: 120px;
    border-radius: 50%;
    background: #f5f7fa;
}

.avatar-edit-hint {
    position: absolute;
    bottom: 0;
    left: 0;
    right: 0;
    background: rgba(0, 0, 0, 0.5);
    color: white;
    text-align: center;
    padding: 5px 0;
    font-size: 12px;
    border-radius: 0 0 50% 50%;
}

.profile-form {
    padding: 10px 20px;
}

.form-section {
    margin-bottom: 24px;
    padding: 20px;
    background-color: #f9f9f9;
    border-radius: 4px;
}

.form-actions {
    margin-top: 30px;
    text-align: center;
}
</style>