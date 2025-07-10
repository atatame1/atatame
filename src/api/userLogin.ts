import axios from "../utils/http";
import { ElMessage } from "element-plus";

const url = '/login'; // 登录和注册的API路径


const captcha= async () => {
    try{
        const response = await axios.get(url+'/captcha');
        if(response.status !== 200) {
            ElMessage.error("网络请求错误：" + response.status);
        }else{
            return {key:response.data.body.key,
                    url:response.data.body.url
            }; // 返回验证码图片的key和图片URL
        }
    } catch (error) {
        ElMessage.error("获取验证码失败，请稍后再试");
        console.error("获取验证码失败:", error);
        throw error; // 抛出错误以便在调用处处理
    }
}

const login = async (username: string, password: string,key:string,code:string) => {
    try {
        const response = await axios.post(url, {
            username: username,
            password: password,
            key: key,
            code: code
        });
        if(response.status !== 200) {
            ElMessage.error("网络请求错误：" + response.status);
        }else{
            if (response.data.code !== 200) {
                ElMessage.error(response.data.message || "登录失败，请检查用户名和密码");
                return null; // 登录失败，返回null
            }
            return response.data.body; // 返回登录结果
        }
    } catch (error) {
        ElMessage.error("登录失败，请检查用户名和密码");
        console.error("登录失败:", error);
        throw error; // 抛出错误以便在调用处处理
    }
}

const register = async (username: string, password: string, key: string, code: string) => {
    try {
        const response = await axios.post(url+'/submit', {
            username: username,
            password: password,
            key: key,
            code: code
        });
        if(response.status !== 200) {
            ElMessage.error("网络请求错误：" + response.status);
        }else{
            if (response.data.code == 301) {
                ElMessage.error(response.data.message);
                return null; // 登录失败，返回null
            }
            ElMessage.success("注册成功");
            return response.data.body; // 返回登录结果
        }
    } catch (error) {
        ElMessage.error("注册失败，请检查用户名和密码");
        console.error("注册失败:", error);
        throw error; // 抛出错误以便在调用处处理
    }
}

export {login,captcha,register};