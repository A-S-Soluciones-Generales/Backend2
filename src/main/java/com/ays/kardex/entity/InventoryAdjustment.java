package com.ays.kardex.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "inventory_adjustments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryAdjustment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AdjustmentType type;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "stock_resulting", nullable = false)
    private Integer stockResulting;

    @Column(name = "reason_code", nullable = false, length = 50)
    private String reasonCode;

    @Column(nullable = false, length = 255)
    private String note;

    @Column(name = "value_impact", nullable = false)
    private Double valueImpact;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sede_id", nullable = false)
    private Sede sede;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }

    public enum AdjustmentType {
        NEGATIVE,
        POSITIVE
    }
}
