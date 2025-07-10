import { ElMessage } from "element-plus";
import axios from "../utils/http";


const getFunctionByToken = async () => {
    const rse = await axios.get('/function/getFunctionByToken')
    if (rse.status == 200) {
        if (rse.data.code !== 200) {
            ElMessage.error(rse.data.message);
            return [];
        }
    }
    return rse.data.body || [];
}

export default getFunctionByToken;