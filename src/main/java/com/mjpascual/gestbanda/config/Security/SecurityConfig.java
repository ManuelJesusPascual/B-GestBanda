package com.mjpascual.gestbanda.config.Security;

import com.mjpascual.gestbanda.services.UsersService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailService;
    private final JWTAuthorizationFilter jwtAuthorizationFilter;
    private final UsersService usersService;

    /**
     * When an HTTP request arrives, the first thing it must pass through is the various filters defined in our
     * security configuration. This method defines those filters.
     * @see JWTAuthenticationFilter Authentication filter
     * @see JWTAuthorizationFilter Authorization filter
     * @param httpSecurity An object passed to all the filters, which work with it and modify it.
     * @throws Exception Exception thrown by the .build() method if there is a problem building the filter chain.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, AuthenticationManager authManager, JWTAuthorizationFilter jWTAuthorizationFilter) throws Exception {

        JWTAuthenticationFilter jwtAuthenticationFilter = new JWTAuthenticationFilter(usersService);
        jwtAuthenticationFilter.setAuthenticationManager(authManager);
        jwtAuthenticationFilter.setFilterProcessesUrl("/api/users/login");

        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(http -> {
                    http.requestMatchers(HttpMethod.POST, "/api/users/registerUser").permitAll();
                    http.requestMatchers(HttpMethod.POST, "/login").permitAll();
                    http.anyRequest().authenticated();
                })
                .addFilter(jwtAuthenticationFilter)
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * This method defines an AuthenticationManager, which is the component responsible for managing authentication
     * in the application. It is obtained from the provided authentication configuration.
     *
     * @param authenticationConfiguration Configuration used to obtain the AuthenticationManager.
     * @return AuthenticationManager The configured authentication manager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Defines an AuthenticationProvider, responsible for performing user-specific authentication.
     * In this case, a DaoAuthenticationProvider is used, which requires a password encoder and a service
     * to load user details.
     *
     * @return AuthenticationProvider The configured authentication provider.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailService);
        return provider;
    }

    /**
     * This method defines a password encoder (PasswordEncoder) using BCrypt, a secure hashing algorithm for
     * storing passwords.
     *
     * @return PasswordEncoder The configured password encoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
