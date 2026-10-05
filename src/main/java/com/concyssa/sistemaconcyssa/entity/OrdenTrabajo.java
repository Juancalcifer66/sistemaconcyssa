package com.concyssa.sistemaconcyssa.entity;

import com.concyssa.sistemaconcyssa.enums.EstadoOrden;
import com.concyssa.sistemaconcyssa.enums.PrioridadOrden;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "ordenes_trabajo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdenTrabajo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String codigo;

    @Column(nullable = false, length = 500)
    private String descripcion;

    @Column(nullable = false, length = 255)
    private String direccion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoOrden estado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PrioridadOrden prioridad;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_programada")
    private LocalDateTime fechaProgramada;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creador_id", nullable = false)
    private Usuario creador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operador_asignado_id")
    private Usuario operadorAsignado;

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
        if (this.estado == null) {
            this.estado = EstadoOrden.PENDIENTE;
        }
        if (this.prioridad == null) {
            this.prioridad = PrioridadOrden.MEDIA;
        }
    }
}
