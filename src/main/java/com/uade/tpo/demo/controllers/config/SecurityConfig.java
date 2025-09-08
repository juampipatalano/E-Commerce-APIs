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

import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties.Http;

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
                                .requestMatchers("/api/v1/auth/**").permitAll() //PERMITO A TODOS A REGISTRARSE Y LOGUEARSE

                                //CATEGORIAS
                                .requestMatchers(HttpMethod.GET,"/categories").permitAll() //permite visualizar categorias sin autenticación
                                .requestMatchers(HttpMethod.POST,"/categories").hasRole("ADMIN") //solo ADMIN puede crear categorias
                                
                                .requestMatchers(HttpMethod.GET,"/categories/{id}").permitAll() //permite visualizar detalles de una categoria sin autenticación
                                .requestMatchers(HttpMethod.PUT,"/categories/{id}").hasRole("ADMIN") //solo ADMIN puede modificar detalles de una categoria
                                .requestMatchers(HttpMethod.DELETE,"/categories/{id}").hasRole("ADMIN") //solo ADMIN puede eliminar una categoria

                                //PRODUCTOS
                                .requestMatchers(HttpMethod.GET, "/products").permitAll() //permite visualizar productos sin autenticación
                                .requestMatchers(HttpMethod.POST, "/products").hasRole("ADMIN") //solo ADMIN puede crear productos

                                .requestMatchers(HttpMethod.GET, "/products/{productId}").permitAll() //permite visualizar detalles de un producto sin autenticación
                                .requestMatchers(HttpMethod.PUT, "/products/{productId}").hasRole("ADMIN") //solo ADMIN puede modificar detalles de productos
                                .requestMatchers(HttpMethod.DELETE, "/products/{productId}").hasRole("ADMIN") //solo ADMIN puede eliminar productos

                                .requestMatchers(HttpMethod.GET, "/products/by-price").permitAll() //permite visualizar productos por rango de precio sin autenticación
                                

                                //ORDENES
                                .requestMatchers(HttpMethod.GET, "/orders").hasRole("ADMIN") //solo ADMIN puede ver todas las ordenes
                                .requestMatchers(HttpMethod.POST, "/orders").hasRole("USER") //solo USER puede crear ordenes
                                
                                .requestMatchers(HttpMethod.GET, "/orders/{orderId}").hasRole("ADMIN") //solo ADMIN puede ver detalles de una orden
                                                .anyRequest()
                                                .authenticated())
                                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                                .authenticationProvider(authenticationProvider)
                                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }
}
