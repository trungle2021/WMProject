package wm.clientmvc.securities.config.Interceptors;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import wm.clientmvc.securities.config.Interceptors.AuthenticationInterceptor;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class AppConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthenticationInterceptor()).addPathPatterns(acceptPath).excludePathPatterns(excludePathList);
    }
    public static final String[] acceptPath = {
            //CUSTOMER
            "/customers/**",
            "/staff/**",
    };

    public static final String[] excludePathList = {
            //CUSTOMER
            "/login",
            "/register",
            "/changePassword",
            "/forgot_password",
            "/logout",
            "/access-denied",
            //STAFF
            "/staff/logout",
            "/staff/login",
            //ANONYMOUS

    };
}
