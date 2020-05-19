package com.niuzhendong.frame.modules.sys.service;

import com.niuzhendong.frame.modules.sys.entity.SysUser;
import com.niuzhendong.frame.modules.sys.model.SysUserModel;

public interface SysUserService {
    SysUserModel findUserByName(String username);

    void save(SysUser sysUser);

    SysUser findById(Long id);
}
