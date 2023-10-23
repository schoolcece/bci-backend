package com.hcc.common.utils;

import io.jsonwebtoken.*;
import org.springframework.util.StringUtils;
import java.util.Date;

public class JwtUtils {

    // 过期时间 单位毫秒
    private static long tokenExpiration = 30 * 60 * 1000;

    // 密钥，使用随机生成，做加密操作
    private static String tokenSignKey = "ukc8BDbRigUDaY6pZFfWus2jZWLPHO";

    public static String createToken(int userId, String userName) {
        String token = Jwts.builder()

                // 设置主体
                .setSubject("jwt-login")

                // 设置超时时间
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))

                // 设置token的主题部分，存储用户的信息
                .claim("userId", userId)
                .claim("userName", userName)

                // 设置JWT的头信息
                .signWith(SignatureAlgorithm.HS512, tokenSignKey)
                .compressWith(CompressionCodecs.GZIP)
                .compact();
        return token;
    }

    // 得到token里的userId 封装在header里 可修改定制
    public static Integer getUserId(String token) {
        if (StringUtils.isEmpty(token)) return null;
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        Integer userId = (Integer) claims.get("userId");
        return userId;
    }

    // 得到token里的userId 可修改定制
//    public static Long getUserId(HttpServletRequest request) {
//
//        String token = request.getHeader("token");
//        if (StringUtils.isEmpty(token)) return null;
//        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
//        Claims claims = claimsJws.getBody();
//        Integer userId = (Integer) claims.get("userId");
//        return userId.longValue();
//    }

    // 得到token里的userName 可修改定制
    public static String getUserName(String token) {
        if (StringUtils.isEmpty(token)) return "";
        Jws<Claims> claimsJws
                = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        return (String) claims.get("userName");
    }

    // 得到token里的userName 封装在header里 可修改定制
//    public static String getUserName(HttpServletRequest request) {
//        String token = request.getHeader("token");
//        if (StringUtils.isEmpty(token)) return null;
//        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
//        Claims claims = claimsJws.getBody();
//        String userName = (String) claims.get("userName");
//        return userName;
//    }


    // 判断token是否存在与有效
    public static boolean checkToken(String jwtToken) {
        if (StringUtils.isEmpty(jwtToken)) return false;
        try {
            Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(jwtToken);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    // 判断token是否存在与有效 封装在header里
//    public static boolean checkToken(HttpServletRequest request) {
//        try {
//            String jwtToken = request.getHeader("token");
//            if (StringUtils.isEmpty(jwtToken)) return false;
//            Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(jwtToken);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//        return true;
//    }

//    public static void main(String[] args) {
//        String token = JwtUtils.createToken(1L, "czs");
//        System.out.println(token);
//        if (checkToken(token)) {
//            System.out.println(JwtUtils.getUserId(token));
//            System.out.println(JwtUtils.getUserName(token));
//        } else {
//            System.out.println("token已过期");
//        }
//    }
}
