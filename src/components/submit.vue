<template>
    <div class="container">
        <div v-if="flag">
            <!-- 登录 -->
            <p class="login-title">登录</p>
            <div class="user">
                <el-input
                        v-model="username"
                        placeholder="输入用户名"
                        clearable
                        :prefix-icon="User"
                        autofocus
                    >
                    <template #prepend>用户名</template>
                </el-input>
                
                <el-input
                        v-model="password"
                        type="password"
                        prefixe="密码"
                        placeholder="输入密码"
                        show-password
                        :prefix-icon="Key"
                    >
                    <template #prepend>密码</template>
                </el-input>
                <div class="captcha">
                    <el-input
                        v-model="captchaCode"
                        prefixe="验证码"
                        placeholder="输入验证码"
                        :prefix-icon="Key"
                        style="width:150px ;"
                    />
                    <el-image
                        style="width: 100px; height: 40px;"
                        :src="captchaUrl"
                        fit="contain"
                        @click="captchaSubmit"
                    />
                </div>
                
            </div>    
            <div class="service">    
                <span @click="refresh">未有账号？先注册</span>
                <el-button plain type="primary" @click="loginSubmit">登录</el-button>
            </div>
        </div>

        <!-- 注册 -->
        <div v-else>
            <p class="login-title">注册</p>
            <div class="user">
                <el-input
                        v-model="username"
                        prefixe="用户名"
                        placeholder="输入用户名"
                        clearable
                        :prefix-icon="User"
                        autofocus
                    >
                    <template #prepend>用户名</template>
                </el-input>
                <el-input
                        v-model="password"
                        type="password"
                        prefixe="密码"
                        placeholder="请设置密码"
                        show-password
                        :prefix-icon="Key"
                    >
                    <template #prepend>密码</template>
                </el-input>
                <el-input
                    v-model="secondPassword"
                    type="password"
                    placeholder="确认密码"
                    show-password
                    :prefix-icon="Key"
                />
                <p style="color: red;position: absolute;top:160px" v-if="same">密码不一致</p>
                <div class="captcha">
                    <el-input
                        v-model="captchaCode"
                        prefixe="验证码"
                        placeholder="输入验证码"
                        :prefix-icon="Key"
                        style="width:150px ;"
                    />
                    <el-image
                        style="width: 100px; height: 40px;"
                        :src="captchaUrl"
                        @click="captchaSubmit"
                    />
                </div>
            </div>
            <div class="service">    
                <span @click="refresh">去登录</span>
                <el-button plain type="primary" @click="registerSubmit">注册</el-button>
            </div>    
        </div>

    </div>
</template>

<script setup lang="ts">
    import {ref,watch} from 'vue'
    import { ElMessage } from 'element-plus'
    import { RouteRecordRaw, useRouter } from 'vue-router';
    import { Key, Refresh, User } from '@element-plus/icons-vue';
    import { login, register,captcha } from '../api/userLogin';
    import axios from '../utils/http';

    const username=ref('')
    const password=ref('')
    const captchaCode=ref('')
    const secondPassword=ref('')
    
    interface captchaResponse {
        key: string; // 图片的key
        url: string; // 图片的URL
    }

    let router = useRouter();

    const flag=ref(true)
    const same=ref(false)

    const captchaUrl=ref('')
    const captchaKey=ref('')

    const captchaSubmit=async ()=>{
        const res = await captcha() as captchaResponse;
        if (res) {
           captchaUrl.value = res.url; // 返回图片的URL
           captchaKey.value = res.key; // 返回key
        }
    }

    const loginSubmit=async ()=>{
        if(username.value==''||password.value==''){
            ElMessage.error('用户名或密码不能为空')
            return
        }
        if(captchaCode.value==''){
            ElMessage.error('验证码不能为空')
            return
        }
        const token=await login(username.value,password.value,captchaKey.value,captchaCode.value)
        
        if(token){
            // console.log(res)
            localStorage.setItem('jwtToken', token);//存储token
            console.log(localStorage.getItem('jwtToken'))
            getRoutes()//动态生成路由
        }
    }

    const registerSubmit=async ()=>{
        if(username.value==''||password.value==''){
            ElMessage.error('用户名或密码不能为空')
            return
        }
        if(captchaCode.value==''){
            ElMessage.error('验证码不能为空')
            return
        }
        const res=await register(username.value,password.value,captchaKey.value,captchaCode.value)
        
        if(res!==null){
            refresh()
        }
    }


