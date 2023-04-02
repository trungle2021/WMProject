package com.springboot.wmproject.securities.JWT;

//import com.springboot.wmproject.security.AuthenticationToken.CustomerUsernamePasswordAuthenticationToken;
import com.springboot.wmproject.securities.AuthenticationToken.CustomerUsernamePasswordAuthenticationToken;
import com.springboot.wmproject.securities.AuthenticationToken.EmployeeUsernamePasswordAuthenticationToken;
//import com.springboot.wmproject.security.UserDetailsService.CustomerDetailsService;
import com.springboot.wmproject.securities.UserDetailsService.CustomerDetailsService;
import com.springboot.wmproject.securities.UserDetailsService.EmployeeDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
                String userID = jwtTokenProvider.getUserID(token);
                String is_verified = jwtTokenProvider.getIsVerified(token);
                switch (userType){
                    case "ROLE_ADMIN":
                    case "ROLE_SALE":
                    case "ROLE_ORGANIZE":
                        UserDetails userDetails = employeeDetailsService.loadUserByUsername(username);
                        UsernamePasswordAuthenticationToken authenticationToken = new EmployeeUsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                        break;
                    case "ROLE_CUSTOMER":
                        UserDetails customerUserDetails = customerDetailsService.loadUserByUsername(username);
                        UsernamePasswordAuthenticationToken customer_authenticationToken = new CustomerUsernamePasswordAuthenticationToken(
                                customerUserDetails,
                                null,
                                customerUserDetails.getAuthorities()
                        );
                        customer_authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(customer_authenticationToken);
                        break;
                    default:
                        break;
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
