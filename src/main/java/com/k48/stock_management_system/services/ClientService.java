package com.k48.stock_management_system.services;

import com.k48.stock_management_system.dto.ClientDto;

import java.util.List;


public interface ClientService  {

    ClientDto save(ClientDto dto);

    ClientDto findById(Integer id);

    List<ClientDto> findAll();

    ClientDto delete(Integer id);
}
