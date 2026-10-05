package com.concyssa.sistemaconcyssa.repository.specification;

import com.concyssa.sistemaconcyssa.entity.OrdenTrabajo;
import com.concyssa.sistemaconcyssa.enums.EstadoOrden;
import com.concyssa.sistemaconcyssa.enums.PrioridadOrden;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class OrdenTrabajoSpecification {

    public static Specification<OrdenTrabajo> conFiltros(EstadoOrden estado, PrioridadOrden prioridad, Long operadorId) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (estado != null) {
                predicates.add(criteriaBuilder.equal(root.get("estado"), estado));
            }

            if (prioridad != null) {
                predicates.add(criteriaBuilder.equal(root.get("prioridad"), prioridad));
            }

            if (operadorId != null) {
                predicates.add(criteriaBuilder.equal(root.get("operadorAsignado").get("id"), operadorId));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
