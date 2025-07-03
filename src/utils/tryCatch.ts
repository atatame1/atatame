import { ElMessage } from "element-plus";

type ErrorHandler = (error: unknown) => void;

/**
 * 封装 try-catch 的工具函数
 * @param fun 要执行的函数（支持同步/异步）
 * @param onError 自定义错误处理（字符串或函数）
 * @returns 成功时返回函数结果，失败时返回 null
 */
export default async function tryCatch<T>(
    fun: () => Promise<T> | T,
    onError?: ErrorHandler | string
): Promise<T | null> {
    try {
        return await fun();
    } catch (error) {
        const handleError = () => {
            if (typeof onError === "function") {
                onError(error);
            } else {
                ElMessage.error(onError || "操作失败，请稍后再试");
            }
        };
        return null;
    }
}