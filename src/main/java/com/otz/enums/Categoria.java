package com.otz.enums;

public enum Categoria {
    REMERAS("Remeras"),
    BUZOS("Buzos"),
    CAMISAS("Camisas"),
    PANTALONES("Pantalones"),
    ACCESORIOS("Accesorios"),
    VESTIDOS("Vestidos"),
    SHORTS("Shorts"),
    CHOMBAS("Chombas");

    private final String displayName;

    Categoria(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
