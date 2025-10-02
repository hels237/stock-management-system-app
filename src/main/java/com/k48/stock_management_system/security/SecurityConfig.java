package com.k48.stock_management_system.security;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Permet d'utiliser @PreAuthorize pour la sécurité basée sur les méthodes
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final CustomUserService userDetailsService;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable) // Désactive CSRF pour les APIs stateless
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Active CORS
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/gestiondestock/v1/auth/**").permitAll() // Autorise l'authentification sans JWT
                        .requestMatchers(
                                        "/swagger-ui/**",
                                        "/swagger-ui.html",
                                        "/v3/api-docs/**",
                                        "/v3/api-docs.yaml").permitAll() // Autorise Swagger UI et OpenAPI
                        .requestMatchers("/ws/**").permitAll() // WebSocket pour notifications
                        
                        // Gestion des entreprises - ADMIN seulement
                        .requestMatchers("/gestiondestock/v1/entreprise/**").hasRole("ADMIN")
                        
                        // Gestion des utilisateurs - ADMIN seulement
                        .requestMatchers("/gestiondestock/v1/utilisateur/**").hasRole("ADMIN")
                        
                        // Gestion des catégories - ADMIN seulement
                        .requestMatchers("/gestiondestock/v1/category/**").hasRole("ADMIN")
                        
                        // Gestion des articles - ADMIN et FOURNISSEUR
                        .requestMatchers("/gestiondestock/v1/article/**").hasAnyRole("ADMIN", "FOURNISSEUR")
                        
                        // Gestion des clients - ADMIN seulement
                        .requestMatchers("/gestiondestock/v1/client/**").hasRole("ADMIN")
                        
                        // Gestion des fournisseurs - ADMIN seulement
                        .requestMatchers("/gestiondestock/v1/fournisseur/**").hasRole("ADMIN")
                        
                        // Commandes clients - ADMIN et CLIENT
                        .requestMatchers("/gestiondestock/v1/commandeclient/**").hasAnyRole("ADMIN", "CLIENT")
                        
                        // Commandes fournisseurs - ADMIN et FOURNISSEUR
                        .requestMatchers("/gestiondestock/v1/commandefournisseur/**").hasAnyRole("ADMIN", "FOURNISSEUR")
                        
                        // Ventes - ADMIN seulement
                        .requestMatchers("/gestiondestock/v1/vente/**").hasRole("ADMIN")
                        
                        // Mouvements de stock - ADMIN et FOURNISSEUR
                        .requestMatchers("/gestiondestock/v1/mvtstock/**").hasAnyRole("ADMIN", "FOURNISSEUR")
                        
                        // Photos upload/delete - Tous les rôles authentifiés
                        .requestMatchers("/gestiondestock/v1/photos/upload/**", "/gestiondestock/v1/photos/delete/**").hasAnyRole("ADMIN", "CLIENT", "FOURNISSEUR")
                        
                        // Photos affichage - Accès public pour le frontend
                        .requestMatchers("/gestiondestock/v1/photos/*/*/photo").permitAll()
                        
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Pas de session HTTP
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) // Ajoute notre filtre JWT
                .build();
    }

    // Bean pour la configuration CORS (Cross-Origin Resource Sharing)
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Permettre tous les origines, ou des origines spécifiques
        configuration.setAllowedOriginPatterns(List.of("*")); // Utiliser setAllowedOriginPatterns pour les motifs
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true); // Autorise les credentials (cookies, en-têtes d'autorisation)
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Appliquer cette configuration à toutes les routes
        return source;
    }
}
