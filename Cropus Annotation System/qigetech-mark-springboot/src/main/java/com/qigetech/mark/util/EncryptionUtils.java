package com.qigetech.mark.util;

/**
 * Created by liuxuanjun on 2019-06-19
 * Project : qigetech-mark
 */
public class EncryptionUtils {

    private static final String key = "qigetechqigetech";

    private static final String iv = "0000000000000001";

    /**
     * @Author panzejia
     * @Description 加密
     * @Date 9:40 2019-06-19
     * @Param []
     * @return java.lang.String

    public static String toEncryption(String text){
        AES aes = new AES(Mode.CTR, Padding.PKCS5Padding, key.getBytes(), iv.getBytes());
        // 加密为16进制表示
        String encryptHex = aes.encryptHex(text);
        return encryptHex;
    }

    public static String toDecryptStr(String encryptHex){
        AES aes = new AES(Mode.CTR, Padding.PKCS5Padding, key.getBytes(), iv.getBytes());
        // 解密为字符串
        String decryptStr = aes.decryptStr(encryptHex, CharsetUtil.CHARSET_UTF_8);
        return decryptStr;
    }
     **/
}
