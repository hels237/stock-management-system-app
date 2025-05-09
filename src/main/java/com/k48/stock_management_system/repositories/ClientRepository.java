package com.k48.stock_management_system.repositories;

import com.k48.stock_management_system.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client,Integer> {
    Optional<Client> findByEmail(String email);

}
