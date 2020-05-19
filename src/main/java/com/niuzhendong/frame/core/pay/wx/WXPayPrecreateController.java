//package com.niuzhendong.frame.core.pay.wx;
//import java.io.InputStreamReader;
//import java.util.Date;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.google.common.base.Charsets;
//import com.google.common.io.CharStreams;
//import com.jstv.reporter.common.GlobalConstants;
//import com.jstv.reporter.entity.PayLog;
//import com.jstv.reporter.entity.PubUser;
//import com.jstv.reporter.entity.SysUserModel;
//import com.jstv.reporter.service.PayLogService;
//import com.jstv.reporter.service.PubUserService;
//import com.jstv.reporter.service.SysUserService;
//import com.jstv.reporter.util.HttpUtils;
//import com.jstv.reporter.config.pay.WXPayUtil;
//import com.jstv.reporter.util.IdGen;
//import com.jstv.reporter.util.MD5Util;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.util.ObjectUtils;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import springfox.documentation.annotations.ApiIgnore;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.InputStream;
//import java.math.BigDecimal;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * 微信支付-扫码支付.
// * <p>
// * detailed description
// *
// * @author Mengday Zhang
// * @version 1.0
// * @since 2018/6/18
// */
//@Api(tags = "微信支付-扫码支付", value = "微信支付-扫码支付")
//@Slf4j
//@RestController
//@RequestMapping("/wxpay/precreate")
//public class WXPayPrecreateController {
//
//    @Autowired
//    PayLogService payLogService;
//
//    @Autowired
//    PubUserService pubUserService;
//
//    @Autowired
//    SysUserService sysUserService;
//
//    /**
//     * @param request
//     * @param code
//     * @return Map
//     * @Description 微信浏览器内微信支付/公众号支付(JSAPI)
//     */
//    @ApiOperation(notes = "公众号支付(JSAPI)", value = "公众号支付(JSAPI)")
//    @GetMapping("orders")
//    public Map orders(HttpServletRequest request, String code,String id) {
//        try {
//            // 向微信服务器发送get请求获取openIdStr
//            String openIdStr = HttpUtils.doGet(GlobalConstants.WEIXIN_ACCESS_TOKEN_URL, GlobalConstants.paramsString(code));
//            JSONObject json = JSONObject.parseObject(openIdStr);//转成Json格式
//            String openId = json.getString("openid");//获取openId
//            //拼接统一下单地址参数
//            Map<String, String> paraMap = new HashMap<String, String>();
//            //获取请求ip地址
//            String ip = request.getHeader("x-forwarded-for");
//            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//                ip = request.getHeader("Proxy-Client-IP");
//            }
//            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//                ip = request.getHeader("WL-Proxy-Client-IP");
//            }
//            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//                ip = request.getRemoteAddr();
//            }
//            if (ip.indexOf(",") != -1) {
//                String[] ips = ip.split(",");
//                ip = ips[0].trim();
//            }
//
//            paraMap.put("appid", "wx8a3b86c573aa85a8");
//            paraMap.put("body", "荔枝新闻小记者");
//            paraMap.put("mch_id", GlobalConstants.MCH_ID);
//            paraMap.put("nonce_str", WXPayUtil.generateNonceStr());
//            paraMap.put("openid", openId);
//            String outTradeNo = System.currentTimeMillis()+"";
//            paraMap.put("out_trade_no", outTradeNo);//订单号
//            paraMap.put("spbill_create_ip", ip);
//            paraMap.put("total_fee", GlobalConstants.TOTAL_FEE+"");
//            paraMap.put("trade_type", "JSAPI");
//            paraMap.put("notify_url", "https://hd5.jstv.com/reporter/wxpay/precreate/callback");// 此路径是微信服务器调用支付结果通知路径随意写
//            String sign = WXPayUtil.generateSignature(paraMap, "ad7a98330310615c49f5f37ecb329bd3");
//            paraMap.put("sign", sign);
//            String xml = WXPayUtil.mapToXml(paraMap);//将所有参数(map)转xml格式
//
//            // 统一下单 https://api.mch.weixin.qq.com/pay/unifiedorder
//            String unifiedorder_url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
//
//            String xmlStr = HttpUtils.doPost(unifiedorder_url, xml);//发送post请求"统一下单接口"返回预支付id:prepay_id
//            //以下内容是返回前端页面的json数据
//            String prepay_id = "";//预支付id
//            Map<String, String> map = null;
//            if (xmlStr.indexOf("SUCCESS") != -1) {
//                map = WXPayUtil.xmlToMap(xmlStr);
//                prepay_id = (String) map.get("prepay_id");
//            }
//            Map<String, String> payMap = new HashMap<String, String>();
//            payMap.put("appId", "wx8a3b86c573aa85a8");
//            payMap.put("timeStamp", WXPayUtil.getCurrentTimestamp() + "");
//            payMap.put("nonceStr", WXPayUtil.generateNonceStr());
//            payMap.put("signType", "MD5");
//            payMap.put("package", "prepay_id=" + prepay_id);
//            String paySign = WXPayUtil.generateSignature(payMap, "ad7a98330310615c49f5f37ecb329bd3");
//            payMap.put("paySign", paySign);
//
//            //支付记录入库
//            //先根据人员信息表主键和微信的OpenId查看是否已经有这条记录
//            PayLog savePayLog = payLogService.findByUserIdAndOpenId(id,openId);
//            if(ObjectUtils.isEmpty(savePayLog)) {
//                PayLog payLog = new PayLog();
//                payLog.setId(IdGen.uuid());
//                payLog.setUserId(id);
//                payLog.setOpenId(openId);
//                payLog.setOutTradeNo(outTradeNo);
//                payLog.setTotalFee(Double.valueOf(GlobalConstants.TOTAL_FEE*1.0/100));
//                payLog.setParaMap(JSON.toJSONString(paraMap));
//                payLog.setSignXml(xml);
//                payLog.setXmlStr(xmlStr);
//                payLog.setXmlMap(JSON.toJSONString(map));
//                payLog.setPayMap(JSON.toJSONString(payMap));
//                payLog.setPaid(0);
//                Date date = new Date();
//                payLog.setCreateTime(date);
//                payLogService.save(payLog);
//            }
//
//            return payMap;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//
//    }
//
//    /**
//     * @ClassName WxPayController
//     * @Description 微信支付成功后回调次接口
//     * @author wtao wangtao@eyaoshun.com
//     * @date 2018年1月11日 下午3:10:59
//     */
//    //回调路径是自己在之前已经填写过的
//    @ApiIgnore
//    @RequestMapping("callback")
//    public String callBack(HttpServletRequest request, HttpServletResponse response) {
//        InputStream is = null;
//        try {
//            is = request.getInputStream();//获取请求的流信息(这里是微信发的xml格式所有只能使用流来读)
//            String xml = CharStreams.toString(new InputStreamReader(is, Charsets.UTF_8));
//            Map<String, String> notifyMap = WXPayUtil.xmlToMap(xml);//将微信发的xml转map
//
//            if (notifyMap.get("return_code").equals("SUCCESS")) {
//                if (notifyMap.get("result_code").equals("SUCCESS")) {
//                    String ordersSn = notifyMap.get("out_trade_no");//商户订单号
//                    String amountpaid = notifyMap.get("total_fee");//实际支付的订单金额:单位 分
//                    BigDecimal amountPay = (new BigDecimal(amountpaid).divide(new BigDecimal("100"))).setScale(2);//将分转换成元-实际支付金额:元
//
//                    //根据订单号查询支付记录，设置为已支付
//                    PayLog savePayLog = payLogService.findByOutTradeNo(ordersSn);
//                    Date date = new Date();
//                    if(!ObjectUtils.isEmpty(savePayLog)) {
//                        savePayLog.setPaid(1);
//                        savePayLog.setUpdateTime(date);
//                    }
//
//                    //设置人员信息表中为已支付
//                    PubUser savePubUser = pubUserService.findById(savePayLog.getUserId());
//                    if(!ObjectUtils.isEmpty(savePubUser)) {
//                        savePubUser.setPaid(1);
//                        savePubUser.setUpdateTime(date);
//                        savePubUser.setSerial(ordersSn);
//                        pubUserService.save(savePubUser);
//                    }
//
//                    //创建账号
//                    SysUserModel sysUser = new SysUserModel();
//                    if(!ObjectUtils.isEmpty(sysUser)) {
//                        sysUser.setId(IdGen.uuid());
//                        sysUser.setUserId(savePayLog.getUserId());
//                        sysUser.setOpenId(savePayLog.getOpenId());
//                        sysUser.setPhone(savePubUser.getPhone());
//                        //密码加密
//                        sysUser.setIdCard(MD5Util.md5Encode(savePubUser.getIdCard(),""));
//                        sysUser.setCreateTime(date);
//                        sysUser.setUpdateTime(date);
//                        sysUserService.save(sysUser);
//                    }
//                }
//            }
//
//            //告诉微信服务器收到信息了，不要在调用回调action了========这里很重要回复微信服务器信息用流发送一个xml即可
//            response.getWriter().write("<xml><return_code><![CDATA[SUCCESS]]></return_code></xml>");
//            is.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//}
