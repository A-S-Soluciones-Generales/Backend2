package com.ays.kardex.audit;

import java.util.Map;

public final class AuditLogContext {

    private static final ThreadLocal<AuditData> CONTEXT = new ThreadLocal<>();

    private AuditLogContext() {
    }

    public static void register(String action, Map<String, Object> details) {
        CONTEXT.set(new AuditData(action, details));
    }

    public static AuditData getCurrent() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }

    public record AuditData(String action, Map<String, Object> details) {
    }
}
