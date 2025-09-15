package com.sumixkids.util;

import java.security.SecureRandom;

public class TwoFactorUtil {
    private static final SecureRandom random = new SecureRandom();

    public static String generateCode() {
        int code = 100000 + random.nextInt(900000); // 6 dígitos
        return String.valueOf(code);
    }
}
