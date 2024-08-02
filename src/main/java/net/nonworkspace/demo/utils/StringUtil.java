package net.nonworkspace.demo.utils;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    public static String getRandomStringValue(int length) {
        Random random = new Random();
        StringBuilder builer = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (random.nextBoolean()) {
                builer.append((char) ((int) random.nextInt(26) + 97));
            } else {
                builer.append(random.nextInt(10));
            }
        }
        return builer.toString();
    }

    public static boolean isEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
