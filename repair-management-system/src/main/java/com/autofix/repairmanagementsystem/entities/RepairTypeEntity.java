package com.autofix.repairmanagementsystem.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "repair_types")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepairTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "repair_type_id", unique = true, nullable = false)
    private Long repairTypeId;

    @Column(nullable = false)
    private String description;

    @Column(name = "base_cost_gasoline", nullable = false)
    private BigDecimal baseCostGasoline;

    @Column(name = "base_cost_diesel", nullable = false)
    private BigDecimal baseCostDiesel;

    @Column(name = "base_cost_hybrid", nullable = false)
    private BigDecimal baseCostHybrid;

    @Column(name = "base_cost_electric", nullable = false)
    private BigDecimal baseCostElectric;
}
