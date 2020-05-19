package com.niuzhendong.frame.core.shiro;

import com.niuzhendong.frame.modules.sys.entity.SysUser;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.stereotype.Component;

/**
 * Shiro密码加密
 * @author niuzhendong
 */
@Component
public class PasswordHelper {
    private RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
    /**
     * 基础散列算法
     */
    public static final String ALGORITHM_NAME = "md5";
    /**
     * 自定义散列次数
     */
    public static final int HASH_ITERATIONS = 2;

    public void encryptPassword(SysUser sysUser) {
        // User对象包含最基本的字段Username和Password
        sysUser.setSalt(randomNumberGenerator.nextBytes().toHex());
        // 将用户的注册密码经过散列算法替换成一个不可逆的新密码保存进数据，散列过程使用了盐
        String newPassword = new SimpleHash(ALGORITHM_NAME,sysUser.getPassword(), ByteSource.Util.bytes(sysUser.getCredentialsSalt()),HASH_ITERATIONS).toHex();
        sysUser.setPassword(newPassword);
    }

    public String encryptPassword(String password,String credentialsSalt) {
        return new SimpleHash(ALGORITHM_NAME,password, ByteSource.Util.bytes(credentialsSalt),HASH_ITERATIONS).toHex();
    }
}
