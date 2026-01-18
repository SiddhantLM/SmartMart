package com.examly.springapp.config;
 
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.servlet.http.HttpServletResponse;
 
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    PasswordEncoder passwordEncoder () {return new BCryptPasswordEncoder();}
 
    @Autowired
    UserDetailsService service;
 
    @Bean
    DaoAuthenticationProvider daoAuthenticationProvider () {
        DaoAuthenticationProvider d = new DaoAuthenticationProvider();
        d.setPasswordEncoder(passwordEncoder());
        d.setUserDetailsService(service);
        return d;
    }
 
    @Autowired
    JwtAuthenticationFilter filter;
 
    @Bean
    SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
            .authorizeHttpRequests()
            .requestMatchers("/api/login", "/api/register", "/api/user/send-otp", "/api/products").permitAll()
            .anyRequest().authenticated()
            .and()
            .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)            
            .exceptionHandling(e -> e
                .authenticationEntryPoint((req, res, ex) -> {
                    res.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
                    res.getWriter().write("Custom Unauthorized: Invalid or missing token");
                })
                .accessDeniedHandler((req, res, ex) -> {
                    res.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403
                    res.getWriter().write("Custom Forbidden: You don't have the required role");
                })
            )
            .httpBasic(Customizer.withDefaults());
 
        return http.build();
    }
    @Bean
    public AccessDeniedHandler accessDeniedHandler()
    {
        return (request,response,accessDeniedException)->response.sendError(HttpServletResponse.SC_FORBIDDEN,"Forbidden");
    }

}