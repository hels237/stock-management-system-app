# 📋 Règles Métier - Gestion des Commandes

## 🔄 États des Commandes

### États Disponibles
- **EN_PREPARATION** : Commande en cours de création
- **VALIDEE** : Commande confirmée et validée
- **LIVREE** : Commande livrée au client (état final)

### Diagramme des Transitions
```
EN_PREPARATION → VALIDEE → LIVREE
      ↑            ↑         ↑
   (Création)  (Validation) (Livraison)
```

## 🚦 Règles de Transition d'États

### 1. EN_PREPARATION → VALIDEE
**Conditions requises :**
- ✅ La commande doit contenir au moins un article
- ✅ Tous les articles doivent exister en base de données
- ✅ Le client doit être valide

**Actions automatiques :**
- 📦 Génération des mouvements de sortie de stock
- 📧 Envoi d'email de confirmation au client (si configuré)

### 2. VALIDEE → LIVREE
**Conditions requises :**
- ✅ La commande doit être dans l'état VALIDEE
- ✅ Aucune condition supplémentaire

**Actions automatiques :**
- 🔒 Verrouillage définitif de la commande
- 📧 Notification de livraison (si configuré)

### 3. Transitions Interdites
- ❌ **LIVREE → tout autre état** : État final, aucun retour possible
- ❌ **EN_PREPARATION → LIVREE** : Doit passer par VALIDEE
- ❌ **VALIDEE → EN_PREPARATION** : Pas de retour en arrière

## 🛠️ Règles de Modification par État

### État EN_PREPARATION
| Action | Autorisé | Commentaire |
|--------|----------|-------------|
| Modifier client | ✅ | Libre modification |
| Ajouter articles | ✅ | Libre ajout |
| Modifier quantités | ✅ | Libre modification |
| Supprimer articles | ✅ | Libre suppression |
| Modifier prix | ✅ | Libre modification |

### État VALIDEE
| Action | Autorisé | Commentaire |
|--------|----------|-------------|
| Modifier client | ❌ | Client figé |
| Ajouter articles | ❌ | Articles figés |
| Modifier quantités | ⚠️ | Limité (avec justification) |
| Supprimer articles | ❌ | Articles figés |
| Modifier prix | ❌ | Prix figés |

### État LIVREE
| Action | Autorisé | Commentaire |
|--------|----------|-------------|
| Toute modification | ❌ | Commande complètement figée |

## 📦 Gestion des Stocks

### Règles de Sortie de Stock
1. **Moment de la sortie** : Lors du passage à l'état VALIDEE uniquement
2. **Quantité** : Exactement la quantité commandée
3. **Type de mouvement** : SORTIE avec motif "COMMANDE_CLIENT"
4. **Vérification stock** : Contrôle de disponibilité avant validation

### Cas d'Erreur Stock
- **Stock insuffisant** : Commande rejetée avec message explicite
- **Article inexistant** : Validation impossible
- **Stock négatif** : Alerte générée pour réapprovisionnement

## 🔐 Règles de Sécurité

### Validation des Données
- **ID Commande** : Obligatoire et existant
- **ID Client** : Obligatoire et existant
- **Articles** : Tous doivent exister en base
- **Quantités** : Strictement positives
- **Prix** : Cohérents avec le catalogue

### Contrôles d'Intégrité
- **Unicité** : Un seul état par commande
- **Cohérence** : Dates logiques (création < validation < livraison)
- **Traçabilité** : Historique des changements d'état

## 📧 Notifications Automatiques

### Email de Confirmation (État VALIDEE)
**Destinataire :** Client de la commande
**Contenu :**
- Numéro de commande
- Liste des articles
- Quantités et prix
- Date de livraison estimée

### Email de Livraison (État LIVREE)
**Destinataire :** Client de la commande
**Contenu :**
- Confirmation de livraison
- Numéro de suivi
- Facture finale

## ⚠️ Gestion des Erreurs

### Codes d'Erreur Spécifiques
- **CMDE_CLIENT_NON_MODIFIABLE** : Modification interdite selon l'état
- **TRANSITION_INVALIDE** : Changement d'état non autorisé
- **STOCK_INSUFFISANT** : Quantité en stock insuffisante
- **ARTICLE_INEXISTANT** : Article non trouvé en base

### Messages d'Erreur Types
```
"Transition invalide de LIVREE vers EN_PREPARATION"
"Impossible de modifier une commande livrée"
"Stock insuffisant pour l'article [nom] (demandé: X, disponible: Y)"
"Impossible de valider une commande sans articles"
```

## 🔄 Processus Métier Complet

### 1. Création de Commande
```
1. Validation des données client
2. Vérification existence articles
3. Calcul des prix et totaux
4. État initial : EN_PREPARATION
5. Sauvegarde en base
```

### 2. Validation de Commande
```
1. Vérification état actuel (EN_PREPARATION)
2. Contrôle présence d'articles
3. Vérification stock disponible
4. Génération mouvements de stock
5. Changement état : VALIDEE
6. Envoi email confirmation
```

### 3. Livraison de Commande
```
1. Vérification état actuel (VALIDEE)
2. Changement état : LIVREE
3. Verrouillage définitif
4. Envoi email livraison
5. Mise à jour indicateurs
```

## 📊 Indicateurs et Reporting

### Métriques par État
- Nombre de commandes EN_PREPARATION
- Temps moyen de validation
- Taux de commandes livrées
- Valeur moyenne par commande

### Alertes Automatiques
- Commandes en préparation > 7 jours
- Stock critique après validation
- Erreurs de transition répétées

---

**Version :** 1.0  
**Dernière mise à jour :** $(date)  
**Responsable :** Équipe Développement