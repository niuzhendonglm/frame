package com.niuzhendong.frame.common.enums;

import lombok.Getter;

@Getter
public enum  LoginEnum {
    USERNAME_ERROR(1,"用户名错误"),
    PASSWORD_ERROR(2,"密码错误"),
    TOKEN_AUTH_FAIL(3,"token认证失败")
    ;

    private Integer code;
    private String message;

    LoginEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
