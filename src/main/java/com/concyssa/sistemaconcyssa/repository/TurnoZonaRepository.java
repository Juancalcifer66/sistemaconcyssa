package com.concyssa.sistemaconcyssa.repository;

import com.concyssa.sistemaconcyssa.entity.TurnoZona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TurnoZonaRepository extends JpaRepository<TurnoZona, Long> {
    
    @Query("SELECT t FROM TurnoZona t WHERE t.fecha BETWEEN :inicio AND :fin")
    List<TurnoZona> findByRangoFechas(@Param("inicio") LocalDate inicio, @Param("fin") LocalDate fin);

    List<TurnoZona> findByControladorIdAndFechaBetween(Long controladorId, LocalDate inicio, LocalDate fin);
}