package com.concyssa.sistemaconcyssa.repository;

import com.concyssa.sistemaconcyssa.entity.ApagonInterrupcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApagonInterrupcionRepository extends JpaRepository<ApagonInterrupcion, Long> {
    List<ApagonInterrupcion> findByControladorId(Long controladorId);
}