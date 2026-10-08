package com.concyssa.sistemaconcyssa.repository;

import com.concyssa.sistemaconcyssa.entity.DanioFalla;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DanioFallaRepository extends JpaRepository<DanioFalla, Long> {
    List<DanioFalla> findByControladorId(Long controladorId);
}