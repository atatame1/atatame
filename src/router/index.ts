import { createRouter, createWebHistory, RouteRecordRaw, Router } from "vue-router";
import initDynamicRoutes from "./dynamicRouter";
import { checkToken } from "../api/token";
import { ElMessage } from "element-plus";

// 静态路由
const staticRoutes: RouteRecordRaw[] = [
  {
    name: "login",
    path: "/",
    component: () => import("../login.vue"),
  },
  {
    name: "MainFrame",
    path: "/views",
    component: () => import("../MainFrame.vue"),
    children: [], // 动态路由会挂载到这里
    meta: { requiresAuth: true },
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes: staticRoutes,
});

// 检查动态路由是否已加载
function hasDynamicRoutesLoaded() {
  return router.getRoutes().some((route) => route.meta?.isDynamic);
}

// 路由守卫
router.beforeEach(async (to, from, next) => {
  const token = localStorage.getItem("jwtToken");
 
  // 1. 登录页直接放行
  if (to.name === "login") {
    next();
    return;
  }
 
  // 2. 需要认证的路由
  if (to.meta.requiresAuth) {
    // 2.1 检查 token 是否存在
 
    // 2.2 验证 token 是否有效
    const isValid = await checkToken(token);
    if (!isValid) {
      localStorage.removeItem("jwtToken");
      next({ name: "login" });
      ElMessage.error("登录已过期，请重新登录");
      return;
    }
 
    // 2.3 动态路由未加载时，加载它们
    if (!hasDynamicRoutesLoaded()) {
      try {
        console.log("加载动态路由...");
        await initDynamicRoutes(router);
        next();
      } catch (error) {
        console.error("动态路由加载失败:", error);
        ElMessage.error("动态路由加载失败");
        localStorage.removeItem("jwtToken");
        next({ name: "login" });
      }
    } else {
      next();
    }
  } else {
    next();
  }
});

export default router;