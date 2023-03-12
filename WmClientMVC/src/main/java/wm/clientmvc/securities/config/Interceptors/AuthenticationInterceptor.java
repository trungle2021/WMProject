package wm.clientmvc.securities.config.Interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthenticationInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String role = authentication.getAuthorities().stream().findFirst().toString();
        if ( authentication == null || (authentication.isAuthenticated() && role.contains("ANONYMOUS"))){
            if (request.getServletPath().contains("staff")) {
                response.sendRedirect("/staff/login");
            } else if (request.getServletPath().contains("customer")){
                response.sendRedirect("/customer/login");
            }else{
                response.sendRedirect("/error");
            }
            return false;
        }
        return true;
    }
}

