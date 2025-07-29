package com.k48.stock_management_system.repositories;

import com.k48.stock_management_system.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    public Optional<Category> findByCode(String code);
}
