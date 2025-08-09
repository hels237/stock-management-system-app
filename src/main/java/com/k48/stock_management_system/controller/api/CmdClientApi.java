package com.k48.stock_management_system.controller.api;

import com.k48.stock_management_system.dto.CmdeClientDto;
import com.k48.stock_management_system.dto.LigneCmdeClientDto;
import com.k48.stock_management_system.model.EtatCmde;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

public interface CmdClientApi {

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Commande client créée avec succès",
                    content = @Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = CmdeClientDto.class))),
            @ApiResponse(responseCode = "400", description = "La requête est invalide ou les données fournies sont incorrectes")
    })
    @PostMapping("/commandesclients/create")
    CmdeClientDto save(@RequestBody CmdeClientDto dto);


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Commande client mise à jour avec succès",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CmdeClientDto.class))),
            @ApiResponse(responseCode = "404", description = "Aucune commande client trouvée avec l'ID fourni")
    })
    @PatchMapping("/commandesclients/update/etat/{idCommande}/{etatCommande}")
    CmdeClientDto updateEtatCmde(@PathVariable("idCommande") Integer idCommande, @PathVariable("etatCommande") EtatCmde etatCommande);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quantité de la commande client mise à jour avec succès",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CmdeClientDto.class))),
            @ApiResponse(responseCode = "404", description = "Aucune commande client trouvée avec l'ID fourni")
    })
    @PatchMapping("/commandesclients/update/quantite/{idCommande}/{idLigneCommande}/{quantite}")
    CmdeClientDto updateQuantiteCommande(@PathVariable("idCommande") Integer idCommande,
                                                             @PathVariable("idLigneCommande") Integer idLigneCommande, @PathVariable("quantite") BigDecimal quantite);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client de la commande client mis à jour avec succès",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CmdeClientDto.class))),
            @ApiResponse(responseCode = "404", description = "Aucune commande client trouvée avec l'ID fourni")
    })
    @PatchMapping("/commandesclients/update/client/{idCommande}/{idClient}")
    CmdeClientDto updateClient(@PathVariable("idCommande") Integer idCommande, @PathVariable("idClient") Integer idClient);


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Article de la commande client mis à jour avec succès",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CmdeClientDto.class))),
            @ApiResponse(responseCode = "404", description = "Aucune commande client trouvée avec l'ID fourni")
    })
    @PatchMapping("/commandesclients/update/article/{idCommande}/{idLigneCommande}/{idArticle}")
    CmdeClientDto updateArticle(@PathVariable("idCommande") Integer idCommande, @PathVariable("idLigneCommande") Integer idLigneCommande, @PathVariable("idArticle") Integer idArticle);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Article supprimé de la commande client avec succès",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CmdeClientDto.class))),
            @ApiResponse(responseCode = "404", description = "Aucune commande client trouvée avec l'ID fourni")
    })
    @DeleteMapping("/commandesclients/delete/article/{idCommande}/{idLigneCommande}")
    CmdeClientDto deleteArticle(@PathVariable("idCommande") Integer idCommande, @PathVariable("idLigneCommande") Integer idLigneCommande);


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Commande client trouvée avec succès",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CmdeClientDto.class))),
            @ApiResponse(responseCode = "404", description = "Aucune commande client trouvée avec l'ID fourni")
    })
    @GetMapping("/commandesclients/{idCommandeClient}")
    CmdeClientDto findById(@PathVariable Integer idCommandeClient);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Commande client trouvée avec succès",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CmdeClientDto.class))),
            @ApiResponse(responseCode = "404", description = "Aucune commande client trouvée avec le code fourni")
    })
    @GetMapping("/commandesclients/filter/{codeCommandeClient}")
    CmdeClientDto findByCode(@PathVariable("codeCommandeClient") String code);


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste de toutes les commandes clients",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CmdeClientDto.class, type = "array"))),
            @ApiResponse(responseCode = "404", description = "Aucune commande client trouvée")
    })
    @GetMapping("/commandesclients/all")
    List<CmdeClientDto> findAll();


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des lignes de commande client pour la commande spécifiée",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LigneCmdeClientDto.class, type = "array"))),
            @ApiResponse(responseCode = "404", description = "Aucune ligne de commande client trouvée pour la commande avec l'ID fourni")
    })
    @GetMapping("/commandesclients/lignesCommande/{idCommande}")
    List<LigneCmdeClientDto> findAllLignesCommandesClientByCommandeClientId(@PathVariable("idCommande") Integer idCommande);


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Commande client supprimée avec succès",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CmdeClientDto.class))),
            @ApiResponse(responseCode = "404", description = "Aucune commande client trouvée avec l'ID fourni")
    })
    @DeleteMapping("/commandesclients/delete/{idCommandeClient}")
    CmdeClientDto delete(@PathVariable("idCommandeClient") Integer id);
}
