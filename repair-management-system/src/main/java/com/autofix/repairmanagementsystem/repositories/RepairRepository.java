package com.autofix.repairmanagementsystem.repositories;

import com.autofix.repairmanagementsystem.entities.RepairEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RepairRepository extends JpaRepository<RepairEntity, Long> {
    // Encuentra reparaciones dentro de un rango de fechas
    List<RepairEntity> findByEntryDateBetween(LocalDate start, LocalDate end);

    // Obtener el costo total de las reparaciones para un vehículo específico
    @Query("SELECT SUM(r.repairCost) FROM RepairEntity r WHERE r.vehicle.vehicleId = :vehicleId")
    Double findTotalRepairCostByVehicleId(Long vehicleId);

    // Obtener el número de reparaciones por vehículo
    @Query("SELECT COUNT(r) FROM RepairEntity r WHERE r.vehicle.vehicleId = :vehicleId AND r.entryDate >= :startDate")
    Long countRepairsByVehicleIdAndDateRange(@Param("vehicleId") Long vehicleId, @Param("startDate") LocalDate startDate);
}