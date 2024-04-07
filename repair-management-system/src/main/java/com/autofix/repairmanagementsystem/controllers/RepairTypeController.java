package com.autofix.repairmanagementsystem.controllers;

import com.autofix.repairmanagementsystem.entities.RepairTypeEntity;
import com.autofix.repairmanagementsystem.services.RepairTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/repair-types")
public class RepairTypeController {

    private final RepairTypeService repairTypeService;

    @Autowired
    public RepairTypeController(RepairTypeService repairTypeService) {
        this.repairTypeService = repairTypeService;
    }

    @PostMapping
    public ResponseEntity<RepairTypeEntity> createRepairType(@RequestBody RepairTypeEntity repairType) {
        try {
            RepairTypeEntity savedRepairType = repairTypeService.createOrUpdateRepairType(repairType);
            return new ResponseEntity<>(savedRepairType, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<RepairTypeEntity>> getAllRepairTypes() {
        List<RepairTypeEntity> repairTypes = repairTypeService.findAllRepairTypes();
        if (repairTypes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(repairTypes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RepairTypeEntity> getRepairTypeById(@PathVariable("id") Long id) {
        try {
            RepairTypeEntity repairType = repairTypeService.findRepairTypeById(id)
                    .orElseThrow(() -> new Exception("RepairType not found with ID: " + id));
            return new ResponseEntity<>(repairType, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<RepairTypeEntity> updateRepairType(@PathVariable("id") Long id, @RequestBody RepairTypeEntity repairType) {
        try {
            repairType.setRepairTypeId(id);
            RepairTypeEntity updatedRepairType = repairTypeService.createOrUpdateRepairType(repairType);
            return new ResponseEntity<>(updatedRepairType, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteRepairType(@PathVariable("id") Long id) {
        try {
            repairTypeService.deleteRepairType(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
