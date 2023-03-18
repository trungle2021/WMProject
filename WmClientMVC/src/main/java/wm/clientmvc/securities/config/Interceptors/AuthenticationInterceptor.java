package wm.clientmvc.securities.config.Interceptors;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.ui.Model;
import org.springframework.web.servlet.HandlerInterceptor;
import wm.clientmvc.securities.UserDetails.CustomUserDetails;
import wm.clientmvc.utils.APIHelper;
import wm.clientmvc.utils.SD_CLIENT;

import java.util.HashSet;
import java.util.Set;

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




        if(isAnonymous){
            if (path.startsWith("/customers")) {
                response.sendRedirect("/login");
                return false;
            }

            if(path.startsWith("/staff")){
                response.sendRedirect("/staff/login");
                return false;
            }
        }else{
            String token = APIHelper.getCookie(request,"token");
            if(token!=null){
                String roleCurrentDB = APIHelper.makeApiCall(SD_CLIENT.api_employee_findRoleByEmpID, HttpMethod.GET,token,null,String.class);
                if(roleCurrentDB != role){
                    CustomUserDetails principals = (CustomUserDetails) authentication.getPrincipal();

                    SecurityContext securityContext = SecurityContextHolder.getContext();
                    Set<GrantedAuthority> authorities = new HashSet<>();
                    authorities.add(new SimpleGrantedAuthority(roleCurrentDB));

                    CustomUserDetails customerUserDetails = new CustomUserDetails(principals.getUsername(), principals.getPassword(), principals.getUserId(), principals.getFullName(), principals.getAvatar(), authorities);
                    Authentication newAuthentication = new UsernamePasswordAuthenticationToken(customerUserDetails, principals.getPassword(), authorities);
                    securityContext.setAuthentication(newAuthentication);
                }
            }
        }
        return true;
    }


    public void removeAuthentication( HttpServletRequest request, HttpServletResponse response) throws ServletException {
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
            SecurityContextHolder.clearContext();
        }
    }
}

