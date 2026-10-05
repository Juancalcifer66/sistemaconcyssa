package com.concyssa.sistemaconcyssa.repository;

import com.concyssa.sistemaconcyssa.entity.OrdenTrabajo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrdenTrabajoRepository extends JpaRepository<OrdenTrabajo, Long>, JpaSpecificationExecutor<OrdenTrabajo> {

    Optional<OrdenTrabajo> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);

    List<OrdenTrabajo> findByOperadorAsignadoId(Long operadorId);
}
