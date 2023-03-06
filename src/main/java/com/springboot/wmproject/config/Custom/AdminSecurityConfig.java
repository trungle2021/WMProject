//package com.springboot.wmproject.config.Custom;
//
//import com.springboot.wmproject.security.AuthenticationProvider.CustomAuthenticationProvider;
//import com.springboot.wmproject.security.JWT.JwtAuthenticationEntryPoint;
//import com.springboot.wmproject.security.JWT.JwtAuthenticationFilter;
//import com.springboot.wmproject.security.UserDetailsService.EmployeeDetailsService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.Order;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
////@EnableWebSecurity
////@EnableGlobalMethodSecurity(prePostEnabled = true)
//@Order(1)
//public class AdminSecurityConfig {
//
//    private JwtAuthenticationEntryPoint authenticationEntryPoint;
//
//    private JwtAuthenticationFilter authenticationFilter;
//
//    @Autowired
//    public AdminSecurityConfig(JwtAuthenticationEntryPoint authenticationEntryPoint, JwtAuthenticationFilter authenticationFilter) {
//        this.authenticationEntryPoint = authenticationEntryPoint;
//        this.authenticationFilter = authenticationFilter;
//    }
//
//    @Bean
//    public UserDetailsService userDetailsService1(){
//        return new EmployeeDetailsService();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder1(){
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public DaoAuthenticationProvider authenticationProvider1(){
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        authProvider.setUserDetailsService(userDetailsService1());
//        authProvider.setPasswordEncoder(passwordEncoder1());
//        return authProvider;
//    }
//
//
//    @Bean
//    SecurityFilterChain securityFilterChain1(HttpSecurity http) throws Exception {
//        http.csrf().disable()
//                .authorizeHttpRequests((authorize) ->
//                        authorize
//                                .requestMatchers(HttpMethod.GET,"/api/**").permitAll()
//                                .requestMatchers("/customer/**").permitAll()
////                                .requestMatchers(AUTH_WHITELIST).permitAll()
//                                .anyRequest().authenticated()
//                ).exceptionHandling(
//                        exception -> exception.authenticationEntryPoint(authenticationEntryPoint))
//                .sessionManagement(
//                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .logout(logout->logout
//                        .logoutUrl("/staff/logout")
//                        .logoutSuccessUrl("/staff/login")
//                        .invalidateHttpSession(true)
//                        .deleteCookies("token")
//                );
//
//        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
//        return http.build();
//    }
//
//
//
//    private static final String[] AUTH_WHITELIST = {
//            // -- Swagger UI v3 (OpenAPI)
//            "/v3/api-docs/**",
//            "/swagger-ui/**",
//            "/swagger-resources/**",
//            "/swagger-ui.html",
//            "/webjars/**",
//            // auth api
//            "/api/auth/**",
//            //js file
//            "/js/**",
//            "/**"
//    };
//
//}
