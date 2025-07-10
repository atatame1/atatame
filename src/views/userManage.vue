<template>
    <div class="user-management-container">
        <!-- 页面标题和操作栏 -->
        <div class="header-section">
            <h2 class="page-title">用户管理</h2>
            <div class="actions">
                <el-button type="primary" :icon="Plus" @click="handleAdd">新增用户</el-button>
                <el-button :icon="Refresh" @click="getDate">刷新</el-button>
            </div>
        </div>

        <!-- 搜索和筛选区域 -->
        <div class="filter-section">
            <el-card shadow="hover">
                <el-form :model="filterForm" label-width="80px" class="filter-form">
                    <el-row :gutter="20">
                        <el-col :xs="24" :sm="12" :md="8" :lg="6">
                            <el-form-item label="用户名">
                                <el-input v-model="filterForm.username" placeholder="请输入用户名" clearable />
                            </el-form-item>
                        </el-col>

                        <el-col :xs="24" :sm="12" :md="8" :lg="6">
                            <el-form-item label="角色">
                                <el-select v-model="filterForm.role" placeholder="全部角色" clearable>
                                    <el-option label="管理员" value=1 />
                                    <el-option label="普通用户" value=0 />
                                </el-select>
                            </el-form-item>
                        </el-col>

                        <el-col :xs="24" :sm="12" :md="8" :lg="6">
                            <el-form-item label="状态">
                                <el-select v-model="filterForm.status" placeholder="全部状态" clearable>
                                    <el-option label="启用" value="1" />
                                    <el-option label="禁用" value="0" />
                                </el-select>
                            </el-form-item>
                        </el-col>

                        <el-col :xs="24" :sm="12" :md="8" :lg="6">
                            <el-form-item label="注册时间">
                                <el-date-picker v-model="filterForm.dateRange" type="daterange" range-separator="至"
                                    start-placeholder="开始日期" end-placeholder="结束日期" value-format="YYYY-MM-DD" />
                            </el-form-item>
                        </el-col>
                    </el-row>

                    <div class="filter-actions">
                        <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
                        <el-button :icon="RefreshLeft" @click="resetFilters">重置</el-button>
                    </div>
                </el-form>
            </el-card>
        </div>

        <!-- 数据统计卡片 -->
        <div class="stats-section">
            <el-row :gutter="20">
                <el-col :xs="24" :sm="12" :md="6">
                    <div class="stat-card">
                        <div class="stat-value">{{ stats.totalUsers }}</div>
                        <div class="stat-label">总用户数</div>
                    </div>
                </el-col>
                <el-col :xs="24" :sm="12" :md="6">
                    <div class="stat-card admin">
                        <div class="stat-value">{{ stats.totalAdmins }}</div>
                        <div class="stat-label">管理员</div>
                    </div>
                </el-col>
                <el-col :xs="24" :sm="12" :md="6">
                    <div class="stat-card active">
                        <div class="stat-value">{{ stats.activeUsers }}</div>
                        <div class="stat-label">活跃用户</div>
                    </div>
                </el-col>
                <el-col :xs="24" :sm="12" :md="6">
                    <div class="stat-card disabled">
                        <div class="stat-value">{{ stats.disabledUsers }}</div>
                        <div class="stat-label">禁用账户</div>
                    </div>
                </el-col>
            </el-row>
        </div>

        <!-- 用户表格 -->
        <div class="table-section">
            <el-card shadow="hover">
                <el-table :data="paginatedData" style="width: 100%;min-height: 400px;margin-left: 100px;" border stripe v-loading="loading">
                    <el-table-column prop="avatar" label="头像" width="100" align="center" fit="fill">
                        <template #default="{ row }">
                            <el-avatar :size="40" :src="row.faceImg" />
                        </template>
                    </el-table-column>
                    <el-table-column prop="username" label="用户名" width="120" />
                    <el-table-column prop="password" label="密码" width="120" />
                    <el-table-column prop="realName" label="真实姓名" width="120" />
                    <el-table-column prop="gender" label="性别" width="70" align="center">
                        <template #default="{ row }">
                            {{ row.gender===0?'':row.gender==2  ? '男' : '女' }}
                        </template>
                    </el-table-column>
                    <el-table-column prop="birthday" label="生日" width="180" />
                    <el-table-column prop="occupation" label="职业" width="180" />
                    <el-table-column prop="role" label="身份" width="120" align="center">
                        <template #default="{ row }">
                            <el-tag :type="row.role > 0 ? 'danger' : 'success'">
                                {{ row.role > 0 ? '管理员' : '普通用户' }}
                            </el-tag>
                        </template>
                    </el-table-column>
                    <el-table-column prop="status" label="状态" width="100" align="center">
                        <template #default="{ row }">
                            <el-switch v-model="row.status" :active-value="1" :inactive-value="0" active-color="#13ce66"
                                inactive-color="#ff4949" @change="handleStatusChange(row)" />
                        </template>
                    </el-table-column>
                    <el-table-column prop="createdTime" label="注册时间" width="180" />
                    <el-table-column label="操作" width="180" fixed="right">
                        <template #default="{ row }">
                            <el-button type="primary" size="small" :icon="Edit" @click="handleEdit(row)">编辑</el-button>
                            <el-button type="danger" size="small" :icon="Delete"
                                @click="handleDelete(row)">删除</el-button>
                        </template>
                    </el-table-column>
                </el-table>

                <!-- 分页 -->
                <div class="pagination-container">
                    <el-pagination v-model:current-page="currentPage" v-model:page-size="pageSize"
                        :page-sizes="[5]" :small="isMobile" :background="!isMobile"
                        layout="total, sizes, prev, pager, next, jumper" :total="filteredData.length"
                        @size-change="handleSizeChange" @current-change="handleCurrentChange" />
                </div>
            </el-card>
        </div>

        <el-dialog title="用户操作" v-model="dialogVisible" :lock-scroll="true">
            <UserDialog @cancel="()=>{dialogVisible=false}" :userData="userDate"/>
        </el-dialog>
    </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import {
    Plus, Refresh, Search, RefreshLeft, Edit, Delete
} from '@element-plus/icons-vue'

