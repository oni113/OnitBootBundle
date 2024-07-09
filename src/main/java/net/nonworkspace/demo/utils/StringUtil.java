package net.nonworkspace.demo.utils;

import java.util.Random;

public class StringUtil {

    public static String getRamdomStringValue(int length) {
        Random random = new Random();
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < length; i++) {
            if (random.nextBoolean()) {
                buf.append((char) ((int) random.nextInt(26) + 97));
            } else {
                buf.append(random.nextInt(10));
            }
        }
        return buf.toString();
    }
}
