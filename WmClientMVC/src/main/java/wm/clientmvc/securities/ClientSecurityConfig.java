package wm.clientmvc.securities;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class ClientSecurityConfig {

    @Bean
    public SecurityFilterChain config(HttpSecurity http) throws Exception {
        http.httpBasic().disable();
//        http.authorizeHttpRequests()
//                .requestMatchers("/resources/**").permitAll()
//                .requestMatchers("/").permitAll()
//                .requestMatchers("/staff/**").hasAnyRole("ADMIN","EMPLOYEE")
//                .requestMatchers(AUTH_WHITELIST).permitAll()
//                .anyRequest().authenticated();


        return http.build();
    }


    private static final String[] AUTH_WHITELIST = {

            //FrontendFile
            "/js/**",
            "/css/**",
            "/images/**",
            "/fonts/**",
            "/scss/**",
            "/adminStatic/**",

            //routes
            "/staff/login",
            "/customer/login",
            "/**",
    };
}
