package com.concyssa.sistemaconcyssa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "cronograma_turnos_zonas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TurnoZona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate fecha; // Fecha específica del turno

    @Column(nullable = false, length = 50)
    private String turno; // Ej: MAÑANA, TARDE, NOCHE

    @Column(nullable = false, length = 100)
    private String zona; // Ej: Estación Sur, Planta Principal, Pozos Sector 4

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "controlador_id", nullable = false)
    private Usuario controlador; // Controlador asignado a la zona y turno
}