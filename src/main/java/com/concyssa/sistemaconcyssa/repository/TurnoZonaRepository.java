package com.concyssa.sistemaconcyssa.repository;

import com.concyssa.sistemaconcyssa.entity.TurnoZona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TurnoZonaRepository extends JpaRepository<TurnoZona, Long> {
    List<TurnoZona> findByMesIgnoreCase(String mes);
}