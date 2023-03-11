package wm.clientmvc.securities.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import wm.clientmvc.utils.SD_CLIENT;

@Configuration
@EnableWebSecurity
@Order(2)
public  class StaffSecurity {


    @Bean
    public SecurityFilterChain staffFilterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests()
                .requestMatchers("/staff/login").permitAll()
                .requestMatchers("/staff/logout").permitAll()
//                .requestMatchers("/staff/admin/**").hasAuthority("ROLE_ADMIN")
//                .requestMatchers("/staff/organize/**").hasAuthority("ROLE_ORGANIZE")
//                .requestMatchers("/staff/sale/**").hasAuthority("ROLE_SALE")
                .requestMatchers("/staff/**").permitAll()
                .requestMatchers("/access-denied").permitAll()
                .requestMatchers(SD_CLIENT.AUTH_WHITELIST).permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .accessDeniedPage("/access-denied")
                .and()
                .cors().and().csrf().disable();
        return http.build();
    }


}
