<template>
  <div>
    <el-menu
      style="width: 220px;height: 100%;"
      background-color="#3c414a"
      text-color="#b7bdc3"
      active-text-color="#fff"
    >
      <template v-for="item in items" :key="item.id">
        <!-- 有子菜单的项 -->
        <el-sub-menu 
          v-if="item.children && item.children.length > 0" 
          :index="item.routerPath"
        >
          <template #title>
            <component :is="item.iconComponent" v-if="item.iconComponent" class="menuicon"/>
            <span>{{ item.functionName }}</span>
          </template>
          <el-menu-item 
            v-for="child in item.children" 
            :key="child.id" 
            :index="child.routerPath"
            @click="handleMenuClick(child.routerPath)"
          >
            <component :is="child.iconComponent" v-if="child.iconComponent" class="menuicon"/>
            <span>{{ child.functionName }}</span>
          </el-menu-item>
        </el-sub-menu>

        <!-- 没有子菜单的项 -->
        <el-menu-item 
          v-else 
          :index="item.routerPath"
          @click="handleMenuClick(item.routerPath)"
        >
          <component :is="item.iconComponent" v-if="item.iconComponent" class="menuicon"/>
          <span>{{ item.functionName }}</span>
        </el-menu-item>
      </template>
    </el-menu>
  </div>
</template>

<script setup lang="ts">
import { reactive, h } from 'vue'
import { useRouter } from 'vue-router'
import * as icons from '@element-plus/icons-vue'; 
import axios from '../utils/http'
import { ElMessage } from 'element-plus'

// 菜单项类型定义
interface MenuItem {
  id: string
  functionName: string
  icon?: string
  iconComponent?: ReturnType<typeof h> // 渲染后的图标组件
  routerPath: string
  children?: MenuItem[]
}

// API 返回的函数数据类型
interface ApiFunctionItem {
  id: string
  functionName: string
  icon?: string
  routerPath: string
  parentId?: string | null
}

const router = useRouter()
const items = reactive<MenuItem[]>([])

// 处理菜单点击
const handleMenuClick = (path: string) => {
  if (path) {
    router.push(path)
  }
}

// 获取所有功能菜单
const getAllFunctions = async () => {
  try {
    const res = await axios.get('/function/getFunctionByToken')
    if (res.status === 200) {
      const allFunctions = res.data.body as ApiFunctionItem[]
      buildMenuTree(allFunctions, null, items)
    } else {
      ElMessage.error('获取菜单失败')
    }
  } catch (error) {
    ElMessage.error('请求错误: ' + error)
  }
}

// 构建菜单树
const buildMenuTree = (
  allFunctions: ApiFunctionItem[],
  parentId: string | null,
  menuItems: MenuItem[]
) => {
  const subFunctions = allFunctions.filter(item => 
    parentId === null ? item.parentId === null : item.parentId === parentId
  )

  subFunctions.forEach(functionItem => {
    const menuItem: MenuItem = {
      id: functionItem.id,
      functionName: functionItem.functionName,
      icon: functionItem.icon,
      routerPath: functionItem.routerPath,
      children: []
    }

    // 动态加载图标组件（根据实际使用的图标库调整）
    if (functionItem.icon) {
      // 对于 Element Plus 图标，需要这样导入：
      // const iconModule = import(`@element-plus/icons-vue`).then(m => m[functionItem.icon])
      // 这里以 ant-design 为例：
      try {
        menuItem.iconComponent = h(icons[functionItem.icon as keyof typeof icons])
      } catch (e) {
        console.warn(`图标 ${functionItem.icon} 加载失败`)
      }
    }

    // 递归构建子菜单
    buildMenuTree(allFunctions, functionItem.id, menuItem.children!)
    
    // 如果没有子菜单，删除 children 属性
    if (menuItem.children!.length === 0) {
      delete menuItem.children
    }

    menuItems.push(menuItem)
  })
}

// 初始化时获取菜单数据
getAllFunctions()
</script>

<style scoped>
:deep(.menuicon) {
  margin-right: 8px;
  font-size: 16px;
  width: 1em;
  height: 1em;
}
</style>