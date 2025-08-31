package com.uade.tpo.demo.controllers.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

        private final JwtAuthenticationFilter jwtAuthFilter;
        private final AuthenticationProvider authenticationProvider;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(AbstractHttpConfigurer::disable)
                                .authorizeHttpRequests(req -> req
                                .requestMatchers("/api/v1/auth/**").permitAll()
                                
                                .requestMatchers("/cart/**").hasRole("USER")
                                //CATEGORIAS
                                
                                .requestMatchers(HttpMethod.POST, "/categories").hasRole("ADMIN") //solo ADMIN puede crear categorías


                                //PRODUCTOS
                                .requestMatchers(HttpMethod.POST, "/product").hasRole("ADMIN")  //solo ADMIN puede crear productos
                                .requestMatchers(HttpMethod.GET, "/product").permitAll() //permite visualizar productos sin autenticación
                                .requestMatchers(HttpMethod.GET, "/product/{id}").permitAll() //permite visualizar detalles de un producto sin autenticación
                                .requestMatchers(HttpMethod.PUT, "/product/{id}").hasRole("ADMIN") //solo ADMIN puede modificar detalles de productos
                                .requestMatchers(HttpMethod.DELETE, "/product/{id}").hasRole("ADMIN") //solo ADMIN puede eliminar productos
                                                .anyRequest()
                                                .authenticated())
                                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                                .authenticationProvider(authenticationProvider)
                                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }
}
