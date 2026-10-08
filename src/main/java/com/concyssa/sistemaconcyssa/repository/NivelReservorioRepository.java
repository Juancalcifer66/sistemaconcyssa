package com.concyssa.sistemaconcyssa.repository;

import com.concyssa.sistemaconcyssa.entity.NivelReservorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NivelReservorioRepository extends JpaRepository<NivelReservorio, Long> {
    List<NivelReservorio> findByControladorId(Long controladorId);
}