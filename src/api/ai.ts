import axios from "axios";
import { ElMessage } from "element-plus";

const url='http://10.70.5.94:8000/ai'

const getAnswer=async(question:String):Promise<String|null>=>{
    const res=await axios.post(url,{
        question:question
    })
    if(res.status!=200){
        ElMessage.error('错误码：'+res.status,)
        return null
    }
    return res.data.response
}

export default getAnswer