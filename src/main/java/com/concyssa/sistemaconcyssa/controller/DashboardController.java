package com.concyssa.sistemaconcyssa.controller;

import com.concyssa.sistemaconcyssa.repository.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard de Supervisión", description = "Consolidado de métricas y totales de los 6 reportes operativos")
public class DashboardController {

    private final OrdenTrabajoRepository ordenTrabajoRepository;
    private final DanioFallaRepository danioFallaRepository;
    private final MedicionCloroRepository medicionCloroRepository;
    private final NivelReservorioRepository nivelReservorioRepository;
    private final ApagonInterrupcionRepository apagonInterrupcionRepository;
    private final VisitaTerceroRepository visitaTerceroRepository;

    @Operation(summary = "Obtener resumen general de reportes para el dashboard del supervisor")
    @GetMapping("/resumen")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<Map<String, Long>> obtenerResumenReportes() {
        Map<String, Long> resumen = new HashMap<>();
        resumen.put("totalOrdenesTrabajo", ordenTrabajoRepository.count());
        resumen.put("totalDaniosFallas", danioFallaRepository.count());
        resumen.put("totalMedicionesCloro", medicionCloroRepository.count());
        resumen.put("totalNivelesReservorios", nivelReservorioRepository.count());
        resumen.put("totalApagones", apagonInterrupcionRepository.count());
        resumen.put("totalVisitasTerceros", visitaTerceroRepository.count());

        return ResponseEntity.ok(resumen);
    }
}