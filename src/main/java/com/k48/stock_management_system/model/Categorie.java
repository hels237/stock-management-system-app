package com.k48.stock_management_system.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;


@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Categorie extends AbstractEntity{

    private String code;
    private String designation;
    private String description;

    @OneToMany(mappedBy = "categorie")
    private List<Article> articles;
}
