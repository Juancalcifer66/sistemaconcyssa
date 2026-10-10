package com.concyssa.sistemaconcyssa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "niveles_reservorio")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NivelReservorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_sistema")
    private Integer numeroSistema;

    @Column(name = "estacion", length = 100)
    private String estacion;

    @Column(name = "pasos_llenos")
    private Integer pasosLlenos;

    @Column(name = "pasos_libres")
    private Integer pasosLibres;

    @Column(name = "observacion")
    private String observacion;

    @Builder.Default
    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario controlador;
}