import { ElMessage } from "element-plus";
import axios from "../utils/http";

interface outputHeath{
    weight:number,//体重 单位kg

    height:number,//身高 单位cm

    bloodPressure:number, //血压 单位mmHg

    glucose: number; //血糖 mg/dL

    insulin: number; //胰岛素 unit

    createdTime:Date
    // 输入都不能为0
}
interface intputHeath{
    weight:number,//体重 单位kg

    height:number,//身高 单位cm

    bloodPressure:number, //血压 单位mmHg

    glucose: number; //血糖 mg/dL

    insulin: number; //胰岛素 unit
    // 输入都不能为0
}
const storeData=async (heath:intputHeath):Promise<Boolean>=>{
    const res=await axios.post('/health/storeData',heath)
    if(res.status!=200){
        ElMessage.error('状态码：'+res.status)
        return false
    }else{
        if(res.data.code!=200){
            ElMessage.error(res.data.message)
            return false
        }else{
            return true
        }
    }
}

const getData=async ():Promise<Array<outputHeath>|null>=>{
    const res=await axios.get('/health/getData')
    if(res.status!=200){
        ElMessage.error('状态码：'+res.status)
        return null
    }else{
        if(res.data.code!=200){
            ElMessage.error(res.data.message)
            return null
        }else{
            return res.data.body
        }
    }
}

export {outputHeath,intputHeath,storeData,getData}