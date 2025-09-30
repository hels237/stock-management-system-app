package com.k48.stock_management_system.controller.api;

import com.k48.stock_management_system.dto.auth.AuthResponse;
import com.k48.stock_management_system.dto.auth.LoginRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface AuthApi {

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Connexion réussie",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "401", description = "Identifiants invalides")
    })
    @Operation(summary = "Connexion utilisateur", description = "Authentifie un utilisateur et retourne un token JWT")
    @PostMapping("/login")
    ResponseEntity<?> login(@RequestBody LoginRequestDto request);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Déconnexion réussie")
    })
    @Operation(summary = "Déconnexion utilisateur", description = "Déconnecte l'utilisateur en effaçant le contexte de sécurité")
    @PostMapping("/logout")
    ResponseEntity<?> logout();

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Utilisateur actuel récupéré avec succès"),
            @ApiResponse(responseCode = "401", description = "Non authentifié")
    })
    @Operation(summary = "Obtenir l'utilisateur actuel", description = "Retourne les informations de l'utilisateur actuellement connecté")
    @GetMapping("/me")
    ResponseEntity<?> getCurrentUser();
}