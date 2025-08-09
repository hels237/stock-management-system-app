package com.k48.stock_management_system.controller;

import com.k48.stock_management_system.controller.api.ClientApi;
import com.k48.stock_management_system.dto.ClientDto;
import com.k48.stock_management_system.services.ClientService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.k48.stock_management_system.utils.Constants.APP_ROOT;

@RestController
@Tag(name = "Client", description = "API pour la gestion des clients")
@RequestMapping(APP_ROOT + "clients")
@RequiredArgsConstructor
public class ClientController implements ClientApi {
    private final ClientService clientService;

    @Override
    public ClientDto save(ClientDto dto) {
        return clientService.save(dto);
    }

    @Override
    public ClientDto findById(Integer id) {
        return clientService.findById(id);
    }

    @Override
    public List<ClientDto> findAll() {
        return clientService.findAll();
    }

    @Override
    public ClientDto delete(Integer id) {
        return clientService.delete(id);
    }
}
