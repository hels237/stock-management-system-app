package com.k48.stock_management_system.exceptions;

public enum ErrorCode {

    ARTICLE_NOT_FOUND(1000),
    CATEGORIE_NOT_FOUND(2000),
    CMDE_CLIENT_NOT_FOUND(3000),
    CMDE_FOURNISSEUR_NOT_FOUND(4000),
    ENTREPPRISE_NOT_FOUND(5000),
    FOURNISSEUR_NOT_FOUND(6000),
    lIGNE_CMDE_CLIENT_NOT_FOUND(7000),
    LIGNE_CMDE_FOURNISSEUR_NOT_FOUND(8000),
    LIGNE_VENTURE_NOT_FOUND(9000),
    MVT_STOCK_NOT_FOUND(10000),
    UTILISATEUR_NOT_FOUND(11000),
    VENTE_NOT_FOUND(11001),
    CLIENT_NOT_FOUND(12000),
    EMPTY_LIST(13000);



    private int code;

    ErrorCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
