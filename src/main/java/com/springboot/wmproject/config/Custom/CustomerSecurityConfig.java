//package com.springboot.wmproject.config.Custom;
//
//import com.springboot.wmproject.security.JWT.JwtAuthenticationEntryPoint;
//import com.springboot.wmproject.security.JWT.JwtAuthenticationFilter;
//import com.springboot.wmproject.security.UserDetailsService.CustomerDetailsService;
//import com.springboot.wmproject.security.UserDetailsService.EmployeeDetailsService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.Order;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
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
//@Order(2)
//public class CustomerSecurityConfig {
//
//    private JwtAuthenticationEntryPoint authenticationEntryPoint;
//
//    private JwtAuthenticationFilter authenticationFilter;
//
//    @Autowired
//    public CustomerSecurityConfig(JwtAuthenticationEntryPoint authenticationEntryPoint, JwtAuthenticationFilter authenticationFilter) {
//        this.authenticationEntryPoint = authenticationEntryPoint;
//        this.authenticationFilter = authenticationFilter;
//    }
//
//    @Bean
//    public UserDetailsService userDetailsService2(){
//        return new CustomerDetailsService();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder2(){
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public DaoAuthenticationProvider authenticationProvider2(){
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        authProvider.setUserDetailsService(userDetailsService2());
//        authProvider.setPasswordEncoder(passwordEncoder2());
//        return authProvider;
//    }
//
//
//    @Bean
//    SecurityFilterChain securityFilterChain2(HttpSecurity http) throws Exception {
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
//                        .logoutUrl("/customer/logout")
//                        .logoutSuccessUrl("/customer/login")
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
