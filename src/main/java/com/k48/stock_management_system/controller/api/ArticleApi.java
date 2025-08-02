package com.k48.stock_management_system.controller.api;

import com.k48.stock_management_system.dto.ArticleDto;
import com.k48.stock_management_system.dto.LigneCmdeClientDto;
import com.k48.stock_management_system.dto.LigneCmdeFournisseurDto;
import com.k48.stock_management_system.dto.LigneVenteDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.k48.stock_management_system.utils.Constants.APP_ROOT;

@Tag(name = "Articles", description = "API pour la gestion des articles")
@RequestMapping(APP_ROOT)
public interface ArticleApi {

    @PostMapping(value =  "/articles/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = " Enregistrer un article", description = "Cette methode permet de creer  un article")
    @ApiResponse(responseCode = "200", description = "L'objet article cree / modifie")
    @ApiResponse(responseCode ="400", description = "L'objet article n'est pas valide")
    ArticleDto save(@RequestBody ArticleDto dto);

    @GetMapping(value = "/articles/{idArticle}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary= "Rechercher un article par ID")
    @ApiResponse(responseCode = "200",description = "L'article a ete trouve dans la BDD")
    @ApiResponse(responseCode = "404", description = "Aucun article n'existe dans la BDD avec l'ID fourni")
    ArticleDto findById(@PathVariable("idArticle") Integer id);

    @GetMapping(value = "/articles/filter/{codeArticle}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Rechercher un article par CODE", description = "Cette methode permet de chercher un article par son CODE")

    @ApiResponse(responseCode ="200", description = "L'article a ete trouve dans la BDD")
    @ApiResponse(responseCode = "404", description= "Aucun article n'existe dans la BDD avec le CODE fourni")
    ArticleDto findByCodeArticle(@PathVariable("codeArticle") String codeArticle);

    @GetMapping(value = "/articles/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Renvoi la liste des articles", description = "Cette methode permet de chercher et renvoyer la liste des articles qui existent " + "dans la BD")
    @ApiResponse(responseCode = "200", description = "La liste des article / Une liste vide")
    List<ArticleDto> findAll();

    @Operation(summary = "Renvoi la liste des lignes de vente d'un article", description = "Cette methode permet de chercher et renvoyer toutes les  lignes de vente d'un article")
    @GetMapping(value = "/articles/historique/vente/{idArticle}", produces = MediaType.APPLICATION_JSON_VALUE)
    List<LigneVenteDto> findHistoriqueVentes(@PathVariable("idArticle") Integer idArticle);

    @Operation(summary = "Renvoi la liste des lignes de commande d'un client pour un article", description = "Cette methode permet de chercher et renvoyer toutes les lignes de commande d'un client pour un article")
    @GetMapping(value = "/articles/historique/commandeclient/{idArticle}", produces = MediaType.APPLICATION_JSON_VALUE)
    List<LigneCmdeClientDto> findHistoriqueCommandeClient(@PathVariable("idArticle") Integer idArticle);

    @Operation(summary = "Renvoi la liste des lignes de commande fournisseur d'un article", description = "Cette methode permet de chercher et renvoyer toutes les lignes de commande fournisseur où un article a été acheté ")
    @GetMapping(value =  "/articles/historique/commandefournisseur/{idArticle}", produces = MediaType.APPLICATION_JSON_VALUE)
    List<LigneCmdeFournisseurDto> findHistoriqueCommandeFournisseur(@PathVariable("idArticle") Integer idArticle);

    @Operation(summary = "Renvoi la liste des articles par ID de categorie", description = "Cette methode permet de chercher et renvoyer la liste des articles par ID de categorie")
    @GetMapping(value =  "/articles/filter/category/{idCategory}", produces = MediaType.APPLICATION_JSON_VALUE)
    List<ArticleDto> findAllArticleByIdCategory(@PathVariable("idCategory") Integer idCategory);

    @DeleteMapping(value = "/articles/delete/{idArticle}")
    @Operation(summary = "Supprimer un article", description = "Cette methode permet de supprimer un article par ID")
    @ApiResponse(responseCode ="200", description = "L'article a ete supprime")
    ArticleDto delete(@PathVariable("idArticle") Integer id);
}
