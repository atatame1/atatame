import { ElMessage } from "element-plus";
import axios from "../utils/http";



interface UserInfoVo {
    id?: string|null
    username: string|null;
    password: string|null;
    role: number|null; // 0=用户, 1=管理员
    realName: string|null;
    birthday: Date | null; // 
    gender: number; // 0=未知 1=女, 2=男
    occupation: string|null;
    createdTime: Date|null;
    faceImg: string|null;
    status: number; // 0=禁用, 1=启用 
}
const InitUserInfo=<UserInfoVo>{
    id:null,
    username: null,
    password: null,
    role: null,// 0=用户, 1=管理员
    realName: null,
    birthday: null,
    gender: 0, // 0=未知 1=女, 2=男
    occupation: null,
    faceImg: null,
    status:1,
    createdTime:null
}
const getUser = async (): Promise<UserInfoVo | null> => {
    try {
        const res = await axios.get('/user/getUser')
        if (res.status !== 200) {
            ElMessage.error("网络请求错误：" + res.status);
            return null; // 返回null表示获取失败
        } else {
            if (res.data.code !== 200) {
                ElMessage.error(res.data.message);
                return null;
            } else {
                return res.data.body as UserInfoVo; // 返回用户列表数据
            }
        }
    } catch (error) {
        ElMessage.error("获取个人信息失败:" + error);
        return null; // 返回null表示获取失败
    }
}

const uploadImage = async (file:File): Promise<string | null> => {
    try {
            const formData = new FormData()
            formData.append('file', file)
            const res = await axios.post('/user/uploadImage',formData,{
                headers:{
                    'Content-Type': 'multipart/form-data'
                }
            })
        if (res.status !== 200) {
            ElMessage.error("网络请求错误：" + res.status);
            return null; // 返回null表示获取失败
        } else {
            if (res.data.code !== 200) {
                ElMessage.error(res.data.message);
                return null;
            } else {
                return res.data.body as string|null; // 返回用户列表数据
            }
        }
    } catch (error) {
        ElMessage.error("头像上传失败:" + error);
        return null; // 返回null表示获取失败
    }
} 
const UserUpdateSelf = async (userinfo: UserInfoVo): Promise<boolean> => {
    try {
        const res = await axios.post('/user/updateUser', userinfo)
        if (res.status !== 200) {
            ElMessage.error("网络请求错误：" + res.status);
            return false; // 返回null表示获取失败
        } else {
            if (res.data.code !== 200) {
                ElMessage.error(res.data.message);
                return false;
            } else {
                ElMessage.success("更新成功")
                return true// 返回用户列表数据
            }
        }
    } catch (error) {
        ElMessage.error("更新失败:" + error);
        return false; // 返回null表示获取失败
    }
}

const getUserList = async (): Promise<UserInfoVo[] | null> => {
    try {
        const res = await axios.get('/user/admin/getAll')
        if (res.status !== 200) {
            ElMessage.error("网络请求错误：" + res.status);
            return null; // 返回null表示获取失败
        } else {
            if (res.data.code !== 200) {
                ElMessage.error(res.data.message);
                return null;
            } else {
                ElMessage.success("获取成功")
                return res.data.body as Array<UserInfoVo>; // 返回用户列表数据
            }
        }
    } catch (error) {
        ElMessage.error("获取用户列表失败:" + error);
        return null; // 返回null表示获取失败
    }
}
const UserUpdate = async (userinfo: UserInfoVo): Promise<boolean> => {
    try {
        const res = await axios.post('/user/admin/updateUser', userinfo)
        if (res.status !== 200) {
            ElMessage.error("网络请求错误：" + res.status);
            return false; // 返回null表示获取失败
        } else {
            if (res.data.code !== 200) {
                ElMessage.error(res.data.message);
                return false;
            } else {
                ElMessage.success("更新成功")
                return true// 返回用户列表数据
            }
        }
    } catch (error) {
        ElMessage.error("更新失败:" + error);
        return false; // 返回null表示获取失败
    }
}

const UserAdd = async (userinfo: UserInfoVo): Promise<boolean> => {
    try {
        if(userinfo.username==''||userinfo.password==''){
            ElMessage.error("账号密码不能为空");
            return false
        }
        const res = await axios.post('/user/admin/addUser', userinfo)
        if (res.status !== 200) {
            ElMessage.error("网络请求错误：" + res.status);
            return false; // 返回null表示获取失败
        } else {
            if (res.data.code !== 200) {
                ElMessage.error(res.data.message);
                return false;
            } else {
                ElMessage.success("添加成功")
                return true// 返回用户列表数据
            }
        }
    } catch (error) {
        ElMessage.error("添加失败:" + error);
        return false; // 返回null表示获取失败
    }
}
const UserDelte = async (userinfo: UserInfoVo): Promise<boolean> => {
    try {
        if(userinfo.id==null||userinfo.id==''){
            ElMessage.error("id不能为空");
            return false
        }
        const res = await axios.delete('/user/admin/deleteUser', {
            params:{
                id:userinfo.id
            }
        })
        if (res.status !== 200) {
            ElMessage.error("网络请求错误：" + res.status);
            return false; // 返回null表示获取失败
        } else {
            if (res.data.code !== 200) {
                ElMessage.error(res.data.message);
                return false;
            } else {
                ElMessage.success("删除成功")
                return true// 返回用户列表数据
            }
        }
    } catch (error) {
        ElMessage.error("删除失败:" + error);
        return false; // 返回null表示获取失败
    }
}


export { getUserList, UserUpdate, UserAdd, UserDelte,UserInfoVo ,InitUserInfo,getUser,uploadImage,UserUpdateSelf}