<template>
  <el-upload
    class="avatar-uploader"
    :show-file-list="false"
    :before-upload="beforeAvatarUpload"
    :on-change="handleChange"
    :auto-upload="false"
    :disabled="!isEditing"
  >
    <el-image v-if="imageUrl" :src="imageUrl" class="avatar" fit="cover" />
    <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
    <div v-if="isEditing" class="avatar-edit-hint">点击更换头像</div>
  </el-upload>
</template>

<script lang="ts" setup>
import { ref, watch } from 'vue'
import { ElMessage } from 'element-plus'
import type { UploadProps } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { uploadImage } from '../api/user';

const props = defineProps({
  modelValue: {
    type: String,
    default: null
  },
  isEditing:{
    type:Boolean
  }
});
const emit = defineEmits(['update:modelValue','change']);
const imageUrl = ref('')
const file=ref<File|null>(null)

watch(() => props.modelValue, (newVal) => {
  if (newVal) {
    console.log('imageUrl:'+newVal)
    imageUrl.value = newVal 
  } else {
    imageUrl.value = ''
  }
  console.log('imageUrl:'+imageUrl.value)
}, { immediate: true })


const handleChange = (uploadFile: { raw: File }) => {
  file.value = uploadFile.raw
  imageUrl.value = URL.createObjectURL(uploadFile.raw)
  emit('change', uploadFile.raw) // 通知父组件文件已变更
}

const beforeAvatarUpload: UploadProps['beforeUpload'] = (rawFile) => {
  const allowedTypes = ['image/jpeg', 'image/jpg', 'image/png']
  if (!allowedTypes.includes(rawFile.type)) {
    ElMessage.error('图片格式必须是 JPG/PNG!')
    return false
  } else if (rawFile.size / 1024 / 1024 > 2) {
    ElMessage.error('图片大小不能超过 2MB!')
    return false
  }
  return true
}

defineExpose({
  upload:async():Promise<String|null>=>{
    if(!file.value)return null

    const url=await uploadImage(file.value);
    if (url) {
        emit('update:modelValue', url)
        return url
      }
    return null
  },
  reset:()=>{
    file.value = null
    imageUrl.value = props.modelValue || ''
  }
})


</script>

<style scoped>
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
  bottom: 5px;
  left: 0;
  right: 0;
  background: rgba(0, 0, 0, 0.5);
  color: white;
  text-align: center;
  padding: 5px 0;
  font-size: 12px;
  border-radius: 0 0 50% 50%;
}
</style>
