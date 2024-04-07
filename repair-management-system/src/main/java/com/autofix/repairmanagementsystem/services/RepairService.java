package com.autofix.repairmanagementsystem.services;

import com.autofix.repairmanagementsystem.entities.RepairEntity;
import com.autofix.repairmanagementsystem.entities.RepairTypeEntity;
import com.autofix.repairmanagementsystem.entities.VehicleEntity;
import com.autofix.repairmanagementsystem.repositories.RepairRepository;
import com.autofix.repairmanagementsystem.repositories.RepairTypeRepository;
import com.autofix.repairmanagementsystem.repositories.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class RepairService {

    private final RepairRepository repairRepository;
    private final VehicleRepository vehicleRepository;
    private final RepairTypeRepository repairTypeRepository;

    @Autowired
    public RepairService(RepairRepository repairRepository, VehicleRepository vehicleRepository, RepairTypeRepository repairTypeRepository) {
        this.repairRepository = repairRepository;
        this.vehicleRepository = vehicleRepository;
        this.repairTypeRepository = repairTypeRepository;
    }

    @Transactional
    public RepairEntity registerRepair(RepairEntity repair) throws Exception {
        // Validación de la existencia del vehículo asociado
        VehicleEntity vehicle = vehicleRepository.findById(repair.getVehicle().getVehicleId())
                .orElseThrow(() -> new Exception("Vehículo no encontrado con ID: " + repair.getVehicle().getVehicleId()));
        repair.setVehicle(vehicle);

        // Validación del tipo de reparación
        RepairTypeEntity repairType = repairTypeRepository.findById(repair.getRepairType().getRepairTypeId())
                .orElseThrow(() -> new Exception("Tipo de reparación no encontrado con ID: " + repair.getRepairType().getRepairTypeId()));
        repair.setRepairType(repairType);

        // Verificación de Fecha y Hora
        if (repair.getExitDate().isBefore(repair.getEntryDate())) {
            throw new IllegalArgumentException("La fecha de salida no puede ser anterior a la fecha de entrada.");
        }
        if (repair.getExitDate().isEqual(repair.getEntryDate()) && repair.getExitTime().isBefore(repair.getEntryTime())) {
            throw new IllegalArgumentException("La hora de salida no puede ser anterior a la hora de entrada en el mismo día.");
        }

        return repairRepository.save(repair);
    }

    public List<RepairEntity> findAllRepairs() {
        return repairRepository.findAll();
    }

    public Optional<RepairEntity> findRepairById(Long repairId) {
        return repairRepository.findById(repairId);
    }

    // Ejemplo de método para encontrar reparaciones dentro de un rango de fechas
    public List<RepairEntity> findRepairsByDateRange(LocalDate start, LocalDate end) {
        return repairRepository.findByEntryDateBetween(start, end);
    }

    // Método para obtener el costo total de reparaciones para un vehículo específico
    public Double findTotalRepairCostByVehicleId(Long vehicleId) {
        return repairRepository.findTotalRepairCostByVehicleId(vehicleId);
    }

    //
    @Transactional
    public void deleteRepair(Long repairId) throws Exception {
        // Verificar si la reparación existe
        RepairEntity repair = repairRepository.findById(repairId)
                .orElseThrow(() -> new Exception("Reparación no encontrada con ID: " + repairId));
        repairRepository.deleteById(repairId);
    }
}
