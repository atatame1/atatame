package org.atatame.common.enums;

import lombok.Getter;

@Getter
public enum ResultEnum {
    SUCCESS(200,"操作成功"),
    FAIL(201,"操作失败"),
    OPERATION_FREQUENT(202,"请求过于频繁"),
    OPERATION_TIMEOUT(203,"请求超时"),

    USER_EXISTED(301,"用户已存在"),
    USER_NOT_EXISTED(306,"用户不存在"),
    USER_IS_BANED(309,"用户已被禁用"),
    ACCOUNT_PASSWORD_ERROR(302,"用户名或密码错误"),
    ACCOUNT_EMPTY(303,"用户名或密码错不能为空"),
    CAPTCHA_EXPIRED(304,"验证码过期"),
    CAPTCHA_ERROR(305,"验证码错误"),
    TOKEN_NOT_VALIDATE(307, "令牌非法"),
    TOKEN_EMPTY(308,"空令牌"),
    LIMITED_AUTHORITY(310,"权限不够"),
    GOOD_NOT_EXIST(320,"商品不存在"),
    GOOD_NOT_ENOUGH(321,"商品库存不足"),

    PARAMS_EMPTY(400,"参数不能为空");



    private final Integer code;
    private final String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
