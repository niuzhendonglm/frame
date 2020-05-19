package com.niuzhendong.frame.core.log;

import com.niuzhendong.frame.common.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 日志拦截器
 *
 * @author niuzhendong
 */
@Component
@Slf4j
public class LogIntercepter implements HandlerInterceptor {

    private static final ThreadLocal<Long> startTimeThreadLocal = new NamedThreadLocal<Long>("ThreadLocal StartTime");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (log.isDebugEnabled()) {
            long startTime = System.currentTimeMillis();
            startTimeThreadLocal.set(startTime);
            log.debug("开始计时：{}  URI：{}", DateUtils.formatDateTimeWithMillis(startTime), request.getRequestURL());
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {
            log.info("ViewName：" + modelAndView.getViewName());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (log.isDebugEnabled()) {
            Long startTime = startTimeThreadLocal.get();
            long endTime = System.currentTimeMillis();
            log.debug("结束计时：{}  耗时：{}  URL：{}  最大内存：{}m  已分配内存：{}m  已分配内存中的剩余空间：{}m  最大可用内存：{}m",
                    DateUtils.formatDateTimeWithMillis(endTime), DateUtils.formatDateTime(endTime - startTime),
                    request.getRequestURL(), Runtime.getRuntime().maxMemory() / 1024 / 1024, Runtime.getRuntime().totalMemory() / 1024 / 1024,
                    Runtime.getRuntime().freeMemory() / 1024 / 1024, (Runtime.getRuntime().maxMemory() - Runtime.getRuntime().totalMemory() + Runtime.getRuntime().freeMemory()) / 1024 / 1024);
        }
    }
}
