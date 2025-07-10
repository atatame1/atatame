import { ElMessage } from "element-plus";
import axios from "../utils/http";

const checkToken = async (token: string | null) => {
    if (!token) return false;
    try {
        const res = await axios.get('/token', { params:{
            token: token
        } }); 
        if (res.data.code === 200) {
            return true;
        } else {
            ElMessage.error(res.data.message);
            return false;
        }
    } catch (error) {
        ElMessage.error("Token 验证失败");
        return false;
    }
};

export { checkToken };