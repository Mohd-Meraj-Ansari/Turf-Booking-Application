
package com.app.TurfBookingApplication.configuration;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.app.TurfBookingApplication.security.CustomUserDetailsService;

@Configuration
public class SecurityConfig {

    /* ===================== SECURITY FILTER ===================== */

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            DaoAuthenticationProvider authProvider
    ) throws Exception {

        http
            // Disable CSRF (API + React)
            .csrf(csrf -> csrf.disable())

            // Enable CORS INSIDE Spring Security
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            // Authentication provider
            .authenticationProvider(authProvider)

            // Authorization rules
            .authorizeHttpRequests(auth -> auth
                // Allow error handling
                .requestMatchers("/error").permitAll()

                // Allow preflight requests
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // Public endpoints
                .requestMatchers("/api/users/login", "/api/users/register").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/users/test").permitAll()

                // Admin-only
                .requestMatchers(HttpMethod.POST, "/api/users/add-turf").hasRole("ADMIN")

                // Client-only wallet top-up
                .requestMatchers(HttpMethod.POST, "/api/wallet/add-balance").hasRole("CLIENT")
                
                .requestMatchers(HttpMethod.GET, "/api/wallet/balance").hasRole("CLIENT")

                // Everything else requires authentication
                .anyRequest().authenticated()
            )

            // BASIC AUTH
            .httpBasic();

        return http.build();
    }

    /* ===================== PASSWORD ENCODER ===================== */

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /* ===================== AUTH PROVIDER ===================== */

    @Bean
    public DaoAuthenticationProvider authenticationProvider(
            CustomUserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder
    ) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    /* ===================== CORS CONFIG (CRITICAL) ===================== */

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // React frontend
        config.setAllowedOrigins(List.of("http://localhost:5173"));

        // HTTP methods
        config.setAllowedMethods(List.of(
                "GET", "POST", "PUT", "DELETE", "OPTIONS"
        ));

        // MUST explicitly allow Authorization header
        config.setAllowedHeaders(List.of(
                "Authorization",
                "Content-Type",
                "X-Requested-With"
        ));

        // Required for Basic Auth
        config.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
