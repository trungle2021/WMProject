package wm.clientmvc.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import wm.clientmvc.DTO.LoginDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SD_CLIENT {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String DOMAIN_APP_API = "http://localhost:8080";
    public static final String DOMAIN_APP_CLIENT = "http://localhost:9999";
    public static final String[] AUTH_WHITELIST = {
            //js file
            "/resource/**",
            "/js/**",
            "/css/**",
            "/scss/**",
            "/images/**",
            "/templates/**",
            "/**"
    };
    public static List<String> STAFF_ROLES = new ArrayList<>() {
        {
            add("ROLE_ADMIN");
            add("ROLE_EMPLOYEE");
            add("ROLE_SALE");
            add("ROLE_ORGANIZE");
        }
    };

//

    public static void clearCookies(HttpServletResponse response, String... cookieNames) {
        String[] cookies = new String[]{};
        addCookies(cookies, cookieNames);
        Arrays.stream(cookies).forEach(cookieName -> {
            Cookie cookie = new Cookie(cookieName, null);
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        });
    }

    public static String[] addCookies(String[] cookies, String... newCookies) {
        String[] combinedCookies = new String[cookies.length + newCookies.length];
        System.arraycopy(cookies, 0, combinedCookies, 0, cookies.length);
        System.arraycopy(newCookies, 0, combinedCookies, cookies.length, newCookies.length);
        return combinedCookies;
    }
}
