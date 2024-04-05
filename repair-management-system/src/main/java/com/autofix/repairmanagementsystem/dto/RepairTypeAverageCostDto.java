package com.autofix.repairmanagementsystem.dto;


import java.math.BigDecimal;

public class RepairTypeAverageCostDto {

    private String description;
    private BigDecimal averageCost;

    public RepairTypeAverageCostDto(String description, BigDecimal averageCost) {
        this.description = description;
        this.averageCost = averageCost;
    }

    // Getters

    public String getDescription() {
        return description;
    }

    public BigDecimal getAverageCost() {
        return averageCost;
    }

    // Setters

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAverageCost(BigDecimal averageCost) {
        this.averageCost = averageCost;
    }
}
