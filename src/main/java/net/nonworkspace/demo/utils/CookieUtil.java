package net.nonworkspace.demo.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Base64;
import org.springframework.http.ResponseCookie;
import org.springframework.util.SerializationUtils;

public class CookieUtil {

    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            return "";
        }

        return
            Arrays.stream(cookies).filter(c -> c.getName().equals(cookieName)).findAny().get()
                .getValue();
    }

    public static void addCookie(HttpServletResponse response, String name, String value,
        int maxAge) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
            .path("/")
            .sameSite("None")
            .httpOnly(false)
            .secure(true)
            .maxAge(maxAge)
            .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response,
        String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
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
