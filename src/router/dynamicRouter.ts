import { Router, RouteRecordRaw, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import getFunctionByToken from '../api/functionByToken'

interface ApiFunctionItem {
  id: string
  functionName: string
  routerPath: string 
}


const initDynamicRoutes = async (router:Router) => {
  try {

    const allFunctions: ApiFunctionItem[] = await getFunctionByToken()
    if (!allFunctions.length) {
      ElMessage.warning('未获取到任何功能权限')
      return false
    }
 
    // 2. 预加载所有视图组件（使用import.meta.glob）
    const viewComponents = import.meta.glob<any>('../views/**/*.vue')
    
    // 3. 处理每个功能项
    const childrenRoutes: RouteRecordRaw[] = []
    
    for (const func of allFunctions) {
      // 构建组件路径（匹配glob模式）
      const componentPath = `..${func.routerPath}.vue`
      
      // 检查组件是否存在
      if (func.routerPath ===null) {
        continue // 跳过不存在的组件
      }

      // 创建路由配置
      const routeItem: RouteRecordRaw = {
        name: `${func.functionName}_${func.id}`,
        path: func.routerPath,
        component: viewComponents[componentPath],
        meta: { isDynamic: true }, // 标记为动态路由
      };

      childrenRoutes.push(routeItem)
    }
 
    // 4. 添加子路由
    childrenRoutes.forEach(route => {
        router.addRoute('MainFrame', route); 
      });

    ElMessage.success('路由加载成功')
      return true
  } catch (error) {
    console.error('initDynamicRoutes获取路由失败:', error)
    ElMessage.error(`获取路由失败: ${error instanceof Error ? error.message : String(error)}`)
    return false
  }
}

export  default initDynamicRoutes 