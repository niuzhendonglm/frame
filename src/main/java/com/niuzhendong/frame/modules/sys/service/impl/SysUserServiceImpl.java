package com.niuzhendong.frame.modules.sys.service.impl;

import com.niuzhendong.frame.modules.sys.entity.SysUser;
import com.niuzhendong.frame.modules.sys.mapper.SysUserMapper;
import com.niuzhendong.frame.modules.sys.model.SysUserModel;
import com.niuzhendong.frame.modules.sys.repository.SysUserRepository;
import com.niuzhendong.frame.modules.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    SysUserRepository sysUserRepository;

    @Autowired
    SysUserMapper sysUserMapper;

    @Override
    public SysUserModel findUserByName(String username) {
//        return sysUserRepository.findSysUserByUsername(username);
        return sysUserMapper.findByUsername(username);
    }

    @Override
    public void save(SysUser sysUser) {
        sysUserRepository.save(sysUser);
    }

    @Override
    public SysUser findById(Long id) {
        return sysUserRepository.getOne(id);
    }
}
