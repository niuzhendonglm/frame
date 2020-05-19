package com.niuzhendong.frame.core.sms;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;

import java.io.IOException;
import java.util.Set;
import java.util.TreeMap;

/**
 * 
 * @Title: SmsUtils.java
 * @Description: 短信发送处理GET和POST内容的工具类
 * @Company: 江苏广电传媒有限公司
 * @author niuzhendong
 * @date 2018年9月18日 下午4:26:13
 * @版本 V 1.0
 */
public class SmsUtils {

    private String appSecret;

    public SmsUtils() {
        super();
    }

    public SmsUtils(String appSecret) {
        this.appSecret = appSecret;
    }

    /**
     * 
     * @Title: transformTT @Description: 获取TT @param time 传入当前的时间戳 @return int @throws
     */
    public static int transformTT(int time) {
        int[] b = NumericConvertUtils.int2ByteArray(time);
        int[] newByte = new int[4];
        for (int i = 0; i < b.length; i++) {
//            newByte[4 - i - 1] = (byte) ((~b[i] & 0xf0) | ((b[i] & 0x0f) + 0x01));
            newByte[4 - i - 1] = (((b[i] & 0xf0) ^ 0xf0) | (((b[i] & 0x0f) + 1) & 0x0f));
        }
        return NumericConvertUtils.byteArray2Int(newByte);
    }

    /**
     * 
     * @Title: md5Url @Description: 获取Sign（GET方式） @param url @return String @throws
     */
    public String md5Url(String url, int time) {
        String str = appSecret + url + time;
        return CommonUtils.getMd5Encode(str);
    }

    /**
     * 
     * @Title: md5Url @Description: 获取Sign（POST方式） @param url @param params @param time @return
     * String @throws
     */
    public String md5Url(String url, String params, int time) {
        String str = appSecret + url + params + time;
        return CommonUtils.getMd5Encode(str);
    }

    /**
     * 
     * @Title: encodeUrl @Description: 加密Url（GET方式） @param url @return String @throws
     */
    public String encodeUrl(String url) {
        String result = url;
        int time = (int) System.currentTimeMillis() / 1000;
        if (url.contains("=")) {
            result += "&";
        } else {
            result += "?";
        }
        return result + "TT=" + transformTT(time) + "&Sign=" + md5Url(url, time);
    }

    /**
     * 
     * @Title: encodeBody @Description: 加密body（POST方式） @param url @param body @return String @throws
     */
    public JSONObject encodeBody(String url, String body) {
        int time = (int) (System.currentTimeMillis() / 1000);
        long tt = transformTT(time);
        String sign = md5Url(url, getParams(body, new StringBuilder()), time);
        JSONObject parseObject = JSONObject.parseObject(body);
        parseObject.put("TT", tt);
        parseObject.put("Sign", sign);
        return parseObject;
    }

    /**
     * 
     * @Title: getParams @Description: 递归获取body，排序（POST方式） @param string @param result @return
     * String @throws
     */
    public String getParams(String string, StringBuilder result) {
        if (string.startsWith("{")) {
            //JSONObject parseObject = JSONObject.parseObject(string, Feature.OrderedField);
            TreeMap treeMap = JSONObject.parseObject(string, TreeMap.class);
            Set keySet = treeMap.keySet();
            for (Object object : keySet) {
                result.append(object);
                String value = String.valueOf(treeMap.get(object));
                if (value.startsWith("{") || value.startsWith("[")) {
                    getParams(value, result);
                } else {
                    result.append(value);
                }
            }
        } else if (string.startsWith("[")) {
            JSONArray parseArray = JSONArray.parseArray(string);
            for (Object object : parseArray) {
                if (String.valueOf(object).startsWith("{") || String.valueOf(object).startsWith("[")) {
                    getParams(String.valueOf(object), result);
                } else {
                    result.append(object);
                }
            }
        }
        return result.toString();
    }

    public static String sendVerificationCode(String phone) throws HttpException, IOException {
        HttpClient client = new HttpClient();
        PostMethod post = new PostMethod("http://sms.jstv.com/send");
        post.addRequestHeader("Content-Type", "application/json;charset=UTF-8");// 在头文件中设置转码
        SmsUtils smsUtils = new SmsUtils("G#A%C^cnr%9ph4!$GiF!$Bxz*^&cniCV");
        JSONObject encodeBody = smsUtils.encodeBody("/send", "{\"AppID\":\"LiChiNewsXiaoJiZhe\",\"MessageID\":\"100073\",\"Phone\":\"" + phone + "\"}");
        post.setRequestBody(encodeBody.toString());
        client.executeMethod(post);

        // 接收响应数据
        Header[] headers = post.getResponseHeaders();
        int statusCode = post.getStatusCode();
        System.out.println("statusCode:" + statusCode);
        for (Header h : headers) {
            System.out.println("---" + h.toString());
        }
        return new String(post.getResponseBodyAsString().getBytes("UTF-8"));
    }

