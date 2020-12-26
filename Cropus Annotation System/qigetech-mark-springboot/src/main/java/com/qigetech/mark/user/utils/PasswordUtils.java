package com.qigetech.mark.user.utils;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Created by liuxuanjun on 11/12/2018
 * Project : teaching
 */
public class PasswordUtils {

    public static String getSlatPwMd5(String password){
        return new BCryptPasswordEncoder().encode(password);
    }
    public static boolean isValidMessageAudio(String msg) {
        int cnt = 0;
        for (int i=0; i<msg.length(); ++i) {
            switch (msg.charAt(i)) {
                case '0': case '1': case '2': case '3': case '4':
                case '5': case '6': case '7': case '8': case '9':
                case 'a': case 'b': case 'c': case 'd': case 'e': case 'f':
                case 'A': case 'B': case 'C': case 'D': case 'E': case 'F':
                    ++ cnt;
                    if (32 <= cnt) return true;
                    break;
                case '/':
                    if ((i + 10) < msg.length()) {// "/storage/"
                        char ch1 = msg.charAt(i+1);
                        char ch2 = msg.charAt(i+8);
                        if ('/' == ch2 && ('s' == ch1 || 'S' == ch1)) return true;
                    }
                default:
                    cnt = 0;
                    break;
            }
        }
        return false;
    }

}
