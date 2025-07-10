<template>
  <div class="diabetes-qa-container">
    <el-card class="qa-card">
      <template #header>
        <div class="card-header">
          <el-icon><ChatDotRound /></el-icon>
          <span style="margin-left: 10px;">糖心智能助手</span>
        </div>
      </template>

      <!-- 聊天记录区域 -->
      <div class="chat-history" ref="chatContainer">
        <div 
          v-for="(message, index) in messages" 
          :key="index" 
          :class="['message', message.type]"
        >
          <el-avatar :size="32" :src="message.type === 'question' ? faceImg : 'http://127.0.0.1:9000/atatame/03be7445-6cf1-4fc7-82da-6ba4dd3a4cda_1751866150637.png'"></el-avatar>
          <div class="message-content">
            <div class="message-text">
              {{ message.text }}
              <el-icon class="loading-icon" v-if="message.isThinking">
                <Loading />
              </el-icon>
            </div>
            <div class="message-time">{{ message.time }}</div>
          </div>
        </div>
      </div>

      <!-- 输入区域 -->
      <div class="input-area">
        <el-input
          v-model="userInput"
          placeholder="请输入您的问题（例如：血糖正常范围、如何注射胰岛素）..."
          @keyup.enter="handleSubmit"
          clearable
        >
          <template #append>
            <el-button 
              type="primary" 
              size="large"
              @click="handleSubmit"
              :loading="isLoading"
            >
              <el-icon><Promotion /></el-icon>
              发送
            </el-button>
          </template>
        </el-input>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { ChatDotRound, Promotion,Loading } from '@element-plus/icons-vue'
import { getUser } from '../api/user'
import getAnswer from '../api/ai'
// 用户输入
const userInput = ref('')
const isLoading = ref(false)
const chatContainer = ref(null)
const faceImg=ref()

onMounted(async ()=>{
      const user = await getUser();
      if (user) {
        faceImg.value = user.faceImg || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png';
      }
})

// 聊天记录
const messages = ref([
  {
    type: 'welcome',
    text: '您好！我是糖尿病智能助手，可以回答您关于糖尿病防治、饮食、运动等方面的问题。请输入您的问题：',
    time: new Date().toLocaleTimeString()
  }
])

// 静态问答知识库
const qaDatabase = [
  {
    keywords: ['血糖', '正常范围', '标准'],
    answer: '空腹血糖正常范围是3.9-6.1mmol/L，餐后2小时血糖应低于7.8mmol/L。'
  },
  {
    keywords: ['饮食', '吃什么', '食谱'],
    answer: '糖尿病饮食建议：\n1. 控制总热量，均衡营养\n2. 多食用全谷物、蔬菜\n3. 限制精制糖和饱和脂肪\n4. 定时定量进餐'
  },
  {
    keywords: ['运动', '锻炼', '运动建议'],
    answer: '糖尿病运动建议：\n1. 每周至少150分钟中等强度有氧运动\n2. 如快走、游泳、骑自行车\n3. 运动前后注意血糖监测\n4. 避免空腹运动'
  },
  {
    keywords: ['胰岛素', '注射', '用药'],
    answer: '胰岛素使用指南：\n1. 注射前洗手并检查胰岛素\n2. 轮换注射部位\n3. 注射后停留10秒再拔出\n4. 严格遵医嘱用药'
  },
  {
    keywords: ['并发症', '风险', '危害'],
    answer: '糖尿病常见并发症：\n1. 视网膜病变（可导致失明）\n2. 肾病（肾功能衰竭）\n3. 神经病变（感觉异常）\n4. 心血管疾病（心梗、中风）'
  },
  {
    keywords: ['监测', '血糖仪', '测量'],
    answer: '血糖监测建议：\n1. 空腹及餐后2小时血糖\n2. 使用前检查血糖仪代码\n3. 记录测量结果\n4. 定期校准设备'
  },
  {
    keywords: ['低血糖', '症状', '处理'],
    answer: '低血糖症状及处理：\n症状：出汗、颤抖、心慌、饥饿感\n处理：立即进食15克碳水化合物，15分钟后复查血糖'
  },
  {
    keywords: ['糖化血红蛋白', 'HbA1c', '指标'],
    answer: '糖化血红蛋白（HbA1c）反映2-3个月平均血糖水平，目标值通常＜7%（具体请遵医嘱）。'
  }
]

