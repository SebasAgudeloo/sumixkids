package com.sumixkids.model;

/**
 * Lista fija de roles aceptados.
 * Cada uno guarda su nombre exacto tal como aparece en la base.
 */
public enum RoleType {
    ADMIN("admin", "Administrador", "👑"),
    DOCENT("docent", "Docente", "👨‍🏫"),
    STUDENT("student", "Estudiante", "🎓"),
    ATTENDANT("attendant", "Acompañante/Tutor", "👥");

    private final String dbName;
    private final String displayName;
    private final String emoji;

    RoleType(String dbName, String displayName, String emoji) {
        this.dbName = dbName;
        this.displayName = displayName;
        this.emoji = emoji;
    }

    /** Nombre tal cual se guarda en la tabla de roles. */
    public String dbName() { return dbName; }

    /** Nombre legible para mostrar en la interfaz (español). */
    public String displayName() { return displayName; }

    /** Emoji representativo (usar emoji estándar, evitar Font Awesome PRO). */
    public String emoji() { return emoji; }

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
