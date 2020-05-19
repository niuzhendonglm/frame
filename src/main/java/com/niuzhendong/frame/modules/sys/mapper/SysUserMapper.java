package com.niuzhendong.frame.modules.sys.mapper;

import com.niuzhendong.frame.modules.sys.model.SysUserModel;
import org.apache.ibatis.annotations.Param;

/**
 *
 * @author niuzhendong
 */
public interface SysUserMapper {

    SysUserModel findByUsername(@Param("username") String username);
}
