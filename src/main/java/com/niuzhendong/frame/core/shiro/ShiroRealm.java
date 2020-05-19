package com.niuzhendong.frame.core.shiro;

import com.niuzhendong.frame.common.enums.LoginEnum;
import com.niuzhendong.frame.core.jwt.JwtToken;
import com.niuzhendong.frame.core.jwt.JwtUtils;
import com.niuzhendong.frame.modules.sys.entity.SysUser;
import com.niuzhendong.frame.modules.sys.model.SysPermissionModel;
import com.niuzhendong.frame.modules.sys.model.SysRoleModel;
import com.niuzhendong.frame.modules.sys.model.SysUserModel;
import com.niuzhendong.frame.modules.sys.service.SysUserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Shiro认证授权
 * @author niuzhendong
 */
@Component
public class ShiroRealm extends AuthorizingRealm {

    @Autowired
    SysUserService sysUserService;

    /**
     * 授权(取出Session中的User对象)
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //session中获取用户
        Long id = Long.valueOf(JwtUtils.getId(principalCollection.toString()));
        //DB获取用户的密码
        SysUser sysUser = sysUserService.findById(id);
        SysUserModel user = sysUserService.findUserByName(sysUser.getUsername());
        //权限集合
        List<String> permissionList = new ArrayList<>();
        //角色集合
        List<String> roleNameList = new ArrayList<>();
        Set<SysRoleModel> roleSet =  user.getRoles();
        if(CollectionUtils.isNotEmpty(roleSet)){
            for(SysRoleModel role : roleSet){
                roleNameList.add(role.getRname());
                Set<SysPermissionModel> permissionSet = role.getPermissions();
                if(CollectionUtils.isNotEmpty(permissionSet)){
                    for(SysPermissionModel permission : permissionSet){
                        //放入权限集合
                        permissionList.add(permission.getPname());
                    }
                }
            }
        }
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addStringPermissions(permissionList);
        info.addRoles(roleNameList);
        return info;
    }

    /**
     * 认证登陆(认证登陆成功后把User对应放入session中)
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) {
        String token = (String) authenticationToken.getCredentials();
        // 解密获得主键id，用于和数据库进行对比
        Long id = Long.valueOf(JwtUtils.getId(token));
        //DB获取用户的密码
        SysUser sysUser = sysUserService.findById(id);
        if (id == null || ObjectUtils.isEmpty(sysUser) || !JwtUtils.verify(token, id)) {
            throw new AuthenticationException(LoginEnum.TOKEN_AUTH_FAIL.getMessage());
        }

        return new SimpleAuthenticationInfo(token, token, ByteSource.Util.bytes(sysUser.getCredentialsSalt()),this.getClass().getName());
    }

    /**
     * 必须重写此方法，不然会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }
}
