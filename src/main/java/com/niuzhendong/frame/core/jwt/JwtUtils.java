package com.niuzhendong.frame.core.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

/**
 * JWT的Token生成工具类
 * @author niuzhendong
 */
public class JwtUtils {

    public static final String SECRET = "f2ea985e640aae55392cfe2e1ded562c";
    public static final Integer EXPIRE = 1000*60*60*2;
    public static final String HEADER = "jsbc-auth-token";

    /**
     * 生成 token
     *
     * @param id 主键id
     * @return 加密的token
     */
    public static String createToken(Long id) {
        try {
            Date expireDate = new Date(System.currentTimeMillis()+EXPIRE);
            return JWT
                    .create()
                    .withClaim("id",id)
                    //到期时间
                    .withExpiresAt(expireDate)
                    //创建一个新的JWT，并使用给定的算法进行标记
                    .sign(Algorithm.HMAC256(getKey()));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * 校验 token 是否正确
     *
     * @param token    密钥
     * @param id 主键id
     * @return 是否正确
     */
    public static boolean verify(String token, Long id) {
        try {
            //在token中附带了username信息
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(getKey()))
                    .withClaim("id", id)
                    .build();
            //验证 token
            verifier.verify(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     * 获得token中的信息，无需secret解密也能获得
     *
     * @return token中包含的主键id
     */
    public static String getId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("id").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 根据给定的字节数组使用AES加密算法构造一个密钥
     * @return
     */
    private static String getKey() {
        byte[] encodedKey = Base64.decodeBase64(SECRET);
        // 根据给定的字节数组使用AES加密算法构造一个密钥
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key.toString();
    }

    /**
     * 返回主键id
     * @param request
     * @return
     */
    public static String getId(HttpServletRequest request) {
        String token = request.getHeader(HEADER);
        if(token==null) {
            return null;
        }
        return getId(token);
    }

    /**
     * 解密
     * @param jsonWebToken
     * @param base64Security
     * @return
     */
    public static Claims parseJWT(String jsonWebToken, String base64Security) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(base64Security.getBytes())
                    .parseClaimsJws(jsonWebToken).getBody();
            return claims;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * 前三个参数为自己用户token的一些信息比如id，权限，名称等。不要将隐私信息放入（大家都可以获取到）
     * @param map
     * @param base64Security
     * @return
     */
    public static String createJWT(Map<String, Object> map, String base64Security) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        //添加构成JWT的参数
        JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT")
                .setPayload(new Gson().toJson(map))
                .signWith(signatureAlgorithm,base64Security.getBytes()); //估计是第三段密钥
        //生成JWT
        return builder.compact();
    }
}
