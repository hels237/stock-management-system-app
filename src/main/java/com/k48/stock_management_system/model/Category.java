package com.k48.stock_management_system.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;


@SuperBuilder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Category extends AbstractEntity{

    private String code;

    private String designation;

    private String description;

    private Integer idEntreprise;

    @OneToMany(mappedBy = "category")
    private List<Article> articles;
}
