package com.concyssa.sistemaconcyssa.repository;

import com.concyssa.sistemaconcyssa.entity.Asistencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {
    List<Asistencia> findByControladorId(Long controladorId);
    Optional<Asistencia> findTopByControladorIdAndFechaSalidaIsNullOrderByFechaIngresoDesc(Long controladorId);
}