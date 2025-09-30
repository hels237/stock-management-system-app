package com.k48.stock_management_system.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.List;


@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Utilisateur extends AbstractEntity implements UserDetails {

    private String nom;

    private String prenom;

    private String email;

    private String photo;

    private String motDePasse;

    private Instant dateDeNaissance;

    @Embedded
    private Adresse adresse;

    @ManyToOne
    @JoinColumn(name = "entrepriseId")
    private Entreprise entreprise;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Retourne une liste de GrantedAuthority basée sur le rôle de l'utilisateur.
        // Spring Security attend des rôles préfixés par "ROLE_".
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {return motDePasse;}

    @Override
    public String getUsername() {return email;}

    @Override
    public boolean isAccountNonExpired() {return true;}

    @Override
    public boolean isAccountNonLocked() {return true;}

    @Override
    public boolean isCredentialsNonExpired() {return true;}

    @Override
    public boolean isEnabled() {return true;}
}
