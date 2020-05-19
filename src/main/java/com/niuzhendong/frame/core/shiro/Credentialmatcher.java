package com.niuzhendong.frame.core.shiro;

import com.niuzhendong.frame.modules.sys.model.SysUserModel;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 自定义密码校验规则
 * @author niuzhendong
 */
public class Credentialmatcher extends SimpleCredentialsMatcher {

    @Autowired
    PasswordHelper passwordHelper;

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        //获取session中的密码
        String password = new String(usernamePasswordToken.getPassword());
        //从Realm中传递过来的数据库中的密码
        String dbPassword = (String) info.getCredentials();
        SysUserModel sysUserModel = (SysUserModel) info.getPrincipals().getPrimaryPrincipal();

//        AtomicInteger retryCount = passwordRetryCache.get(username);
//        if (retryCount == null) {
//            retryCount = new AtomicInteger(0);
//            passwordRetryCache.put(username, retryCount);
//        }
//        // 自定义一个验证过程：当用户连续输入密码错误5次以上禁止用户登录一段时间
//        if (retryCount.incrementAndGet() > 5) {
//            throw new ExcessiveAttemptsException();
//        }
//        boolean match = super.doCredentialsMatch(token, info);
//        if (match) {
//            //匹配之后，从缓存中移除
//            passwordRetryCache.remove(username);
//        }


        return this.equals(passwordHelper.encryptPassword(password,sysUserModel.getCredentialsSalt()),dbPassword);
    }
}
