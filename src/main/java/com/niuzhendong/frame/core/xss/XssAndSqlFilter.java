package com.niuzhendong.frame.core.xss;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * XssAndSqlFilter
 * @author niuzhendong
 */
public class XssAndSqlFilter implements Filter {

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String method = "GET";
        String param = "";
        XssAndSqlHttpServletRequestWrapper xssRequest = null;
        if (request instanceof HttpServletRequest) {
            method = ((HttpServletRequest) request).getMethod();
            xssRequest = new XssAndSqlHttpServletRequestWrapper((HttpServletRequest) request);
        }
        if ("POST".equalsIgnoreCase(method)) {
            param = XssAndSqlFilter.getBodyString(xssRequest.getReader());
            if(StringUtils.isNotBlank(param)){
                if(XssAndSqlHttpServletRequestWrapper.checkXSSAndSql(param)){
                    responseBody(response);
                    return;
                }
            }
        }
        if (xssRequest.checkParameter()) {
            responseBody(response);
            return;
        }
        chain.doFilter(xssRequest, response);
    }

    private void responseBody(ServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.write("您所访问的页面请求中有违反安全规则元素存在，拒绝访问!");
    }

    /**
     * 获取request请求body中参数
     * @param br
     * @return
     */
    public static String getBodyString(BufferedReader br) {
        String inputLine;
        String str = "";
        try {
            while ((inputLine = br.readLine()) != null) {
                str += inputLine;
            }
            br.close();
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }
        return str;

    }

}