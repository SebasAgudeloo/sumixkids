package com.sumixkids.model;

/**
 * Lista fija de roles aceptados.
 * Cada uno guarda su nombre exacto tal como aparece en la base.
 */
public enum RoleType {
    ADMIN("admin"),
    DOCENT("docent"),
    STUDENT("student"),
    PARENTS("parents");

    private final String dbName;

    RoleType(String dbName) {
        this.dbName = dbName;
    }

    /** Nombre tal cual se guarda en la tabla de roles. */
    public String dbName() {
        return dbName;
    }

    /** Convierte un nombre de texto al valor del enum (no diferencia mayúsculas/minúsculas). */
    public static RoleType fromDbName(String name) {
        if (name == null) return null;
        String norm = name.trim().toLowerCase();
        for (RoleType r : values()) {
            if (r.dbName.equals(norm)) return r;
        }
        throw new IllegalArgumentException("Rol desconocido: " + name);
    }
}
