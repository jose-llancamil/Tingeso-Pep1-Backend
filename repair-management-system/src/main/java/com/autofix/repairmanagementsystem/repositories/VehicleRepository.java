package com.autofix.repairmanagementsystem.repositories;

import com.autofix.repairmanagementsystem.entities.VehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<VehicleEntity, Long> {
    // Buscar vehículos por marca
    List<VehicleEntity> findByBrand(String brand);

    // Buscar vehículos por tipo de motor
    List<VehicleEntity> findByEngineType(String engineType);

    // Buscar vehículos por año de fabricación mayor a un valor específico
    List<VehicleEntity> findByManufactureYearGreaterThan(Integer year);

    // Buscar vehículos con una cantidad específica de asientos
    @Query(value = "SELECT v FROM VehicleEntity v WHERE v.seatCount = :seatCount")
    List<VehicleEntity> findBySeatCount(@Param("seatCount") Integer seatCount);

    // Contar vehículos por tipo
    @Query(value = "SELECT v.type, COUNT(v) FROM VehicleEntity v GROUP BY v.type", nativeQuery = true)
    List<Object[]> countVehiclesByType();
}
