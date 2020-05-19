package com.niuzhendong.frame.core.shiro;

import com.niuzhendong.frame.core.jwt.JwtFilter;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shiro配置类
 * @author niuzhendong
 */
@Configuration
public class ShiroConfig {

    /**
     * 项目启动shiroFilter首先会被初始化,并且逐层传入SecurityManager，Realm，matcher
     * @param manager
     * @return
     */
    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(@Qualifier("securityManager") SecurityManager manager){
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        // 必须设置 SecurityManager
        bean.setSecurityManager(manager);
        // 添加自己的过滤器并且取名为jwt
        Map<String, Filter> filterMap = new HashMap<>();
        //设置我们自定义的JWT过滤器
        filterMap.put("jwt", new JwtFilter());
        bean.setFilters(filterMap);

        //登陆界面
        bean.setLoginUrl("/login");
        //成功后页面
        bean.setSuccessUrl("/index");
        //无权限后的页面
        bean.setUnauthorizedUrl("/unauthorized");

        //键值对:请求-拦截器(权限配置)
        LinkedHashMap<String,String> filterChainDefinitonMap = new LinkedHashMap<>();

        /**不做身份验证*/
        //登陆不需要任何过滤
        filterChainDefinitonMap.put("/login","anon");
        filterChainDefinitonMap.put("/doLogin","anon");
        //注册
        filterChainDefinitonMap.put("/register","anon");
        //druid
        filterChainDefinitonMap.put("/druid/**","anon");
        //swagger
        filterChainDefinitonMap.put("/swagger-ui.html", "anon");
        filterChainDefinitonMap.put("/swagger-resources/**", "anon");
        filterChainDefinitonMap.put("/v2/**", "anon");
        filterChainDefinitonMap.put("/webjars/**", "anon");

        /**需要身份验证*/
        //anon	        无参，开放权限，可以理解为匿名用户或游客
        //authc	        无参，需要认证
        //logout	    无参，注销，执行后会直接跳转到shiroFilterFactoryBean.setLoginUrl(); 设置的 url
        //authcBasic	无参，表示 httpBasic 认证
        //user	        无参，表示必须存在用户，当登入操作时不做检查
        //ssl	        无参，表示安全的URL请求，协议为 https
        //perms[user]	参数可写多个，表示需要某个或某些权限才能通过，多个参数时写 perms["user, admin"]，当有多个参数时必须每个参数都通过才算通过
        //roles[admin]	参数可写多个，表示是某个或某些角色才能通过，多个参数时写 roles["admin，user"]，当有多个参数时必须每个参数都通过才算通过
        //rest[user]	根据请求的方法，相当于 perms[user:method]，其中 method 为 post，get，delete 等
        //port[8081]	当请求的URL端口不是8081时，跳转到schemal://serverName:8081?queryString 其中 schmal 是协议 http 或 https 等等，serverName 是你访问的 Host，8081 是 Port 端口，queryString 是你访问的 URL 里的 ? 后面的
        // 所有请求通过我们自己的JWT Filter
        filterChainDefinitonMap.put("/**", "jwt");
        // 访问 /unauthorized/** 不通过JWTFilter
        filterChainDefinitonMap.put("/unauthorized/**", "anon");
        //其他请求只验证是否登陆过
        //filterChainDefinitonMap.put("/**","authc");
        //首页地址index，使用authc过滤器进行处理
        filterChainDefinitonMap.put("/index","authc");
        //只有角色中拥有admin才能访问admin
        filterChainDefinitonMap.put("/admin","roles[admin]");
        //拥有edit权限
        filterChainDefinitonMap.put("/edit","perms[Update]");

        //放入Shiro过滤器
        bean.setFilterChainDefinitionMap(filterChainDefinitonMap);
        return bean;
    }

    /**
     * 将定义好的Realm放入安全会话中心
     * @param shiroRealm
     * @return
     */
    @Bean("securityManager")
    public SecurityManager securityManager(@Qualifier("shiroRealm") ShiroRealm shiroRealm){
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(shiroRealm);

        /*
         * 关闭shiro自带的session，详情见文档
         * http://shiro.apache.org/session-management.html#SessionManagement-StatelessApplications%28Sessionless%29
         */
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        manager.setSubjectDAO(subjectDAO);
        return manager;
    }

    /**
     * 将自定义的校验规格放入Realm
     * @param matcher
     * @return
     */
//    @Bean("shiroRealm")
//    public ShiroRealm shiroRealm(@Qualifier("credentialmatcher") Credentialmatcher matcher){
//        ShiroRealm shiroRealm = new ShiroRealm();
//        //信息放入缓存
//        shiroRealm.setCacheManager(new MemoryConstrainedCacheManager());
//        shiroRealm.setCredentialsMatcher(matcher);
//        return shiroRealm;
//    }

    /**
     * 校验规则
     * @return 校验实例
     */
//    @Bean("credentialmatcher")
//    public Credentialmatcher credentialmatcher(){
//        return new Credentialmatcher();
//    }

    /**
     * Spring与Shiro关联
     * @param manager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Qualifier("securityManager") SecurityManager manager){
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(manager);
        return advisor;
    }

    /**
     * 开启代理
     * @return
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        // 强制使用cglib，防止重复代理和可能引起代理出错的问题
        // https://zhuanlan.zhihu.com/p/29161098
        creator.setProxyTargetClass(true);
        return creator;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }
}
