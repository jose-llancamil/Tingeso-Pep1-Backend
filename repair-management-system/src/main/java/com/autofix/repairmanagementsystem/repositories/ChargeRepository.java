package com.autofix.repairmanagementsystem.repositories;

import com.autofix.repairmanagementsystem.entities.ChargeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChargeRepository extends JpaRepository<ChargeEntity, Long> {
    // Encuentra recargos por descripción
    List<ChargeEntity> findByDescriptionContainingIgnoreCase(String description);

    // Encuentra recargos por tipo de recargo
    List<ChargeEntity> findByChargeType(ChargeEntity.ChargeType chargeType);

    // Encuentra recargos aplicables a ciertos tipos de vehículos
    List<ChargeEntity> findByApplicableType(String type);
}