import { ElMessage, ElMessageBox } from 'element-plus'
import { getUserList, UserDelte, UserInfoVo, UserUpdate } from '../api/user'
import UserDialog from '../components/UserDialog.vue'

// 响应式数据
const userList = ref<UserInfoVo[]>([])
const dialogVisible = ref(false)
const filterForm = ref({
    username: '',
    role: null,
    status: '',
    dateRange: []
})
const currentPage = ref(1)
const pageSize = ref(5)
const loading = ref(false)
const isMobile = ref(window.innerWidth < 768)

const userDate=ref<UserInfoVo|null>(null)

const getDate = async () => {
    let temp = await getUserList()
    userList.value = temp ? temp : []
    if (userList.value?.length == 0) {
        ElMessage.warning('没有数据')
    }
}
getDate()
// 计算属性
const filteredData = computed(() => {
    let result = userList.value

    // 用户名筛选
    if (filterForm.value.username) {
        const username = filterForm.value.username.toLowerCase()
        result = result.filter(user =>
            user.username.toLowerCase().includes(username) ||
            user.realName?.toLowerCase().includes(username)
        )
    }

    // 权限筛选
    if (filterForm.value.role) {
        result = result.filter(user => user.role === Number(filterForm.value.role))
    }

    // 状态筛选
    if (filterForm.value.status) {
        result = result.filter(user => user.status === Number(filterForm.value.status))
    }

    // 日期范围筛选
    if (filterForm.value.dateRange && filterForm.value.dateRange.length === 2) {
        const [start, end] = filterForm.value.dateRange
        result = result.filter(user => {
            if (!user.createdTime) return false
            const userTime = new Date(user.createdTime).getTime()
            const startTime = new Date(start).getTime()
            const endTime = new Date(end).getTime()
            return userTime >= startTime && userTime <= endTime
        })
    }

    return result
})

