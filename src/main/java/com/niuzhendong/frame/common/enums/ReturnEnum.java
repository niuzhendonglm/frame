package com.niuzhendong.frame.common.enums;

import lombok.Getter;

/**
 * 返回数据的枚举
 * @author niuzhendong
 */
@Getter
public enum ReturnEnum {
    RETURN_TRUE_MSG(1,"成功");
    private Integer code;
    private String msg;

    ReturnEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}