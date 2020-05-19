package com.niuzhendong.frame.common.util.returns;

import com.niuzhendong.frame.common.enums.ReturnEnum;

/**
 * 返回json的工具类
 * @author niuzhendong
 */
public class ReturnVOUtils {

    public static ReturnVO success() {
        ReturnVO returnVO = new ReturnVO();
        returnVO.setSuccess(true);
        returnVO.setCode(200);
        returnVO.setMessage(ReturnEnum.RETURN_TRUE_MSG.getMsg());
        return returnVO;
    }

    public static ReturnVO success(Object object) {
        ReturnVO returnVO = new ReturnVO();
        returnVO.setSuccess(true);
        returnVO.setCode(200);
        returnVO.setMessage(ReturnEnum.RETURN_TRUE_MSG.getMsg());
        returnVO.put("result",object);
        return returnVO;
    }

    public static ReturnVO error(Integer code,String msg) {
        ReturnVO returnVO = new ReturnVO();
        returnVO.setSuccess(false);
        returnVO.setCode(code);
        returnVO.setMessage(msg);
        return returnVO;
    }
}