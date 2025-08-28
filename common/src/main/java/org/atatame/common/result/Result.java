package org.atatame.common.result;

import lombok.Data;
import org.atatame.common.enums.ResultEnum;

@Data
public class Result<T> {
    //状态码
    private Integer code;

    //提示信息
    private String message;

    //返回数据
    private T body;

    private static <T> Result<T> build(T body) {
        Result<T> result = new Result<T>();
        result.setBody(body);
        return result;
    }

    public static <T> Result<T> build(T body, ResultEnum resultEnum) {
        Result<T> result = build(body);
        result.setCode(resultEnum.getCode());
        result.setMessage(resultEnum.getMessage());
        return result;
    }
    public static <T> Result<T> build(T body, Integer code,String message) {
        Result<T> result = build(body);
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    public static <T> Result<T> build(Exception e) {
        Result<T> result = new Result<T>();
        result.setCode(201);
        result.setMessage(e.getMessage());
        return result;
    }

    public static <T> Result<T> ok(T body) {
        return build(body, ResultEnum.SUCCESS);
    }

    public static <T> Result<T> ok(String message,T body) {
        return build(body, 200,message);
    }

    public static <T> Result<T> ok() {
        return build(null, ResultEnum.SUCCESS);
    }

    public static <T> Result<T> error() {
        return build(null, ResultEnum.FAIL);
    }

    public static <T>Result<T> error(String message){
        return build(null,201,message);
    }

    public static <T> Result<T> error(ResultEnum resultEnum) {
        return build(null, resultEnum);
    }

    public static <T> Result<T> error(Exception error) {
        return build(error);
    }

}
