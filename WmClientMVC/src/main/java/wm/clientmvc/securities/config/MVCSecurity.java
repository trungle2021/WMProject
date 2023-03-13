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
public class MVCSecurity {
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests()
//STAFF SECURITY CONFIGURATION
                .requestMatchers("/staff/login").permitAll()
                .requestMatchers("/staff/logout").permitAll()
                //ORDER
                .requestMatchers("/staff/orders/**").hasAnyRole("ADMIN","SALE")
                .requestMatchers("/staff/orders/showall").hasAnyRole("ADMIN","SALE")
                .requestMatchers("/staff/orders/showmyorder/**").hasAnyRole("ADMIN","SALE")
                .requestMatchers("/staff/orders/delete/**").hasAnyRole("ADMIN")
                //EMPLOYEE
                .requestMatchers("/staff/employees/create").hasAnyRole("ADMIN")
                .requestMatchers("/staff/employees/getOne/**").hasAnyRole("ADMIN","ORGANIZE","SALE")
                .requestMatchers("/staff/employees/getAll").hasAnyRole("ADMIN")
                .requestMatchers("/staff/employees/update/**").hasAnyRole("ADMIN","ORGANIZE","SALE")
                .requestMatchers("/staff/employees/delete/**").hasAnyRole("ADMIN")
                //EMPLOYEE ACCOUNT
                .requestMatchers("/staff/employeeAccounts/create").hasAnyRole("ADMIN")
                .requestMatchers("/staff/employeeAccounts/getOne/**").hasAnyRole("ADMIN","ORGANIZE","SALE")
                .requestMatchers("/staff/employeeAccounts/getAll").hasAnyRole("ADMIN")
                .requestMatchers("/staff/employeeAccounts/update/**").hasAnyRole("ADMIN","ORGANIZE","SALE")
                .requestMatchers("/staff/employeeAccounts/delete/**").hasAnyRole("ADMIN")
                //CUSTOMER
                .requestMatchers("/staff/customers/**").hasAnyRole("ADMIN","SALE")
                .requestMatchers("/staff/customers/delete/**").hasAnyRole("ADMIN")
                //CUSTOMER ACCOUNT
                .requestMatchers("/staff/customerAccounts/**").hasAnyRole("ADMIN","SALE")
                .requestMatchers("/staff/customerAccounts/delete/**").hasAnyRole("ADMIN")
                //SERVICE
                .requestMatchers("/staff/services/create").hasAnyRole("ADMIN")
                .requestMatchers("/staff/services/getOne/**").hasAnyRole("ADMIN","ORGANIZE","SALE")
                .requestMatchers("/staff/services/getAll").hasAnyRole("ADMIN","ORGANIZE","SALE")
                .requestMatchers("/staff/services/update/**").hasAnyRole("ADMIN")
                .requestMatchers("/staff/services/delete/**").hasAnyRole("ADMIN")
                //FOOD
                .requestMatchers("/staff/food/create").hasAnyRole("ADMIN")
                .requestMatchers("/staff/food/getOne/**").hasAnyRole("ADMIN","ORGANIZE","SALE")
                .requestMatchers("/staff/food/getAll").hasAnyRole("ADMIN","ORGANIZE","SALE")
                .requestMatchers("/staff/food/update/**").hasAnyRole("ADMIN")
                .requestMatchers("/staff/food/delete/**").hasAnyRole("ADMIN")
                //FOOD DETAILS
                .requestMatchers("/staff/foodDetails/create").hasAnyRole("ADMIN","SALE")
                .requestMatchers("/staff/foodDetails/getOne/**").hasAnyRole("ADMIN","ORGANIZE","SALE")
                .requestMatchers("/staff/foodDetails/getAll").hasAnyRole("ADMIN","ORGANIZE","SALE")
                .requestMatchers("/staff/foodDetails/update/**").hasAnyRole("ADMIN","SALE")
                .requestMatchers("/staff/foodDetails/delete/**").hasAnyRole("ADMIN")
                //VENUE
                .requestMatchers("/staff/venues/create").hasAnyRole("ADMIN")
                .requestMatchers("/staff/venues/getOne/**").hasAnyRole("ADMIN","ORGANIZE","SALE")
                .requestMatchers("/staff/venues/getAll").hasAnyRole("ADMIN","ORGANIZE","SALE")
                .requestMatchers("/staff/venues/update/**").hasAnyRole("ADMIN")
                .requestMatchers("/staff/venues/delete/**").hasAnyRole("ADMIN")
                //MATERIAL
                .requestMatchers("/staff/materials/**").hasRole("ADMIN")
                //TEAM
                .requestMatchers("/staff/teams/create").hasAnyRole("ADMIN")
                .requestMatchers("/staff/teams/getOne/**").hasAnyRole("ADMIN","ORGANIZE","SALE")
                .requestMatchers("/staff/teams/getAll").hasAnyRole("ADMIN","ORGANIZE","SALE")
                .requestMatchers("/staff/teams/update/**").hasAnyRole("ADMIN")
                .requestMatchers("/staff/teams/delete/**").hasAnyRole("ADMIN")
                //PERSONAL PERMISSION
                .requestMatchers("/staff/organize/**").hasRole("ORGANIZE")
                .requestMatchers("/staff/sale/**").hasRole("SALE")
                .requestMatchers("/staff/admin/**").hasRole("ADMIN")
                .requestMatchers("/staff/**").hasAnyRole("ADMIN","SALE","ORGANIZE")
//CUSTOMER SECURITY CONFIGURATION
                .requestMatchers("/customers/login").permitAll()
                .requestMatchers("/customers/forgot_password").permitAll()
                .requestMatchers("/customers/changePassword").permitAll()
                .requestMatchers("/customers/register").permitAll()
                .requestMatchers("/customers/logout").permitAll()
                //ORDER
                .requestMatchers("/customers/orders/**").hasAnyRole("CUSTOMER")
                //CUSTOMER - PROFILE
                .requestMatchers("/customers/profile/**").hasAnyRole("CUSTOMER")
                .requestMatchers("/customers/**").hasAnyRole("CUSTOMER")
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
