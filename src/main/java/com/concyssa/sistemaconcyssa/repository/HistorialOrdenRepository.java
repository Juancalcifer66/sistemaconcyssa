package com.concyssa.sistemaconcyssa.repository;

import com.concyssa.sistemaconcyssa.entity.HistorialOrden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistorialOrdenRepository extends JpaRepository<HistorialOrden, Long> {

    List<HistorialOrden> findByOrdenTrabajoIdOrderByFechaCambioDesc(Long ordenId);
}

