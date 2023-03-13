//package wm.clientmvc.securities.config.Interceptors;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
//import org.springframework.ui.Model;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//public class AuthenticationInterceptor implements HandlerInterceptor {
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String role = authentication.getAuthorities().stream().findFirst().toString();
//
//        boolean userIsStaff = role.contains("ADMIN") || role.contains("SALE") || role.contains("ORGANIZE");
//        boolean userIsCustomer = role.contains("CUSTOMER");
//        boolean isAnonymous = role.contains("ANONYMOUS");
//        String currentPath = request.getServletPath();
//
//
//
//        if (currentPath.startsWith("/staff")) {
//            if (userIsCustomer) {
//                if(currentPath.startsWith("/staff/login")){
//                    removeAuthentication(request,response);
//                    response.sendRedirect("/staff/login");
//                }
//                return true;
//            } else if (isAnonymous) {
//                response.sendRedirect("/staff/login");
//            } else {
//                if(currentPath.startsWith("/staff/login")){
//                    response.sendRedirect("/staff/dashboard");
//                }
//            }
//        } else if (currentPath.startsWith("/customers")) {
//            if (userIsStaff) {
//                if(currentPath.startsWith("/customers/login")){
//                    removeAuthentication(request,response);
//                    response.sendRedirect("/customers/login");
//                }
//                return true;
//            } else
//                if (isAnonymous) {
//                response.sendRedirect("/staff/login");
//            } else {
//                if(currentPath.startsWith("/customers/login")){
//                    response.sendRedirect("/customers/home");
//                }
//            }
//        }
////        else if (currentPath.startsWith("customer/login")) {
////            if (userIsCustomer) {
////
////                response.sendRedirect("/customers/home");
////            } else if(userIsStaff){
////                response.sendRedirect("/customers/login");
////            }else{
////                response.sendRedirect("/customers/login");
////            }
////        }
//        else {
//            response.sendRedirect("/error");
//        }
//        return true;
//    }
//
//
//    public void removeAuthentication( HttpServletRequest request, HttpServletResponse response) throws ServletException {
//        Cookie cookie = new Cookie("token", null);
//        cookie.setMaxAge(0);
//        cookie.setPath("/");
//        response.addCookie(cookie);
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if (auth != null) {
//            new SecurityContextLogoutHandler().logout(request, response, auth);
//            SecurityContextHolder.clearContext();
//        }
//    }
//}
//
