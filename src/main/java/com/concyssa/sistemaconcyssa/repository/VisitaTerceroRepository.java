package com.concyssa.sistemaconcyssa.repository;

import com.concyssa.sistemaconcyssa.entity.VisitaTercero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitaTerceroRepository extends JpaRepository<VisitaTercero, Long> {
    List<VisitaTercero> findByControladorId(Long controladorId);
}