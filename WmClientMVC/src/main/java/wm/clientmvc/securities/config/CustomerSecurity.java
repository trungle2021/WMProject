package wm.clientmvc.securities.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import wm.clientmvc.utils.SD_CLIENT;

@Configuration
@EnableWebSecurity
@Order(1)
public class CustomerSecurity {
    @Bean
    public SecurityFilterChain customerFilterChain(HttpSecurity http) throws Exception{
        http.
                authorizeHttpRequests()
                .requestMatchers("/customer/login").permitAll()
                .requestMatchers("/customer/forgot_password").permitAll()
                .requestMatchers("/customer/changePassword").permitAll()
                .requestMatchers("/customer/logout").permitAll()
                .requestMatchers("/customer/**").hasRole("CUSTOMER")
                .requestMatchers("/access-denied").permitAll()
                .requestMatchers(SD_CLIENT.AUTH_WHITELIST).permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .accessDeniedPage("/access-denied")
                .and()
                .csrf().disable();
        return http.build();
    }
}