// 处理用户提交
const handleSubmit = async () => {
  if (!userInput.value.trim() || isLoading.value) return
  
  const question = userInput.value.trim()
  
  // 添加用户问题到聊天记录
  messages.value.push({
    type: 'question',
    text: question,
    time: new Date().toLocaleTimeString()
  })
  
  userInput.value = ''
  isLoading.value = true
  
  // 添加"思考中"消息
  const thinkingMessage = {
    type: 'answer',
    text: '糖心正在思考...',
    time: new Date().toLocaleTimeString(),
    isThinking: true // 标记为临时消息
  }
  messages.value.push(thinkingMessage)
  
  // 滚动到底部
  await nextTick()
  chatContainer.value.scrollTop = chatContainer.value.scrollHeight
  
  try {
    // 获取回答
    const answer = await getAnswer(question)
    
    // 移除"思考中"消息
    messages.value = messages.value.filter(msg => !msg.isThinking)
    
    if (answer !== null) {
      // 添加真实回答
      messages.value.push({
        type: 'answer',
        text: answer,
        time: new Date().toLocaleTimeString()
      })
    } else {
      // 如果返回null，显示错误提示
      messages.value.push({
        type: 'answer',
        text: '抱歉，暂时无法获取回答，请稍后再试',
        time: new Date().toLocaleTimeString()
      })
    }
  } catch (error) {
    // 错误处理
    messages.value = messages.value.filter(msg => !msg.isThinking)
    messages.value.push({
      type: 'answer',
      text: '服务暂时不可用，请稍后再试',
      time: new Date().toLocaleTimeString()
    })
  } finally {
    isLoading.value = false
    
    // 滚动到底部
    await nextTick()
    chatContainer.value.scrollTop = chatContainer.value.scrollHeight
  }
}

// 关键字匹配获取答案
const getAnswer1 = (question) => {
  const lowerQuestion = question.toLowerCase()
  
  // 精确匹配优先
  const exactMatch = qaDatabase.find(item => 
    item.keywords.some(keyword => 
      lowerQuestion.includes(keyword.toLowerCase())
    )
  )
  
  if (exactMatch) {
    return exactMatch.answer
  }
  
  // 模糊匹配（如果需要）
  // 这里可以添加更复杂的匹配逻辑
  
  // 默认回答
  return '抱歉，我无法回答这个问题。建议咨询专业医生或使用更详细的描述（例如：糖尿病饮食、胰岛素注射方法等）。'
}

// 初始化时滚动到底部
onMounted(() => {
  nextTick(() => {
    if (chatContainer.value) {
      chatContainer.value.scrollTop = chatContainer.value.scrollHeight
    }
  })
})
</script>

<style scoped>
.diabetes-qa-container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px;
}

.qa-card {
  min-height: 800px;
  display: flex;
  flex-direction: column;
}

.card-header {
  display: flex;
  align-items: center;
  font-size: 18px;
  font-weight: bold;
}

.chat-history {
  flex: 1;
  overflow-y: auto;
  padding: 10px;
  height: 700px;
}

.message {
  display: flex;
  margin-bottom: 20px;
  gap: 12px;
}

.message-content {
  max-width: 80%;
}

.message-text {
  padding: 10px 15px;
  border-radius: 4px;
  line-height: 1.5;
  white-space: pre-line;
}

.message-time {
  font-size: 12px;
  color: #999;
  margin-top: 4px;
}

.question .message-content {
  display: flex;
  flex-direction: column;
}

.question .message-text {
  background-color: #e6f7ff;
  border: 1px solid #91d5ff;
}

.thinking .message-text {
  color: #888;
  font-style: italic;
  animation: blink 1.5s infinite;
}

.loading-icon {
  animation: rotating 2s linear infinite;
  margin-left: 5px;
  vertical-align: middle;
}

/* AI回答样式 */
.answer .message-text {
  background-color: #f7f7f7;
  border: 1px solid #eee;
}

.welcome .message-text {
  background-color: #f6ffed;
  border: 1px solid #b7eb8f;
  color: #52c41a;
}

.input-area {
  margin-top: 20px;
}

:deep(.el-input-group__append) {
  padding: 0;
}
</style>