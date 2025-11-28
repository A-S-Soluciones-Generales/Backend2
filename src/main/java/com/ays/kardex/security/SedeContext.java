package com.ays.kardex.security;

public class SedeContext {

    private static final ThreadLocal<Long> SEDE_ID = new ThreadLocal<>();

    private SedeContext() {
    }

    public static void setSedeId(Long sedeId) {
        SEDE_ID.set(sedeId);
    }

    public static Long getSedeId() {
        return SEDE_ID.get();
    }

    public static void clear() {
        SEDE_ID.remove();
    }
}