    public static String sendUsernameAndPassword(String phone,String username,String password) throws HttpException, IOException {
        HttpClient client = new HttpClient();
        PostMethod post = new PostMethod("http://sms.jstv.com/send");
        post.addRequestHeader("Content-Type", "application/json;charset=UTF-8");// 在头文件中设置转码
        SmsUtils smsUtils = new SmsUtils("G#A%C^cnr%9ph4!$GiF!$Bxz*^&cniCV");
        JSONObject encodeBody = smsUtils.encodeBody("/send", "{\"AppID\":\"LiChiNewsXiaoJiZhe\",\"MessageID\":\"100070\",\"Phone\":\"" + phone + "\",\"Name\":\""+username+"\",\"Password\":\""+password+"\"}");
        post.setRequestBody(encodeBody.toString());
        client.executeMethod(post);

        // 接收响应数据
        Header[] headers = post.getResponseHeaders();
        int statusCode = post.getStatusCode();
        System.out.println("statusCode:" + statusCode);
        for (Header h : headers) {
            System.out.println("---" + h.toString());
        }
        return new String(post.getResponseBodyAsString().getBytes("UTF-8"));
    }

    public static String sendMessage(String phone,String message) throws HttpException, IOException {
        HttpClient client = new HttpClient();
        PostMethod post = new PostMethod("http://sms.jstv.com/send");
        post.addRequestHeader("Content-Type", "application/json;charset=UTF-8");// 在头文件中设置转码
        SmsUtils smsUtils = new SmsUtils("G#A%C^cnr%9ph5!$GiF!$Bxz*^&cniCV");
        JSONObject encodeBody = smsUtils.encodeBody("/send", "{\"AppID\":\"Unicom5GHeYing\",\"MessageID\":\"100074\",\"Phone\":\"" + phone + "\",\"Content\":\""+message+"\"}");
        post.setRequestBody(encodeBody.toString());
        client.executeMethod(post);

        // 接收响应数据
        Header[] headers = post.getResponseHeaders();
        int statusCode = post.getStatusCode();
        System.out.println("statusCode:" + statusCode);
        for (Header h : headers) {
            System.out.println("---" + h.toString());
        }
        return new String(post.getResponseBodyAsString().getBytes("UTF-8"));
    }

    public static void main(String[] args) throws IOException {
//        String[] phoneList = new String[]{"15195556868","18021317220","18021315706","13390663951","13270550191","13566665373","15062818972","13952568278","13390663952","13390663957","13390663958","13390663959","13390663911","13390663922","13390663933","15978931507","19923375597","18983381057","15502397190","18523506976","15683275908","13178259172","13023908970","13221337967","15602477821","15602437481","13243860147","15602446150","13249063695","16620784481","13249085147","13128966574","13249060492","17620435474","15602433047","17620465142","13249803265","17620394954","15626529274","16675540539","13040824256","13249051081","13025487343","13249847634","13249056817","18617033603","13049823901","17620347435","13249065407","13249078517","13249084092","17607616449","13249056752","17620468341","13249075821","17520457613","16675163843","15602475613","17520148509","13249821563","13249815921","15626582289","13249838429","17620476714","13249818727","16675545863","15602465273","13026614981","16675348209","13249080153","13265823574","16620952584","15626540858","13249842679","13249816214","17620478838","13040825182","13025483874","13249842816","15602486247","16620873945","17688540410","13040822903","15602439472","15602470842","13249818446","13189748164","15602486774","13249848623","16675541037","13249811654","13249815451","13129591974","15602473208","13249081048","15602475149","16620959544","13249837056","13168013047","16620796914","13265831463","17512058194","13249834350","15602484638","13129542734","15602443670","16620815470","13243861140","16675448271","13249825944","15602435482","16675390442","13249060851","13249849156","15602443857","15602447145","15602449082","13249079346","16675148952","13249834839","13249836438","17520467905","17620384904","13249057683","13058129145","18529567344","17620496894","15602464528","13249825084","13145993604","15602430643","16675594475","15602441275","15602449213","13068714254","13249844216","13249801076","17603026249","13249838529","13249818549","15602445236","16620887767","13249060134","13249848724","13249053057","15626584792","13242950494","15602479340","16675382944","13242052944"};
//        for (int i = 0; i < phoneList.length; i++) {
//            String s = phoneList[i];
//            sendMessage(s,"无敌");
//
//        }
        //15555028190 汤成
        String s = sendMessage("17856102959", "恭喜你中奖了！");
        System.out.println(s);
        //1574912114  1574813170
//        int i = transformTT(1574813170);
//        System.out.println(i);
    }
}
