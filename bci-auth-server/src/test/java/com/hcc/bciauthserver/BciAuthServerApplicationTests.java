package com.hcc.bciauthserver;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcc.bciauthserver.controller.TeamController;
import com.hcc.bciauthserver.entity.RegisterInfo;
import com.hcc.bciauthserver.mapper.TeamRightMapper;
import com.hcc.common.utils.AesUtil;
import com.hcc.common.utils.R;
import com.hcc.common.vo.UserInfo;
import org.bouncycastle.crypto.util.OpenSSHPrivateKeyUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.Base64Utils;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;

@SpringBootTest
class BciAuthServerApplicationTests {


    @Autowired
    TeamController teamController;
    @Autowired
    TeamRightMapper teamRightMapper;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Test
    void contextLoads() {
    }

    @Test
    void test() throws Exception {
        String key = redisTemplate.opsForValue().get("eee");
        if (key != null){
            Boolean delete = redisTemplate.delete(key);
            System.out.println(delete);
        }


    }





    public static String decrypt(String content, String key) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128, new SecureRandom(key.getBytes()));
            SecretKey secretKey = kgen.generateKey();
            byte[] encodedFormat = secretKey.getEncoded();
            SecretKeySpec keySpec = new SecretKeySpec(encodedFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            String decode = URLDecoder.decode(content, "UTF-8");
            byte[] encryptedBytes = Base64.getDecoder().decode(decode);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            return new String(decryptedBytes, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encrypt(String plaintext, String key) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128, new SecureRandom(key.getBytes()));
        SecretKey secretKey = kgen.generateKey();
        byte[] encodedFormat = secretKey.getEncoded();
        SecretKeySpec keySpec = new SecretKeySpec(encodedFormat, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] encrypted = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
        String base64Encoded = Base64.getEncoder().encodeToString(encrypted);
        String urlEncoded = java.net.URLEncoder.encode(base64Encoded, String.valueOf(StandardCharsets.UTF_8));
        return urlEncoded;
    }

    public static String encryptData(Object data, String secretKey) throws Exception {
        if (data instanceof String || data instanceof Object[]) {
            if (secretKey == null) {
                secretKey = secretKey;
            }

            if (secretKey == null) {
                return null;
            }

            String dataStr = data instanceof Object[] ? new ObjectMapper().writeValueAsString(data) : (String) data;
            SecretKeySpec key = new SecretKeySpec(secretKey.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encrypted = cipher.doFinal(dataStr.getBytes("UTF-8"));
            String base64Encoded = Base64.getEncoder().encodeToString(encrypted);
            String urlEncoded = java.net.URLEncoder.encode(base64Encoded, "UTF-8");
            return urlEncoded;
        } else {
            return null;
        }
    }









}
