package com.fourback.redisstudy.global.common.util;


import org.mindrot.jbcrypt.BCrypt;

public class EncryptionUtil {

    public static String encryptPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean checkPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
}
