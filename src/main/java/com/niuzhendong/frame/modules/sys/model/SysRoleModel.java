package com.niuzhendong.frame.modules.sys.model;

import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
public class SysRoleModel implements Serializable {
    private static final long serialVersionUID = -8687790154329829056L;

    private Integer tsrId;

    private String rname;

    private Set<SysPermissionModel> permissions = new HashSet<>();
}
