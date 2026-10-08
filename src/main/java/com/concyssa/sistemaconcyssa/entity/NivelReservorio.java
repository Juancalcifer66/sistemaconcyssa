package com.concyssa.sistemaconcyssa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "niveles_reservorios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NivelReservorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer numeroSistema;

    @Column(nullable = false, length = 50)
    private String estacion;

    @Column(nullable = false)
    private Integer pasosLlenos;

    @Column(nullable = false)
    private Integer pasosLibres;

    @Column(columnDefinition = "TEXT")
    private String observacion;

    @Column(nullable = false)
    private LocalDateTime fechaRegistro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario controlador;
}