const paginatedData = computed(() => {
    const start = (currentPage.value - 1) * pageSize.value
    const end = start + pageSize.value
    return filteredData.value.slice(start, end)
})

const stats = computed(() => {
    return {
        totalUsers: userList.value.length,
        totalAdmins: userList.value.filter(u => u.role > 0).length,
        activeUsers: userList.value.filter(u => u.status === 1).length,
        disabledUsers: userList.value.filter(u => u.status === 0).length
    }
})

// 方法
const handleSearch = () => {
    currentPage.value = 1
    ElMessage.success('筛选条件已应用')
}

const resetFilters = () => {
    filterForm.value = {
        username: '',
        role: null,
        status: '',
        dateRange: []
    }
    currentPage.value = 1
    ElMessage.info('筛选条件已重置')
}


const handleStatusChange = (user: UserInfoVo) => {
    // 实际项目中这里应该调用API更新状态
    UserUpdate(user)
    ElMessage.success(`用户 ${user.username} 状态已${user.status ? '启用' : '禁用'}`)
}

const handleEdit = (user: UserInfoVo) => {
    ElMessage.info(`正在编辑用户: ${user.username}`)
    // 实际项目中这里应该打开编辑对话框
    userDate.value=user
    dialogVisible.value=true
    console.log(userDate.value)
}
const handleAdd =()=>{
    userDate.value=null
    dialogVisible.value=true
}

const handleDelete = (user: UserInfoVo) => {
    ElMessageBox.confirm(
        `确定要删除用户 "${user.username}" 吗?`,
        '警告',
        {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
        }
    ).then(async () => {
        //做删除操作
        await UserDelte(user)
    }).catch(() => {
        ElMessage.info('已取消删除')
    })
}

const handleSizeChange = (val: number) => {
    pageSize.value = val
}

const handleCurrentChange = (val: number) => {
    currentPage.value = val
}

// 生命周期钩子
onMounted(() => {
    window.addEventListener('resize', () => {
        isMobile.value = window.innerWidth < 768
    })
})
</script>

<style scoped>
/* 基础样式 */
.user-management-container {
    padding: 20px;
    background-color: #f5f7fa;
    min-height: calc(100vh - 60px);
}

/* 头部区域 */
.header-section {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
}

.page-title {
    color: #303133;
    font-size: 22px;
    margin: 0;
}

/* 筛选区域 */
.filter-section {
    margin-bottom: 20px;
}

.filter-form {
    padding: 10px 0;
}

.filter-actions {
    display: flex;
    justify-content: flex-end;
    margin-top: 10px;
}

/* 统计卡片 */
.stats-section {
    margin-bottom: 20px;
}

.stat-card {
    background: #fff;
    border-radius: 4px;
    padding: 20px;
    text-align: center;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    transition: all 0.3s;
}

.stat-card:hover {
    transform: translateY(-5px);
    box-shadow: 0 4px 16px 0 rgba(0, 0, 0, 0.15);
}

.stat-value {
    font-size: 28px;
    font-weight: bold;
    color: #409eff;
    margin-bottom: 8px;
}

.stat-label {
    color: #909399;
    font-size: 14px;
}

.stat-card.admin .stat-value {
    color: #f56c6c;
}

.stat-card.active .stat-value {
    color: #67c23a;
}

.stat-card.disabled .stat-value {
    color: #e6a23c;
}

/* 表格区域 */
.table-section {
    max-height: 800px;
    margin: auto;
}

.pagination-container {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
}

/* 响应式调整 */
@media screen and (max-width: 768px) {
    .header-section {
        flex-direction: column;
        align-items: flex-start;
    }

    .actions {
        margin-top: 15px;
        width: 100%;
    }

    .filter-actions {
        justify-content: center;
    }

    .pagination-container {
        justify-content: center;
    }
}
</style>