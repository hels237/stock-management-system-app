package com.k48.stock_management_system.controller;

import com.k48.stock_management_system.controller.api.AuthApi;
import com.k48.stock_management_system.dto.auth.AuthResponse;
import com.k48.stock_management_system.dto.auth.LoginRequestDto;
import com.k48.stock_management_system.model.Utilisateur;
import com.k48.stock_management_system.repositories.UtilisateurRepository;
import com.k48.stock_management_system.security.JwtServiceUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import static com.k48.stock_management_system.utils.Constants.APP_ROOT;

@Tag(name = "Authentification", description = "API pour l'authentification des utilisateurs")
@RestController
@RequestMapping(APP_ROOT + "auth")
@RequiredArgsConstructor
public class AuthController implements AuthApi {


    private final AuthenticationManager authenticationManager;
    private final JwtServiceUtil jwtServiceUtil;
    private final UtilisateurRepository utilisateurRepository;

    @Override
    public ResponseEntity<?> login(LoginRequestDto request) {
        try {
            // Tente d'authentifier l'utilisateur avec l'email et le mot de passe
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            // Si l'authentification réussit, met à jour le contexte de sécurité
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Récupère l'objet Utilisateur complet pour générer le token avec les claims nécessaires
            Utilisateur user = utilisateurRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé après authentification."));

            // Génère le token JWT
            String token = jwtServiceUtil.generateToken(user);

            // Retourne le token au client
            return ResponseEntity.ok(new AuthResponse(token));
        } catch (AuthenticationException e) {
            // Gère les échecs d'authentification (ex: mauvais identifiants)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Identifiants invalides: " + e.getMessage());
        }
    }


    @Override
    public ResponseEntity<?> logout() {
        // Pour un système basé sur JWT et stateless, "logout" côté serveur
        //  on peut juste effacer le contexte de sécurité actuel ,
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Déconnexion réussie");
    }


    @Override
    public ResponseEntity<?> getCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(auth.getPrincipal());
    }
}
