package com.niuzhendong.frame.core.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * LogConfig
 * @author niuzhendong
 */
@Configuration
public class LogConfig implements WebMvcConfigurer {

    @Autowired
    private LogIntercepter logIntercepter;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logIntercepter).addPathPatterns("/**");
    }
}
