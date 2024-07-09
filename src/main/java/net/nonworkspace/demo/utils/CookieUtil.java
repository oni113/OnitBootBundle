package net.nonworkspace.demo.utils;

import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;
import org.springframework.util.SerializationUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtil {

    public static Optional<Cookie> getCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length <= 0) {
            return Optional.empty();
        }

        return Arrays.stream(cookies).filter(c -> c.getName().equals(cookieName)).findAny();
    }

    public static void addCookie(HttpServletResponse response, String name, String value,
            int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response,
            String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length <= 0) {
            return;
        }
        Arrays.stream(cookies).forEach(c -> {
            if (c.getName().equals(cookieName)) {
                c.setValue("");
                c.setPath("/");
                c.setMaxAge(0);
                response.addCookie(c);
            }
        });
    }

    public static String serialize(Object object) {
        return Base64.getUrlEncoder().encodeToString(SerializationUtils.serialize(object));
    }
}
