package com.niuzhendong.frame.common.util.returns;

import java.util.LinkedHashMap;

/**
 * 返回信息的json封装
 * @author niuzhendong
 */
public class ReturnVO {

    /**
     * 是否成功
     */
    private Boolean success;
    /**
     * 错误代码
     */
    private Integer code;
    /**
     * 提示信息
     */
    private String message;
    /**
     * 封装json的map
     */
    private LinkedHashMap<String, Object> body = new LinkedHashMap<String, Object>();

    public LinkedHashMap<String, Object> getBody() {
        return body;
    }

    public void setBody(LinkedHashMap<String, Object> body) {
        this.body = body;
    }

    /**
     * 向json中添加属性，在js中访问，请调用data.map.key
     * @param key
     * @param value
     */
    public void put(String key, Object value){
        body.put(key, value);
    }

    public void remove(String key){
        body.remove(key);
    }

    public String getMessage() {
        return message;
    }

    /**
     * 向json中添加属性，在js中访问，请调用data.msg
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}