import { ElMessage } from "element-plus";
import axios from "../utils/http";

interface PatientData {
    /** 年龄(岁) - 必须大于0 */
    age: number;
    /** 体重(kg) - 必须大于0 */
    weight: number;
    /** 身高(m) - 必须大于0 */
    height: number;
    /** 腰围(cm) - 必须大于0 */
    waist_circumference: number;
    /** 怀孕次数 - 默认值 0 */
    pregnancies?: number;
    /** 血糖(mg/dL) - 必须大于0 */
    glucose: number;
    /** 舒张压(mm Hg) - 必须大于0 */
    blood_pressure: number;
    /** 血清胰岛素(μU/mL) - 必须大于0 */
    insulin: number;

    /** 家族史信息 */
    family_history?: {
        parents: number; // 默认值 0
        /** 兄弟姐妹患病人数 */
        siblings: number; // 默认值 0
        /** (外)祖父母患病人数 */
        grandparents: number; // 默认值 0
        /** 叔伯姑姨舅患病人数 */
        aunts_uncles: number; // 默认值 0
        /** 是否有妊娠糖尿病史 (0/1) */
        has_gestational: 0 | 1; // 默认值 0
    };
}

interface Prediction{
    bmi:number,
    skin_thickness:number,
    dpf:number,
    probability:number,
    prediction:number,
    risk_level:String,
    medical_advice:String,
}

const url='http://10.70.5.94:8000/diabetes/predict'

const validatePatientData = (data: PatientData): string[] => {
  const errors: string[] = []
  
  // 基础验证
  if (data.age <= 0) errors.push('年龄必须大于0')
  if (data.weight <= 0) errors.push('体重必须大于0')
  if (data.height <= 0) errors.push('身高必须大于0')
  if (data.waist_circumference <= 0) errors.push('腰围必须大于0')
  if (data.glucose <= 0) errors.push('血糖值必须大于0')
  if (data.blood_pressure <= 0) errors.push('血压值必须大于0')
  if (data.insulin <= 0) errors.push('胰岛素值必须大于0')
  
  // 家族史验证（如果存在）
  if (data.family_history) {
    if (data.family_history.parents < 0) errors.push('父母患病人数不能为负数')
    if (data.family_history.siblings < 0) errors.push('兄弟姐妹患病人数不能为负数')
    if (data.family_history.grandparents < 0) errors.push('祖父母患病人数不能为负数')
    if (data.family_history.aunts_uncles < 0) errors.push('叔伯姑姨舅患病人数不能为负数')
    if (data.family_history.has_gestational !== 0 && data.family_history.has_gestational !== 1) {
      errors.push('妊娠糖尿病史必须为0或1')
    }
  }
  
  return errors
}
 

const predic=async(params:PatientData):Promise<Prediction|null>=>{
    try {
    // 验证数据
    const validationErrors = validatePatientData(params)
    if (validationErrors.length > 0) {
      console.error('数据验证失败:', validationErrors)
      return null
    }
    
    const res=await axios.post<Prediction|null>(url,params)
    if(res.status!=200){
      ElMessage.error('状态码：'+res.status)
      return null
    }
    return res.data

  } catch (error) {
    console.error('预测过程中发生错误:', error)
    return null
  }
}

export {PatientData,Prediction,predic}