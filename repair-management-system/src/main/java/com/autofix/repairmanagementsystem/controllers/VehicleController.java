package com.autofix.repairmanagementsystem.controllers;

import com.autofix.repairmanagementsystem.entities.VehicleEntity;
import com.autofix.repairmanagementsystem.services.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vehicles")
@CrossOrigin("*")
public class VehicleController {

    private final VehicleService vehicleService;

    @Autowired
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping
    public ResponseEntity<VehicleEntity> addVehicle(@RequestBody VehicleEntity vehicle) {
        try {
            VehicleEntity savedVehicle = vehicleService.registerOrUpdateVehicle(vehicle);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedVehicle);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null); // Consider returning a more informative error structure.
        }
    }

    @GetMapping
    public ResponseEntity<List<VehicleEntity>> getAllVehicles() {
        List<VehicleEntity> vehicles = vehicleService.findAllVehicles();
        if (vehicles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(vehicles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleEntity> getVehicleById(@PathVariable("id") Long id) {
        try {
            VehicleEntity vehicle = vehicleService.findVehicleById(id);
            return ResponseEntity.ok(vehicle);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehicleEntity> updateVehicle(@PathVariable("id") Long id, @RequestBody VehicleEntity vehicle) {
        try {
            vehicle.setVehicleId(id);
            VehicleEntity updatedVehicle = vehicleService.registerOrUpdateVehicle(vehicle);
            return ResponseEntity.ok(updatedVehicle);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteVehicle(@PathVariable("id") Long id) {
        try {
            vehicleService.deleteVehicle(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}