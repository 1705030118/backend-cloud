package com.ldm.user;


import org.apache.commons.codec.digest.DigestUtils;

/**
 * MD5工具类
 *
 * @author noodle
 */
public class MD5Util {
    /**
     * 获取输入字符串的MD5
     *
     * @param src
     * @return
     */
    public static String md5(String src) {
        return DigestUtils.md5Hex(src);
    }
    /**
     * 对表单中的密码md5加盐，加盐后的md5为存储在数据库中的密码md5
     *
     * @param formPassword 表单中填充的明文密码
     * @param saltDb       这里的salt是在数据库查出来的，而并非第一次加盐的盐值
     * @return
     */
    public static String formPassToDbPass(String formPassword, String saltDb) {
        String str = "" + saltDb.charAt(0) + saltDb.charAt(2) + formPassword + saltDb.charAt(5) + saltDb.charAt(4);
        return md5(str);
    }

}