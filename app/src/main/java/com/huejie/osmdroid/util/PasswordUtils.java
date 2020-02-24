package com.huejie.osmdroid.util;

import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;
import java.util.Random;
import java.util.UUID;


/**
 * 密码加密工具类
 *
 * @author fangyi
 */
public class PasswordUtils {

    /**
     * 加盐MD5加密
     *
     * @param password 将要加密的密码
     * @param salt     随机盐
     * @return 加密后字符串
     * @author fangyi
     * @date 2019/1/10
     */
    public static String getSaltMD5(String password, String salt) {
        password = md5Hex(password + salt);
        char[] cs = new char[48];
        for (int i = 0; i < 48; i += 3) {
            cs[i] = password.charAt(i / 3 * 2);
            char c = salt.charAt(i / 3);
            cs[i + 1] = c;
            cs[i + 2] = password.charAt(i / 3 * 2 + 1);
        }
        return String.valueOf(cs);
    }

    /**
     * 验证加盐后是否和原文一致
     *
     * @param password 未加密密码
     * @param md5str   加密密码
     * @return boolean  是否一致
     * @author fangyi
     * @date 2019/1/9
     */
    public static Boolean getSaltverifyMD5(String password, String md5str) {
        char[] cs1 = new char[32];
        char[] cs2 = new char[16];
        for (int i = 0; i < 48; i += 3) {
            cs1[i / 3 * 2] = md5str.charAt(i);
            cs1[i / 3 * 2 + 1] = md5str.charAt(i + 2);
            cs2[i / 3] = md5str.charAt(i + 1);
        }
        String salt = new String(cs2);
        return md5Hex(password + salt).equals(String.valueOf(cs1));
    }


    /**
     * 使用Apache的Hex类实现Hex(16进制字符串和)和字节数组的互转
     */
    @SuppressWarnings("unused")
    private static String md5Hex(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(str.getBytes());
            return new String(new Hex().encode(digest));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.toString());
            return "";
        }
    }

    /**
     * 生成随机盐
     */
    public static String randomSalt() {
        // 生成一个16位的随机数
        Random random = new Random();
        StringBuilder sBuilder = new StringBuilder(16);
        sBuilder.append(random.nextInt(99999999)).append(random.nextInt(99999999));
        int len = sBuilder.length();
        if (len < 16) {
            for (int i = 0; i < 16 - len; i++) {
                sBuilder.append("0");
            }
        }
        // 生成最终的加密盐
        return sBuilder.toString();
    }

    /**
     * 生成UUID
     */
    public static String generateUuid() {
        return UUID.randomUUID().toString();
    }

//    public static void main(String[] args) {
//        String strMD5 = new String("123456");
//        // 获取加盐后的MD5值
//        String ciphertext = PasswordUtils.getSaltMD5(strMD5,randomSalt());
//        System.out.println("加盐后MD5：" + ciphertext);
//        System.out.println("是否是同一字符串1:" + PasswordUtils.getSaltverifyMD5(strMD5, ciphertext));
//
//    }

}
