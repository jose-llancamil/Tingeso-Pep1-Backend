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

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class RepairService {

    private final RepairRepository repairRepository;
    private final VehicleRepository vehicleRepository;
    private final RepairTypeRepository repairTypeRepository;
    private final DiscountService discountService;
    private final ChargeService chargeService;
    private final BonusService bonusService;

    @Autowired
    public RepairService(RepairRepository repairRepository, VehicleRepository vehicleRepository,
                         RepairTypeRepository repairTypeRepository, DiscountService discountService,
                         ChargeService chargeService, BonusService bonusService) {
        this.repairRepository = repairRepository;
        this.vehicleRepository = vehicleRepository;
        this.repairTypeRepository = repairTypeRepository;
        this.discountService = discountService;
        this.chargeService = chargeService;
        this.bonusService = bonusService;
    }

    @Transactional
    public RepairEntity registerRepair(RepairEntity repair) throws Exception {
        // Validar la existencia del vehículo asociado y el tipo de reparación, así como la coherencia de las fechas.
        validateRepair(repair);

        // Guardar la información de la reparación en la base de datos.
        return repairRepository.save(repair);
    }

    private void validateRepair(RepairEntity repair) throws Exception {
        VehicleEntity vehicle = vehicleRepository.findById(repair.getVehicle().getVehicleId())
                .orElseThrow(() -> new Exception("Vehículo no encontrado con ID: " + repair.getVehicle().getVehicleId()));
        repair.setVehicle(vehicle);

        RepairTypeEntity repairType = repairTypeRepository.findById(repair.getRepairType().getRepairTypeId())
                .orElseThrow(() -> new Exception("Tipo de reparación no encontrado con ID: " + repair.getRepairType().getRepairTypeId()));
        repair.setRepairType(repairType);

        if (repair.getExitDate().isBefore(repair.getEntryDate()) ||
                (repair.getExitDate().isEqual(repair.getEntryDate()) && repair.getExitTime().isBefore(repair.getEntryTime()))) {
            throw new IllegalArgumentException("La fecha y hora de salida no pueden ser anteriores a la fecha y hora de entrada.");
        }
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

    public BigDecimal calculateDayOfWeekDiscount(LocalDate entryDate, LocalTime entryTime) {
        DayOfWeek dayOfWeek = entryDate.getDayOfWeek();
        BigDecimal discountPercentage = BigDecimal.ZERO;

        // Verifica si es lunes o jueves
        boolean isDiscountDay = dayOfWeek == DayOfWeek.MONDAY || dayOfWeek == DayOfWeek.THURSDAY;

        // Verifica si la hora está entre las 09:00 y las 12:00
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(12, 0);
        boolean isInTimeRange = !entryTime.isBefore(startTime) && entryTime.isBefore(endTime);

        // Si cumple ambos criterios, se aplica un descuento del 10%
        if (isDiscountDay && isInTimeRange) {
            discountPercentage = new BigDecimal("10");
        }

        return discountPercentage;
    }

    public BigDecimal calculatePickupDelayCharge(Long repairId) {
        RepairEntity repair = repairRepository.findById(repairId)
                .orElseThrow(() -> new RuntimeException("Reparación no encontrada con ID: " + repairId));

        LocalDate readyDate = repair.getExitDate(); // Suponiendo que `exitDate` es cuando el vehículo está listo para ser recogido
        LocalDate pickupDate = repair.getCustomerPickupDate();

        // Calcula el número de días de retraso
        long daysDelayed = ChronoUnit.DAYS.between(readyDate, pickupDate);

        if (daysDelayed <= 0) {
            // No hay retraso
            return BigDecimal.ZERO;
        } else {
            // Recargo del 5% por cada día de retraso
            BigDecimal totalRepairCost = repair.getRepairCost(); // Asumiendo que `repairCost` es el costo total de la reparación antes de aplicar cualquier recargo
            BigDecimal dailyDelayChargePercentage = new BigDecimal("0.05"); // 5%
            BigDecimal delayCharge = totalRepairCost.multiply(dailyDelayChargePercentage).multiply(new BigDecimal(daysDelayed));

            return delayCharge;
        }
    }

    @Transactional
    public BigDecimal calculateTotalRepairCost(Long repairId) throws Exception {
        RepairEntity repair = findRepairById(repairId)
                .orElseThrow(() -> new Exception("Reparación no encontrada con ID: " + repairId));

        BigDecimal baseRepairCost = repair.getRepairCost(); // Costo base de la reparación
        BigDecimal totalCharges = BigDecimal.ZERO; // Inicialización de los recargos totales
        BigDecimal totalDiscounts = BigDecimal.ZERO; // Inicialización de los descuentos totales

        // Calculo de descuentos aplicables
        BigDecimal dayOfWeekDiscount = calculateDayOfWeekDiscount(repair.getEntryDate(), repair.getEntryTime());
        BigDecimal discountAmount = baseRepairCost.multiply(dayOfWeekDiscount.divide(new BigDecimal(100)));
        totalDiscounts = totalDiscounts.add(discountAmount);

        BigDecimal additionalDiscount = discountService.determineDiscountPercentage(repair.getVehicle().getVehicleId(), repair.getVehicle().getEngineType());
        BigDecimal additionalDiscountAmount = baseRepairCost.multiply(additionalDiscount.divide(new BigDecimal(100)));
        totalDiscounts = totalDiscounts.add(additionalDiscountAmount);

        // Calculo de recargos aplicables
        BigDecimal pickupDelayCharge = calculatePickupDelayCharge(repairId);
        totalCharges = totalCharges.add(pickupDelayCharge);

        BigDecimal mileageChargePercentage = chargeService.determineMileageChargePercentage(repair.getVehicle().getVehicleId());
        BigDecimal mileageCharge = baseRepairCost.multiply(mileageChargePercentage.divide(new BigDecimal(100)));
        totalCharges = totalCharges.add(mileageCharge);

        // Calculo de bonos aplicables si corresponde
        BigDecimal bonusAmount = bonusService.calculateBonusForVehicle(repair.getVehicle().getVehicleId());
        totalDiscounts = totalDiscounts.add(bonusAmount);

        // Cálculo final
        BigDecimal totalAfterDiscounts = baseRepairCost.subtract(totalDiscounts);
        BigDecimal totalAfterCharges = totalAfterDiscounts.add(totalCharges);

        // Suponiendo un IVA del 19%
        BigDecimal tax = totalAfterCharges.multiply(new BigDecimal("0.19"));
        BigDecimal totalWithTax = totalAfterCharges.add(tax);

        return totalWithTax;
    }

}
