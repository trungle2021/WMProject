package wm.clientmvc.securities.config;

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
        registry.addInterceptor(new AuthenticationInterceptor()).addPathPatterns(
                        "/customer/**"
                        , "/staff/**"
                )
                .excludePathPatterns(excludePathList);
    }
    public static final String[] excludePathList = {
            //CUSTOMER
            "/customer/login",
            "/customer/register",
            "/customer/forgot_password",
            "/customer/changePassword",
            "/customer/logout",
            "/access-denied",
            //STAFF
            "/staff/login",
            "/staff/logout",
    };
}
