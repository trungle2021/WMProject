package wm.clientmvc.securities;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class ClientSecurityConfig {

    @Bean
    public SecurityFilterChain config(HttpSecurity http) throws Exception {
        http.httpBasic().disable();
        http.csrf().disable()
                .authorizeHttpRequests()
                                        .requestMatchers(HttpMethod.GET, "/resource/**").permitAll()
                                        .requestMatchers(AUTH_WHITELIST).permitAll()
                                        .anyRequest().authenticated();

        http
            .logout().
                logoutUrl("/staff/logout")
                .logoutSuccessUrl("/staff/login")
                .invalidateHttpSession(true)
                .deleteCookies("token")
                .and()
            .logout().
                logoutUrl("/customer/logout")
                .logoutSuccessUrl("/customer/login")
                .invalidateHttpSession(true)
                .deleteCookies("token");


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
