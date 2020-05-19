package com.niuzhendong.frame.common.enums;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * 压缩文件类型
 * @author niuzhendong
 * @date 2019/10/14
 */
@AllArgsConstructor
@NoArgsConstructor
public enum FileTypeEnum {
    FILE_TYPE_ZIP("application/x-zip-compressed", ".zip"),
    FILE_TYPE_RAR("application/octet-stream", ".rar");
    public String type;
    public String fileStufix;

    public static String getFileStufix(String type) {
        for (FileTypeEnum orderTypeEnum : FileTypeEnum.values()) {
            if (orderTypeEnum.type.equals(type)) {
                return orderTypeEnum.fileStufix;
            }
        }
        return null;
    }
}
