package com.concyssa.sistemaconcyssa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "visitas_terceros")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VisitaTercero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer numeroSistema;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String descripcion;

    @Column(nullable = false, length = 100)
    private String datosControl; // Ej: Empresa contratista, fotocheck, DNI del visitante

    @Column(length = 255)
    private String fotoUrl;

    @Column(nullable = false)
    private LocalDateTime fechaVisita;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario controlador;
}