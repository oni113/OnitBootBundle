package net.nonworkspace.demo.utils;

import java.util.regex.Pattern;

public class PasswordUtil {

    private final static String pattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>])[A-Za-z\\d!@#$%^&*(),.?\":{}|<>]{8,}$";

    public static boolean isPassword(String password) {
        return Pattern.matches(pattern, password);
    }
}
