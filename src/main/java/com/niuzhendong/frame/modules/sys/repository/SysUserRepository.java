package com.niuzhendong.frame.modules.sys.repository;

import com.niuzhendong.frame.modules.sys.entity.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SysUserRepository extends JpaRepository<SysUser,Long> {
}
