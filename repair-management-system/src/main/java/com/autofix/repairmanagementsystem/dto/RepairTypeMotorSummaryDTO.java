package com.autofix.repairmanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class RepairTypeMotorSummaryDTO {
    private String repairTypeDescription;
    private String engineType;
    private Long vehicleCount;
    private BigDecimal totalCost;
}
