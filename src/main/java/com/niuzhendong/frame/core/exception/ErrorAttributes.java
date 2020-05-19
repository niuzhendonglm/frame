package com.niuzhendong.frame.core.exception;

import com.niuzhendong.frame.common.util.DateUtils;
import com.niuzhendong.frame.common.util.collection.MapUtil;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.Map;

/**
 * 页面共享信息
 * @author niuzhendong
 */
@Component
public class ErrorAttributes extends DefaultErrorAttributes {
    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
        Map<String, Object> errorAttributesMap = super.getErrorAttributes(webRequest, includeStackTrace);
        errorAttributesMap.replace("timestamp", DateUtils.formatDate((Date)errorAttributesMap.get("timestamp")));
        errorAttributesMap.put("company","jsbc");
        Map<String,Object> nzd = (Map<String,Object>) webRequest.getAttribute("nzd", 0);
        errorAttributesMap.put("nzd",nzd);
        errorAttributesMap.put("success",false);
        errorAttributesMap.put("code",errorAttributesMap.get("status"));
        errorAttributesMap.remove("trace");
        if(MapUtil.isNotEmpty(nzd)&&nzd.containsKey("message")) {
            errorAttributesMap.replace("message",nzd.get("message"));
        }
        return errorAttributesMap;
    }
}
