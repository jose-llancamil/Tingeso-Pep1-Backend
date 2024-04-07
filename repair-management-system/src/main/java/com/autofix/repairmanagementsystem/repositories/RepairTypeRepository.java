package com.autofix.repairmanagementsystem.repositories;

import com.autofix.repairmanagementsystem.dto.RepairTypeAverageCostDto;
import com.autofix.repairmanagementsystem.entities.RepairTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RepairTypeRepository extends JpaRepository<RepairTypeEntity, Long> {
    // Encuentra tipos de reparación por descripción
    List<RepairTypeEntity> findByDescriptionContaining(String description);

    // Consulta personalizada para encontrar tipos de reparación con un costo base promedio superior a un valor específico
    @Query("SELECT rt FROM RepairTypeEntity rt WHERE (rt.baseCostGasoline + rt.baseCostDiesel + rt.baseCostHybrid + rt.baseCostElectric) / 4.0 > :averageCost")
    List<RepairTypeEntity> findByAverageBaseCostGreaterThan(Double averageCost);

    // Obtener los costos base promedio de todos los tipos de reparación
    @Query("SELECT new com.autofix.repairmanagementsystem.dto.RepairTypeAverageCostDto(rt.description, (rt.baseCostGasoline + rt.baseCostDiesel + rt.baseCostHybrid + rt.baseCostElectric) / 4.0) FROM RepairTypeEntity rt")
    List<RepairTypeAverageCostDto> findAverageBaseCosts();

    @Query("SELECT COUNT(r) FROM RepairEntity r WHERE r.repairType.repairTypeId = :repairTypeId")
    long countByRepairTypeId(@Param("repairTypeId") Long repairTypeId);
}
