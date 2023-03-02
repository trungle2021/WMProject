package com.springboot.wmproject.security.JWT;

//import com.springboot.wmproject.security.AuthenticationToken.CustomerUsernamePasswordAuthenticationToken;
import com.springboot.wmproject.security.AuthenticationToken.CustomerUsernamePasswordAuthenticationToken;
import com.springboot.wmproject.security.AuthenticationToken.EmployeeUsernamePasswordAuthenticationToken;
//import com.springboot.wmproject.security.UserDetailsService.CustomerDetailsService;
import com.springboot.wmproject.security.UserDetailsService.CustomerDetailsService;
import com.springboot.wmproject.security.UserDetailsService.EmployeeDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private JwtTokenProvider jwtTokenProvider;

    private CustomerDetailsService customerDetailsService;
    private EmployeeDetailsService employeeDetailsService;

    @Autowired
    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, CustomerDetailsService customerDetailsService, EmployeeDetailsService employeeDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.customerDetailsService = customerDetailsService;
        this.employeeDetailsService = employeeDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //get JWT token from Http Request

        String token = getTokenFromRequest(request);


        if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
            //get Username from token
            String username = jwtTokenProvider.getUsername(token);
            String userType = jwtTokenProvider.getUserType(token);
            if(userType.equals("ROLE_EMPLOYEE") || userType.equals("ROLE_ADMIN")){
                UserDetails userDetails = employeeDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authenticationToken = new EmployeeUsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            }else if(userType.equals("ROLE_CUSTOMER")){
                UserDetails userDetails = customerDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authenticationToken = new CustomerUsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

        }
        filterChain.doFilter(request,response);

    }

    private String getTokenFromRequest(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7,bearerToken.length());
        }
        return null;
    }
}
