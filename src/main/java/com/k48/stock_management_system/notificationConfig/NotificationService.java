package com.k48.stock_management_system.notificationConfig;


import com.k48.stock_management_system.dto.NotificationDto;
import com.k48.stock_management_system.model.Article;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


/*
* ----------------------les composants  cles --------------------------
*
* WebSocket : Connexion persistante pour les communications bidirectionnelles
* STOMP : Protocole de messagerie sur WebSocket (comme HTTP pour WS)
* SockJS : Fallback pour les navigateurs ne supportant pas WS
* SimpMessagingTemplate : Outil Spring pour envoyer des messages via WS
*
* --------------- flux de fonctionement ------------------------
* Le client (navigateur) établit une connexion WebSocket
* Le client s'abonne à des canaux de notification (/topic/... , /user/queue/...)
* Le serveur envoie des notifications via ces canaux lorsque des événements se produisent
* Le client reçoit et affiche les notifications en temps réel
*
* */

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;
    private final EmailService emailService;



    // Notification privée à un utilisateur
    public void sendPrivateNotification(Long userId, NotificationDto notification) {
        messagingTemplate.convertAndSendToUser(
                userId.toString(),
                "/queue/privateNotifications",
                notification
        );
    }

    // Notification publique (pour tous les abonnés)
    public void sendPublicNotification(NotificationDto notification) {
        messagingTemplate.convertAndSend("/topic/publicNotifications", notification);
    }


    // Notification de stock faible
    public void sendStockAlert(Article article, Integer currentStock, Integer minStock) {
        NotificationDto notif = new NotificationDto(
                "STOCK_ALERT",
                "Stock faible pour l'article: " + article.getDesignation() + " (Stock: " + currentStock + ", Min: " + minStock + ")",
                article.getId(),
                LocalDateTime.now(),
                "ADMIN",
                null
        );

        sendPublicNotification(notif);
    }

    // Notification de nouvelle commande
    public void sendNewOrderNotification(Integer orderId, String orderType, Long userId) {
        NotificationDto notif = new NotificationDto(
                "NEW_ORDER",
                "Nouvelle commande " + orderType + " créée (ID: " + orderId + ")",
                orderId,
                LocalDateTime.now(),
                "USER",
                userId
        );

        sendPrivateNotification(userId, notif);
    }


}



