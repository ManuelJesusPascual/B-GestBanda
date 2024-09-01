package com.mjpascual.gestbanda.config.Security;

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


    /**
     * En cuanto llega una petición HTTP lo primero que debe pasar es por los distintos filtros definidos en nuestra
     * configuración de seguridad. En este método se definen esos filtros.
     * @see JWTAuthenticationFilter Filtro de autenticación
     * @see JWTAuthorizationFilter Filtro de autorización
     * @param httpSecurity Es un objeto que se le pasa a todos los filtros, y estos filtros trabajan con él y lo modifican.
     * @throws Exception Excepción lanzada por el método .build() si hay algún problema al construir la cadena de filtros.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, AuthenticationManager authManager, JWTAuthorizationFilter jWTAuthorizationFilter) throws Exception {

        JWTAuthenticationFilter jwtAuthenticationFilter = new JWTAuthenticationFilter();
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
     * Este método define un AuthenticationManager, que es el componente encargado de gestionar la autenticación
     * en la aplicación. Se obtiene a partir de la configuración de autenticación proporcionada.
     *
     * @param authenticationConfiguration Configuración utilizada para obtener el AuthenticationManager.
     * @return AuthenticationManager El administrador de autenticación configurado.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Define un AuthenticationProvider, que es el encargado de realizar la autenticación específica de usuarios.
     * En este caso, se utiliza un DaoAuthenticationProvider que requiere un codificador de contraseñas y un servicio
     * para cargar los detalles del usuario.
     *
     * @return AuthenticationProvider El proveedor de autenticación configurado.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailService);
        return provider;
    }


    /**
     * Este método define un codificador de contraseñas (PasswordEncoder) utilizando BCrypt, que es un algoritmo de
     * hash seguro para almacenar contraseñas.
     *
     * @return PasswordEncoder El codificador de contraseñas configurado.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
