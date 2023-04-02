package wm.clientmvc.securities.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.security.web.session.SessionManagementFilter;
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
                .requestMatchers("/staff/logout").hasAnyRole("ADMIN","ORGANIZE","SALE")
                .requestMatchers("/refreshToken/**").hasAnyRole("ADMIN","ORGANIZE","SALE","CUSTOMER")
                .requestMatchers("/login","/forgot_password","/changePassword","/register").permitAll()
                //ORDER

                .requestMatchers("/staff/orders/order-completed").hasAnyRole("ADMIN","ORGANIZE")
                .requestMatchers("/staff/orders/**").hasAnyRole("ADMIN","SALE","ANONYMOUS")
                .requestMatchers("/staff/orders/showall").hasAnyRole("ADMIN","SALE","ANONYMOUS")
                .requestMatchers("/staff/orders/showmyorder/**").hasAnyRole("ADMIN","SALE","ANONYMOUS")
                .requestMatchers("/staff/orders/delete/**").hasAnyRole("ADMIN")


                //EMPLOYEE
                .requestMatchers("/staff/employees/create").hasAnyRole("ADMIN","ANONYMOUS")
                .requestMatchers("/staff/employees/getOne/**").hasAnyRole("ADMIN","ORGANIZE","SALE","ANONYMOUS")
                .requestMatchers("/staff/employees/getAll").hasAnyRole("ADMIN","ANONYMOUS")
                .requestMatchers("/staff/employees/update/**").hasAnyRole("ADMIN","ORGANIZE","SALE","ANONYMOUS")
                .requestMatchers("/staff/employees/delete/**").hasAnyRole("ADMIN","ANONYMOUS")
                //TEAM

                .requestMatchers("/staff/teams/create").hasAnyRole("ADMIN","ANONYMOUS")
                .requestMatchers("/staff/teams/getOne/**").hasAnyRole("ADMIN","ORGANIZE","SALE","ANONYMOUS")
                .requestMatchers("/staff/teams/getAll").hasAnyRole("ADMIN","ANONYMOUS")
                .requestMatchers("/staff/teams/getAllEmployeeByTeamId/**").hasAnyRole("ADMIN","ORGANIZE","SALE","ANONYMOUS")
                .requestMatchers("/staff/teams/update/**").hasAnyRole("ADMIN","ANONYMOUS")
                .requestMatchers("/staff/teams/delete/**").hasAnyRole("ADMIN","ANONYMOUS")
                //EMPLOYEE ACCOUNT
                .requestMatchers("/staff/employeeAccounts/create").hasAnyRole("ADMIN","ANONYMOUS")
                .requestMatchers("/staff/employeeAccounts/getOne/**").hasAnyRole("ADMIN","ORGANIZE","SALE","ANONYMOUS")
                .requestMatchers("/staff/employeeAccounts/getAll").hasAnyRole("ADMIN","ANONYMOUS")
                .requestMatchers("/staff/employeeAccounts/update/**").hasAnyRole("ADMIN","ORGANIZE","SALE","ANONYMOUS")
                .requestMatchers("/staff/employeeAccounts/delete/**").hasAnyRole("ADMIN","ANONYMOUS")
                //CUSTOMER
                .requestMatchers("/staff/customers/**").hasAnyRole("ADMIN","SALE","ANONYMOUS")
                .requestMatchers("/staff/customers/delete/**").hasAnyRole("ADMIN","ANONYMOUS")
                //CUSTOMER ACCOUNT
                .requestMatchers("/staff/customerAccounts/**").hasAnyRole("ADMIN","SALE","ANONYMOUS")
                .requestMatchers("/staff/customerAccounts/delete/**").hasAnyRole("ADMIN","ANONYMOUS")
                //SERVICE
                .requestMatchers("/staff/services/create").hasAnyRole("ADMIN","ANONYMOUS")
                .requestMatchers("/staff/services/getOne/**").hasAnyRole("ADMIN","ORGANIZE","SALE","ANONYMOUS")
                .requestMatchers("/staff/services/getAll").hasAnyRole("ADMIN","ORGANIZE","SALE","ANONYMOUS")
                .requestMatchers("/staff/services/update/**").hasAnyRole("ADMIN","ANONYMOUS")
                .requestMatchers("/staff/services/delete/**").hasAnyRole("ADMIN","ANONYMOUS")
                //FOOD
                .requestMatchers("/staff/food/create").hasAnyRole("ADMIN","ANONYMOUS")
                .requestMatchers("/staff/food/getOne/**").hasAnyRole("ADMIN","ORGANIZE","SALE","ANONYMOUS")
                .requestMatchers("/staff/food/getAll").hasAnyRole("ADMIN","ORGANIZE","SALE","ANONYMOUS")
                .requestMatchers("/staff/food/update/**").hasAnyRole("ADMIN","ANONYMOUS")
                .requestMatchers("/staff/food/delete/**").hasAnyRole("ADMIN","ANONYMOUS")
                //FOOD DETAILS
                .requestMatchers("/staff/foodDetails/create").hasAnyRole("ADMIN","SALE","ANONYMOUS")
                .requestMatchers("/staff/foodDetails/getOne/**").hasAnyRole("ADMIN","ORGANIZE","SALE","ANONYMOUS")
                .requestMatchers("/staff/foodDetails/getAll").hasAnyRole("ADMIN","ORGANIZE","SALE","ANONYMOUS")
                .requestMatchers("/staff/foodDetails/update/**").hasAnyRole("ADMIN","SALE","ANONYMOUS")
                .requestMatchers("/staff/foodDetails/delete/**").hasAnyRole("ADMIN","ANONYMOUS")
                //VENUE
                .requestMatchers("/staff/venues/create").hasAnyRole("ADMIN","ANONYMOUS")
                .requestMatchers("/staff/venues/getOne/**").hasAnyRole("ADMIN","ORGANIZE","SALE","ANONYMOUS")
                .requestMatchers("/staff/venues/getAll").hasAnyRole("ADMIN","ORGANIZE","SALE","ANONYMOUS")
                .requestMatchers("/staff/venues/update/**").hasAnyRole("ADMIN","ANONYMOUS")
                .requestMatchers("/staff/venues/delete/**").hasAnyRole("ADMIN","ANONYMOUS")
                //MATERIAL
                .requestMatchers("/staff/materials/**").hasAnyRole("ADMIN","ORGANIZE","ANONYMOUS")
                //REVENUE
                .requestMatchers("/staff/revenues/**").hasAnyRole("ADMIN","SALE","ANONYMOUS")

                //PERSONAL PERMISSION
                .requestMatchers("/staff/organize/**").hasAnyRole("ORGANIZE","ANONYMOUS")
                .requestMatchers("/staff/sale/**").hasAnyRole("SALE","ANONYMOUS")
                .requestMatchers("/staff/admin/**").hasAnyRole("ADMIN","ANONYMOUS")
                .requestMatchers("/staff/**").hasAnyRole("ADMIN","SALE","ORGANIZE","ANONYMOUS")
                //CUSTOMER SECURITY CONFIGURATION

                .requestMatchers("/customers/logout").hasRole("CUSTOMER")
                //ORDER
                .requestMatchers("/customers/orders/**").hasAnyRole("CUSTOMER","ANONYMOUS")
                //CUSTOMER - PROFILE
                .requestMatchers("/customers/profile/**").hasAnyRole("CUSTOMER","ANONYMOUS")
                .requestMatchers("/customers/**").hasAnyRole("CUSTOMER","ANONYMOUS")
                .requestMatchers("/access-denied").permitAll()
                //ANONYMOUS
                .requestMatchers(SD_CLIENT.AUTH_WHITELIST).permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .accessDeniedPage("/access-denied")
                .and()
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                .cors().and().csrf().disable();

        return http.build();
    }
}

