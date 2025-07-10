<template>
  <div>
    <!-- 头部导航栏 -->
    <div class="header-container">
      <div class="left-section">
        <img src="/src/assets/png/256.png" alt="系统logo" class="logo"/>
        <div class="title-group">
          <div class="SystemTitle_Chinese">糖心系统</div>
          <div class="SystemTitle_Eng">SUGAR HEART SYSTEM</div>
        </div>
      </div>
      
      <div class="right-section">
        <el-dropdown trigger="click">
          <div class="avatar-wrapper">
            <span class="username">{{ username }}</span>
            <el-avatar :size="36" :src="faceImg" class="avatar"/>
            <el-icon class="dropdown-icon"><ArrowDown /></el-icon>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="handleCommand('profile')">个人中心</el-dropdown-item>
              <el-dropdown-item divided @click="handleCommand('logout')">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>

    <!-- 个人资料弹窗 -->
    <el-dialog 
      v-model="dialogVisible" 
      width="900"
      :lock-scroll="false"
    >
      <user 
        v-if="dialogVisible" 
        @submit="handleSubmit"
      />
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { ArrowDown } from '@element-plus/icons-vue';
import { getUser } from '../api/user';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import user from '../views/user.vue';

const router = useRouter();
const faceImg = ref<string>('');
const username = ref<string>('访客');
const dialogVisible = ref(false);

onMounted(async () => {
  await loadUserData();
});

const loadUserData = async () => {
    const user = await getUser();
    faceImg.value = user?.faceImg || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png';
    username.value = user?.username || '未知用户';
};

const handleCommand = (command: string) => {
  switch (command) {
    case 'profile':
      dialogVisible.value = true;
      break;
    case 'logout':
      // 这里添加实际的退出登录逻辑
      router.push('/');
      ElMessage.success('退出成功');
      break;
  }
};

const handleSubmit=async (userData?: { username: string; faceImg: string })=>{
  if(userData){
    username.value=userData.username
    faceImg.value=userData.faceImg
  }
  dialogVisible.value=false
}
</script>

<style scoped lang="css">
.header-container {
  width: 100%;
  position: relative;
  z-index: 100;
}

.left-section {
  position: absolute;
  top: 30px;
  left: 20px;
  display: flex;
  align-items: center;
  height: 100%;
}

.logo {
  width: 36px;
  height: 36px;
  border-radius: 8px;
  margin-right: 12px;
  object-fit: cover;
}

.title-group {
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.SystemTitle_Chinese {
  font-size: 18px;
  font-family: "PingFang SC", "Microsoft YaHei", sans-serif;
  font-weight: 600;
  color: #fff;
  letter-spacing: 1px;
  line-height: 1.2;
}

.SystemTitle_Eng {
  font-size: 11px;
  font-family: "Arial", sans-serif;
  color: rgba(255, 255, 255, 0.9);
  letter-spacing: 0.5px;
  line-height: 1.2;
  margin-top: 2px;
  text-transform: uppercase;
}

.right-section {
  position: absolute;
  top: 30px;
  right: 20px;
  height: 100%;
  display: flex;
  align-items: center;
}

.avatar-wrapper {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 16px;
  transition: all 0.3s;
  height: 40px;
}

.avatar-wrapper:hover {
  background-color: rgba(255, 255, 255, 0.2);
}

.username {
  font-size: 14px;
  color: white;
  margin-right: 8px;
  font-weight: 500;
  max-width: 120px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.avatar {
  border: 2px solid rgba(255, 255, 255, 0.8);
  background-color: #f5f7fa;
}

.dropdown-icon {
  color: white;
  margin-left: 4px;
  font-size: 12px;
  transition: transform 0.3s;
}

.avatar-wrapper:hover .dropdown-icon {
  transform: rotate(180deg);
}

@media (max-width: 768px) {
  .SystemTitle_Chinese {
    font-size: 16px;
  }
  
  .SystemTitle_Eng {
    display: none;
  }
  
  .username {
    display: none;
  }
}
</style>