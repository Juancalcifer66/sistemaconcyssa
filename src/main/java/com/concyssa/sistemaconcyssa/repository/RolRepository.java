package com.concyssa.sistemaconcyssa.repository;

import com.concyssa.sistemaconcyssa.entity.Rol;
import com.concyssa.sistemaconcyssa.enums.RolNombre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {
    Optional<Rol> findByNombre(RolNombre nombre);
}

