package com.niuzhendong.frame.modules.sys.model;

import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
public class SysUserModel implements Serializable {
    private static final long serialVersionUID = -3320971805590503443L;

    private long tsuId;

    private String username;
    private String password;
    private String salt;

    private Set<SysRoleModel> roles = new HashSet<>();

    public String getCredentialsSalt() {
        return username+salt+salt;
    }
}
