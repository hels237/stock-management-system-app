package com.k48.stock_management_system.controller.api;

import com.k48.stock_management_system.dto.PhotoUploadResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

public interface PhotoApi {

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Photo téléchargée avec succès",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Fichier invalide ou erreur de téléchargement")
    })
    @Operation(summary = "Télécharger une photo", description = "Permet de télécharger une photo pour une entité (article, utilisateur, client, fournisseur, entreprise)")
    @PostMapping(value = "/upload/{entityType}/{entityId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    PhotoUploadResponseDto uploadPhoto(
            @RequestParam("file") MultipartFile file,
            @PathVariable("entityType") String entityType,
            @PathVariable("entityId") Integer entityId
    );

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Photo supprimée avec succès"),
            @ApiResponse(responseCode = "404", description = "Photo non trouvée")
    })
    @Operation(summary = "Supprimer une photo", description = "Permet de supprimer une photo en utilisant son ID public")
    @DeleteMapping("/delete/{publicId}")
    void deletePhoto(@PathVariable("publicId") String publicId);
}
