package com.emeraldia.backend.security;

import com.emeraldia.backend.security.jwt.AuthEntryPointJwt;
import com.emeraldia.backend.security.jwt.AuthTokenFilter;
import com.emeraldia.backend.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {
  @Autowired
  UserDetailsServiceImpl userDetailsService;

  @Autowired
  private AuthEntryPointJwt unauthorizedHandler;

  /**
   * Creates a bean for the authentication JWT token filter.
   *
   * @return AuthTokenFilter instance
   */
  @Bean
  public AuthTokenFilter authenticationJwtTokenFilter() {
    return new AuthTokenFilter(); // Returns a new instance of AuthTokenFilter
  }

  /**
   * Creates a bean for the DAO authentication provider.
   *
   * @return DaoAuthenticationProvider instance
   */
  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(); // Create a new authentication provider

    authProvider.setUserDetailsService(userDetailsService); // Set the user details service
    authProvider.setPasswordEncoder(passwordEncoder()); // Set the password encoder

    return authProvider; // Return the configured authentication provider
  }

  /**
   * Creates a bean for the authentication manager.
   *
   * @param authConfig Authentication configuration
   * @return AuthenticationManager instance
   * @throws Exception if there is an error getting the authentication manager
   */
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager(); // Returns the authentication manager from the configuration
  }

  /**
   * Creates a bean for the password encoder.
   *
   * @return PasswordEncoder instance
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(); // Returns a new instance of BCryptPasswordEncoder
  }

  /**
   * Configures the security filter chain for HTTP requests.
   *
   * @param http HttpSecurity configuration
   * @return SecurityFilterChain instance
   * @throws Exception if there is an error configuring the security filter chain
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    // Configure CSRF protection, exception handling, session management, and authorization
    http.csrf(AbstractHttpConfigurer::disable) // Disable CSRF protection
            .exceptionHandling(exception ->
                    exception.authenticationEntryPoint(unauthorizedHandler))
            // Set unauthorized handler
            .sessionManagement(session ->
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // Set session policy to stateless
            .authorizeHttpRequests(auth -> auth
                    // Configure authorization for HTTP requests
                    .requestMatchers("/api/auth/**").permitAll()
                    .requestMatchers("/api/products/**").permitAll()
                    .requestMatchers("/api/users/**").permitAll()
                    .requestMatchers("/api/categories/**").permitAll()
                    .requestMatchers("/api/cart/**").permitAll()
                    // Allow public access to test endpoints
                    .anyRequest().authenticated());
    // Require authentication for any other request

    http.authenticationProvider(authenticationProvider()); // Set the authentication provider

    // Add the JWT token filter before the username/password authentication filter
    http.addFilterBefore(authenticationJwtTokenFilter(),
            UsernamePasswordAuthenticationFilter.class);

    return http.build(); // Build and return the security filter chain
  }

  @Configuration // Ensure this class is marked as a Configuration class
  public class CorsConfig { // Give it a meaningful name

    @Bean
    public WebMvcConfigurer corsConfigurer() {
      return new WebMvcConfigurer() {
        @Override
        public void addCorsMappings(CorsRegistry registry) {
          registry.addMapping("/**") // Apply CORS to all endpoints
                  .allowedOrigins(
                          "http://localhost:4200"
                  )
                  .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allowed HTTP methods
                  .allowedHeaders("*") // Allowed headers (allow all)
                  .allowCredentials(true); // Allow sending cookies/auth headers
        }
      };
    }
  }
}
