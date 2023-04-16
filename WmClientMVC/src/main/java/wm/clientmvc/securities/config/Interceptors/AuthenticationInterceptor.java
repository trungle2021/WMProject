package wm.clientmvc.securities.config.Interceptors;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthenticationInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String role = authentication.getAuthorities().stream().findFirst().toString();

        boolean isAnonymous = role.contains("ANONYMOUS");
        boolean isSale = role.contains("SALE");
        boolean isOrganize = role.contains("ORGANIZE");
        boolean isAdmin = role.contains("ADMIN");
        String path = request.getServletPath();

        if (isAnonymous) {
            if (path.startsWith("/customers")) {
                response.sendRedirect("/login");
                return false;
            }

            if (path.startsWith("/staff")) {
                response.sendRedirect("/staff/login");
                return false;
            }
        }

        return true;
    }

}



