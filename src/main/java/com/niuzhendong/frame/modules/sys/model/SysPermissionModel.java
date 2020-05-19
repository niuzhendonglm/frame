package com.niuzhendong.frame.modules.sys.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class SysPermissionModel implements Serializable {
    private static final long serialVersionUID = 353629772108330570L;

    private Integer tspId;

    private String pname;

    private String url;
}

