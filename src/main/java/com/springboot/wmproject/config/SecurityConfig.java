package com.springboot.wmproject.config;
import com.springboot.wmproject.security.AuthenticationProvider.CustomAuthenticationProvider;
import com.springboot.wmproject.security.JWT.JwtAuthenticationEntryPoint;
import com.springboot.wmproject.security.JWT.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {


    @Configuration
    @Order(1)
    public static class RestApiSecurityConfig {


        private CustomAuthenticationProvider customAuthenticationProvider;
        private JwtAuthenticationEntryPoint authenticationEntryPoint;

        private JwtAuthenticationFilter authenticationFilter;

        @Autowired
        public RestApiSecurityConfig(CustomAuthenticationProvider customAuthenticationProvider, JwtAuthenticationEntryPoint authenticationEntryPoint, JwtAuthenticationFilter authenticationFilter) {
            this.customAuthenticationProvider = customAuthenticationProvider;
            this.authenticationEntryPoint = authenticationEntryPoint;
            this.authenticationFilter = authenticationFilter;
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
            return configuration.getAuthenticationManager();
        }

        @Bean
        public AuthenticationManager authManager(HttpSecurity http) throws Exception {
            AuthenticationManagerBuilder authenticationManagerBuilder =
                    http.getSharedObject(AuthenticationManagerBuilder.class);
            authenticationManagerBuilder.authenticationProvider(customAuthenticationProvider);
            return authenticationManagerBuilder.build();
        }

        @Bean
        SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

            http.csrf().disable()
                    .authorizeHttpRequests((authorize) ->
                            authorize
                                    .requestMatchers(HttpMethod.GET, "/api/**").permitAll()
                                    .requestMatchers(AUTH_WHITELIST).permitAll()
                                    .anyRequest().authenticated()
                    ).exceptionHandling(
                            exception -> exception.authenticationEntryPoint(authenticationEntryPoint))
                    .sessionManagement(
                            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
            http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
            return http.build();
        }


        private static final String[] AUTH_WHITELIST = {
                // -- Swagger UI v3 (OpenAPI)
                "/v3/api-docs/**",
                "/swagger-ui/**",
                "/swagger-resources/**",
                "/swagger-ui.html",
                "/webjars/**",
                // auth api
                "/api/auth/**",
                //js file
                "/js/**",
                "/**",
        };

    }

    @Configuration
    @Order(2)
    public static class WebClientSecurityConfig {


        @Bean
        SecurityFilterChain securityFilterChain1(HttpSecurity http) throws Exception {

            http.csrf().disable()
                    .authorizeRequests()
                    .requestMatchers("/api/**").denyAll()
                    .requestMatchers( "/customer/login").permitAll()
                    .requestMatchers( "/staff/login").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .formLogin()
                    .loginPage("/login")
                    .permitAll()
                    .and()
                    .logout()
                    .logoutUrl("/logout")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                    .logoutSuccessUrl("/login");

            http.sessionManagement().maximumSessions(1).expiredUrl("/login?expired=true");

            return http.build();
        }



    }


}
