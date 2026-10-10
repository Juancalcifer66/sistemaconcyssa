package com.concyssa.sistemaconcyssa.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "programacion_supervisores")
public class TurnoZona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String mes; // Ej: "Octubre 2026"

    @Column(name = "nombre_completo", nullable = false)
    private String nombreCompleto;

    @Column(nullable = false)
    private String zona; // "Zona Alta", "Zona Baja", "Zona Centro"

    @Column(nullable = false)
    private String turno; // "Mañana", "Tarde", "Noche"
}