package com.concyssa.sistemaconcyssa.repository;

import com.concyssa.sistemaconcyssa.entity.MedicionCloro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicionCloroRepository extends JpaRepository<MedicionCloro, Long> {
    List<MedicionCloro> findByControladorId(Long controladorId);
}