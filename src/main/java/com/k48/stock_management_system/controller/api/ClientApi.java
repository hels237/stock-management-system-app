package com.k48.stock_management_system.controller.api;

import com.k48.stock_management_system.dto.ClientDto;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface ClientApi {

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "L'objet client cree ",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClientDto.class))),
            @ApiResponse(responseCode = "404", description = "L'objet client n'est pas valide")
    })
    ClientDto save(@RequestBody ClientDto dto);

    @GetMapping(value = "/{idClient}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Le client a ete trouve dans la BD",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClientDto.class))),
            @ApiResponse(responseCode = "404", description = "Aucun client n'existe dans la BDD avec l'ID fourni")
    })
    ClientDto findById(@PathVariable("idClient") Integer id);

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La liste des clients ",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClientDto.class, type = "array"))),
            @ApiResponse(responseCode = "404", description = "Aucun client n'existe dans la BD")
    })
    List<ClientDto> findAll();

    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "Le client a ete supprime de la BD",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClientDto.class))),
            @ApiResponse(responseCode = "404", description = "Aucun client n'existe dans la BD avec l'ID fourni")
    })
    @DeleteMapping(value = "delete/{idClient}")
    ClientDto delete(@PathVariable("idClient") Integer id);
}
