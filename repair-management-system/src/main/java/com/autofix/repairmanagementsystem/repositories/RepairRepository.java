package com.autofix.repairmanagementsystem.repositories;

import com.autofix.repairmanagementsystem.entities.RepairEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RepairRepository extends JpaRepository<RepairEntity, Long> {
    // Encuentra reparaciones por tipo de reparación
    List<RepairEntity> findByRepairTypeDescription(String description);

    // Encuentra reparaciones dentro de un rango de fechas
    List<RepairEntity> findByEntryDateBetween(LocalDate start, LocalDate end);

    // Encuentra reparaciones por estado
    List<RepairEntity> findByStatus(String status);

    // Encuentra reparaciones recientes por vehículo
    @Query(value = "SELECT r FROM RepairEntity r WHERE r.vehicle.vehicleId = :vehicleId ORDER BY r.entryDate DESC, r.entryTime DESC")
    List<RepairEntity> findRecentRepairsByVehicleId(@Param("vehicleId") Long vehicleId, Pageable pageable);

    // Encontrar reparaciones por el tipo de vehículo
    @Query("SELECT r FROM RepairEntity r WHERE r.vehicle.type = :type")
    List<RepairEntity> findByVehicleType(String type);

    // Obtener el costo total de las reparaciones para un vehículo específico
    @Query("SELECT SUM(r.repairCost) FROM RepairEntity r WHERE r.vehicle.vehicleId = :vehicleId")
    Double findTotalRepairCostByVehicleId(Long vehicleId);

    // Encontrar las reparaciones más costosas, limitando los resultados
    @Query(value = "SELECT * FROM repairs ORDER BY repair_cost DESC LIMIT :limit", nativeQuery = true)
    List<RepairEntity> findTopExpensiveRepairs(int limit);

    // Obtener el número de reparaciones por cada tipo de reparación
    @Query("SELECT r.repairType.description, COUNT(r) FROM RepairEntity r GROUP BY r.repairType.description")
    List<Object[]> countRepairsByType();

    // Obtener el costo promedio de las reparaciones por tipo de vehículo
    @Query("SELECT AVG(r.repairCost), r.vehicle.type FROM RepairEntity r GROUP BY r.vehicle.type")
    List<Object[]> findAverageRepairCostByVehicleType();

    // Obtener el número de reparaciones por vehículo
    @Query("SELECT COUNT(r) FROM RepairEntity r WHERE r.vehicle.vehicleId = :vehicleId AND r.entryDate >= :startDate")
    Long countRepairsByVehicleIdAndDateRange(@Param("vehicleId") Long vehicleId, @Param("startDate") LocalDate startDate);
}