package com.niuzhendong.frame.core.exception;

import org.apache.shiro.ShiroException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 错误处理Handler
 * @author niuzhendong
 */
@ControllerAdvice
public class ExceptionHandle {

    @ExceptionHandler(LoginException.class)
    public String loginException(Exception e, HttpServletRequest request) {
        Map<String,Object> map = new HashMap<>();
        //传入我们自己的错误状态码  4xx 5xx，否则就不会进入定制错误页面的解析流程
        request.setAttribute("javax.servlet.error.status_code",500);
        map.put("module","登录模块异常");
        map.put("message",e.getMessage());
        request.setAttribute("nzd",map);
        //转发到 /error
        return "forward:/error";
    }

    @ExceptionHandler(ShiroException.class)
    public String shiroException(Exception e, HttpServletRequest request) {
        Map<String,Object> map = new HashMap<>();
        //传入我们自己的错误状态码  4xx 5xx，否则就不会进入定制错误页面的解析流程
        request.setAttribute("javax.servlet.error.status_code",500);
        map.put("module","登录模块异常");
        map.put("message","没有权限访问");
        request.setAttribute("nzd",map);
        //转发到 /error
        return "forward:/error";
    }

}
