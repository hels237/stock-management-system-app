package com.k48.stock_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {

    // ex: "NEW_RDV", "RDV_CANCELLED", "RDV_REMINDER"
    private String objMessage;

    private String message;

    // ID de l'entité concernée (article, commande, etc.)
    private Integer entityId;

    private LocalDateTime timestamp;

    // ex: "ADMIN", "USER", "MANAGER"
    private String recipientType;

    // ID de l'utilisateur concerné (pour les notifications privées)
    private Long recipientId;

}
