//package wm.clientmvc.securities.config.Interceptors;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//import wm.clientmvc.securities.config.Interceptors.AuthenticationInterceptor;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Configuration
//public class AppConfig implements WebMvcConfigurer {
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new AuthenticationInterceptor()).addPathPatterns(
//                        "/customers/**",
//                        "/staff/**"
////                                "/login"
//                )
//                .excludePathPatterns(excludePathList);
//    }
//    public static final String[] excludePathList = {
//            //CUSTOMER
//            "/customers/login",
//            "/customers/register",
//            "/customers/forgot_password",
//            "/customers/changePassword",
//            "/customers/logout",
//            "/access-denied",
//            //STAFF
//            "/staff/login",
//            "/staff/logout",
//            //ANONYMOUS
//
//    };
//}
