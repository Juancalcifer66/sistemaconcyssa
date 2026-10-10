package com.concyssa.sistemaconcyssa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "visitas_terceros")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VisitaTercero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer numeroSistema;

    private String estacion;

    @Column(name = "nombre_empresa", nullable = false)
    private String nombreEmpresa;

    private String motivo;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "foto_url")
    private String fotoUrl;

    @Column(name = "fecha_visita", nullable = false)
    private LocalDateTime fechaVisita;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "controlador_id", nullable = false)
    private Usuario controlador;
}