interface ApiFunctionItem {
  id: string
  functionName: string
  routerPath: string // 示例: "/user/manage" 或 "user/manage"
  [key: string]: any
}
 
 
const getRoutes = async () => {
  // 主框架路由配置
  const MainFrameRoute: RouteRecordRaw = {
    name: 'MainFrame',
    path: '/views',
    component: () => import('../MainFrame.vue'),
    children: []
  }
 
  try {
    // 1. 获取功能列表
    const response = await axios.get('/function/getFunctionByToken')
    if (response.status !== 200) throw new Error('API请求失败')
    if(response.data.code!== 200) ElMessage.error(response.data.message)

    const allFunctions: ApiFunctionItem[] = response.data.body
    if (!allFunctions.length) {
      ElMessage.warning('未获取到任何功能权限')
      return
    }
 
    // 2. 预加载所有视图组件（使用import.meta.glob）
    const viewComponents = import.meta.glob<any>('../views/**/*.vue')
    
    // 3. 处理每个功能项
    const childrenRoutes: RouteRecordRaw[] = []
    
    for (const func of allFunctions) {
      try {

        // 构建组件路径（匹配glob模式）
        const componentPath = `..${func.routerPath}.vue`
        
        // 检查组件是否存在
        if (!viewComponents[componentPath]) {
          console.warn(`组件未找到: ${componentPath}`)
          continue // 跳过不存在的组件
        }
 
        // 创建路由配置
        const routeItem: RouteRecordRaw = {
          name: `${func.functionName}_${func.id}`,
          path: func.routerPath,
          component: viewComponents[componentPath]
        }
 
        childrenRoutes.push(routeItem)
      } catch (routeError) {
        console.error(`处理路由失败: ${func.functionName}`, routeError)
      }
    }
 
    // 4. 添加子路由
    MainFrameRoute.children = childrenRoutes
 
    // 5. 添加到路由系统
    router.addRoute(MainFrameRoute)
    ElMessage.success('路由加载成功')
 
    // 6. 跳转到第一个可用路由或默认路由
    router.push({'name':'MainFrame'})
 
  } catch (error) {
    console.error('获取路由失败:', error)
    ElMessage.error(`获取路由失败: ${error instanceof Error ? error.message : String(error)}`)
    
    // 可以在这里添加降级处理，比如加载默认路由
    // await loadDefaultRoutes()
  }
}
    
    const refresh=()=>{
        username.value=''
        password.value=''
        secondPassword.value=''
        captchaCode.value=''
        flag.value=!flag.value
        captchaSubmit()//刷新验证码
    }
    

    watch(()=>secondPassword.value,(New)=>{
        if(password.value&&New!=password.value){
            same.value=true
        }else{
            same.value=false
        }
    })

    localStorage.clear//清除之前的所有信息
    captchaSubmit()//获取验证码
</script>
   
<style scoped lang="css">
.container {
    width: 400px; /* 更合理的宽度 */
    height: 300px;
    padding: 40px;
    border-radius: 12px;
    position: absolute;
    left: 50%;
    top: 40%;
    transform: translate(-50%, -50%); /* 同时居中X和Y */
    font-family: 'Courier New', Courier, monospace;
}

.login-title {
    position: absolute;
    left: 50%;
    top: 20px;
    transform: translateX(-50%);
    font-size: 24px;
    color: #ffffff; /* 更深的文字颜色 */
    font-weight: 500; /* 中等字重 */
    margin: 0px;;
}

.user {
    width: 100%;
    position: absolute;
    top: 80px;
    left: 90px;
    display: flex;
    flex-direction: column;

}

.user label {
    display: flex;
    align-items: center;
    font-size: 14px;
    color: #ffffff;
}

.user label span {
    width: 60px; /* 固定标签宽度使输入框对齐 */
    flex-shrink: 0;
}

.user :deep(.el-input) {
    flex-grow: 1; /* 输入框占据剩余空间 */
    margin-bottom: 25px; /* 输入框之间的间距 */
    width: 300px;
    height: 40px;
}

.service {
    width: 80%;
    position: absolute;
    bottom: 20px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin: 0;
}

.service span {
    color: #409eff; /* 使用Element Plus主色 */
    font-size: 14px;
    cursor: pointer;
    transition: color 0.2s;
}

.service span:hover {
    color: #66b1ff; /* 悬停颜色 */
}

.el-button {
    width: 120px; /* 固定按钮宽度 */
}
.captcha{
    display: flex;
    justify-content: space-between;
    margin-bottom: 25px; 
    width: 300px;
}

</style>