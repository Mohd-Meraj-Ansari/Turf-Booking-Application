package com.app.TurfBookingApplication.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.app.TurfBookingApplication.security.CustomUserDetailsService;

@Configuration
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http,
	                                       DaoAuthenticationProvider authProvider) throws Exception {

	    http
	        .csrf(csrf -> csrf.disable())
	        .cors(cors -> {})
	        .authenticationProvider(authProvider)   // ⭐ VERY IMPORTANT
	        .authorizeHttpRequests(auth -> auth
	                .requestMatchers(HttpMethod.GET, "/api/users/test").permitAll()
	                .requestMatchers(HttpMethod.POST, "/api/users/register").permitAll()
	                .requestMatchers(HttpMethod.GET, "/api/users/all").permitAll()
	                .requestMatchers(HttpMethod.POST, "/api/users/add-turf").hasRole("ADMIN")
	                .requestMatchers(HttpMethod.PUT, "/api/users/update-profile").authenticated()
	                .requestMatchers("/api/users/login", "/api/users/register").permitAll()
	                .requestMatchers("/api/accessories/**").authenticated()
	                .anyRequest().authenticated()
	        )
	        .httpBasic();

	    return http.build();
	}


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public DaoAuthenticationProvider authenticationProvider(
            CustomUserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }
}
