package mk.ukim.finki.wp.sep2022.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

/**
 *  This class is used to configure user login on path '/login' and logout on path '/logout'.
 *  The only public page in the application should be '/'.
 *  All other pages should be visible only for a user with role 'ROLE_ADMIN'.
 *  Furthermore, in the "list.html" template, the 'Edit', 'Delete', 'Add' buttons should only be
 *  visible for a user with role 'ROLE_ADMIN'.
 *  The 'Follow Match' button should only be visible for a user with role 'ROLE_USER'.
 *
 *  For login inMemory users should be used. Their credentials are given below:
 *  [{
 *      username: "user",
 *      password: "user",
 *      role: "ROLE_USER"
 *  },
 *
 *  {
 *      username: "admin",
 *      password: "admin",
 *      role: "ROLE_ADMIN"
 *  }]
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception  {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeRequests( (requests) -> requests
                        .antMatchers("/","/matches")
                        .permitAll()
                        .antMatchers("/matches/*/follow").hasRole("USER")
                        .antMatchers("/matches/**").hasRole("ADMIN")
                        .anyRequest()
                        .authenticated()

                )
                .formLogin((form) -> form
                        .permitAll()
                        .failureUrl("/login?error=BadCredentials")
                        .defaultSuccessUrl("/matches", true)
                )
                .logout((logout) -> logout
                        .logoutUrl("/logout")
                        .clearAuthentication(true)
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessUrl("/")
                );

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user1 = User.builder()
                .username("user")
                .password(passwordEncoder.encode("user"))
                .roles("USER")
                .build();
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user1, admin);
    }




}
