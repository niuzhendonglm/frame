//package com.niuzhendong.frame.common.util;
//
//import com.alibaba.druid.support.json.JSONUtils;
//import com.jsbc.lottery.entity.Prize;
//import com.jsbc.lottery.entity.Users;
//import org.springframework.util.CollectionUtils;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//
///**
// * @Title: SendRequestUtil
// * @Description 发送请求服务层实现类
// * @Company: 江苏广电传媒有限公司
// * @Author niuzhendong
// * @Date 2019/1/21
// * @Version 1.0
// */
//public class SendRequestUtil {
//
//    /**
//     * @Title: sendRequest
//     * @Description 发送请求，获取响应
//     * @Param [user]
//     * @Return java.lang.String
//     * @Author niuzhendong
//     * @Date 2019/1/21
//     */
//    public static String sendRequest(Users user, Prize prize) {
//
//        // 构造请求参数
//        HashMap map1 = new HashMap<String, Object>();
//        HashMap map2 = new HashMap<String, Object>();
//        HashMap map31 = new HashMap<String, Object>();
//        HashMap map32 = new HashMap<String, Object>();
//        HashMap map4 = new HashMap<String, Object>();
//        HashMap map51 = new HashMap<String, Object>();
//        HashMap map61 = new HashMap<String, Object>();
//
//        map4.put("UNI_BSS_BODY", map51);
//        map51.put("SEND_COUPONS_REQ", map61);
//        map61.put("ACT_TYPE", "e");
//        map61.put("MCODE", "101400");
//        map61.put("MID", "00000000000000");
//
//        // 获取当前时间
//        String temp_str = "";
//        Date dt = new Date();
//        // 最后的aa表示“上午”或“下午” HH表示24小时制 如果换成hh表示12小时制
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HHmmss");
//        temp_str = sdf.format(dt);
//        String[] s1 = temp_str.split(" ");
//
//        map61.put("DATE", s1[0]);
//        map61.put("TIME", s1[1]);
//        map61.put("MSGPRO", "SF");
//        map61.put("VER", "0001");
//        map61.put("SAFEFLG", "00");
//        map61.put("ACT_ID", prize.getPrizeTid());// 奖品编码
//        map61.put("MBL_NO", user.getPhone());// 用户手机号
//
//        map61.put("PROV_CD", "34");
//        map61.put("CITY_CD", "340");
//        map61.put("BON_AMT", prize.getPrizePrice());// 奖品金额
//
//        // 获取uuid作为流水号
//        String uuid = UUID.randomUUID().toString();
//        map61.put("TLOG_NO", uuid.replaceAll("-", ""));
//
//        map61.put("TCNL_TYP", "34");
//        map61.put("TTXN_DT", s1[0]);
//
//        map61.put("IN_MODE", "1");
////        map61.put("API_KEY", "5f2e411ecb944303bd0b3910b44da3cc");
//        map61.put("API_KEY", "6673315a7d568461a83bb1435005f530");
//
//        // "$"对应的内容加密
//        String keyString = "1000010620150706142758KGOE";// 加密规则
//        BlowfishUtil crypt = new BlowfishUtil(keyString);
//        String str = JSONUtils.toJSONString(map4);
//        String tempStr = crypt.encryptString(str);
//        map32.put("$", tempStr);
//
//        map31.put("APP_SECRET", "B931DD1CF4958758F4CCA7348CA0D893");
//        map31.put("PROCESS_TIME", s1[0] + s1[1]);// 当前时间
//        map31.put("APP_KEY", keyString);// 加密规则
//        map31.put("TRANS_REQ_ID", "2015030300200302");
//        map31.put("RSP_CODE", "");
//        map31.put("RSP_DESC", "");
//
//        map2.put("HEAD", map31);
//        map2.put("BODY", map32);
//
//        map1.put("INPUT", map2);
//
//        String param = JSONUtils.toJSONString(map1);
//        // 生产地址
//        String url = "http://122.194.14.181:8088/aaop/SENDCOUPONS";
////        String url = "http://132.224.18.49:8888/server-web/rest/SENDCOUPONS";
//
//        // 发送请求，获取响应
//        String s = HttpRequests.sendRequest(url, param);
//        String result;
//
//        // 获取"BODY"的内容，解密
//        if (null != s && s.length() > 0) {
//            Map<String, Object> outMap1 = (Map) JSONUtils.parse(s);
//            Map<String, Object> outMap2 = (Map) outMap1.get("OUTPUT");
//            if (null != outMap2 & outMap2.size() > 0) {
//                String output = (String) outMap2.get("BODY");
//                if (!StringUtils.isEmpty(output)){
//                    result = crypt.decryptString(output);
//                    outMap2.put("BODY", result);
//                }else{
//                    outMap2.put("BODY", "");
//                }
//            }
//            return JSONUtils.toJSONString(outMap1);
//        }
//
//        return s;
//    }
//
//    /**
//     * @Title: drawLottery
//     * @Description 判断抽奖结果是成功还是失败
//     * @Param [user]
//     * @Return java.lang.Boolean
//     * @Author niuzhendong
//     * @Date 2019/1/23
//     */
//    public static Boolean drawLottery(Users user, Prize prize) {
//        String s = sendRequest(user,prize);
//
//        if(StringUtils.isEmpty(s)){
//            return false;
//        }
//        Map<String, Object> outMap1 = (Map) JSONUtils.parse(s);
//
//        if(CollectionUtils.isEmpty(outMap1)){
//            return false;
//        }
//        Map<String, Object> outMap2 = (Map) outMap1.get("OUTPUT");
//
//        if(CollectionUtils.isEmpty(outMap2)){
//            return false;
//        }
//        // "BODY"取出的值不是map，是string，需要转成map
//        String outMapStr = (String) outMap2.get("BODY");
//
//        if(StringUtils.isEmpty(outMapStr)){
//            return false;
//        }
//        Map<String,Object> outMap3 = (Map)JSONUtils.parse(outMapStr);
//
//        if (CollectionUtils.isEmpty(outMap3)){
//            return false;
//        }
//        Map<String, Object> outMap4 = (Map) outMap3.get("UNI_BSS_BODY");
//
//        if(CollectionUtils.isEmpty(outMap4)){
//            return false;
//        }
//        Map<String, Object> outMap5 = (Map) outMap4.get("SEND_COUPONS_RSP");
//
//        if(CollectionUtils.isEmpty(outMap5)){
//            return false;
//        }
//        String rcode = (String) outMap5.get("RCODE");
//
//        if (StringUtils.isEmpty(rcode) || !"MKM00000".equals(rcode)){
//            return false;
//        }
//
//        return true;
//    }
//
//    public static void main(String[] args) {
//        Users users = new Users();
//        users.setPhone("17856102959");
//        Prize prize = new Prize();
////        prize.setPrizeTid("342019qBDG");
//        prize.setPrizeTid("3420199WMg");
//        prize.setPrizePrice("100");
//        Boolean aBoolean = SendRequestUtil.drawLottery(users,prize);
//        if(!aBoolean) {
//            System.err.println("没有成功发送短信的用户   手机号："+users.getPhone()+"  奖品的code："+prize.getPrizeTid());
//        }
//
//    }
//}
