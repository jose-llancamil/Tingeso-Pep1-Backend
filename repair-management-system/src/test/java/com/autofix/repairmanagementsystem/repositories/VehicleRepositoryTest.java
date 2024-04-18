package com.autofix.repairmanagementsystem.repositories;

import com.autofix.repairmanagementsystem.entities.VehicleEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test") // Use this if you have a specific profile for test configurations
public class VehicleRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Test
    void findByLicensePlateNumber_WhenVehicleExists_ShouldReturnVehicle() {
        // Arrange
        VehicleEntity vehicle = new VehicleEntity();
        vehicle.setLicensePlateNumber("ABC123");
        vehicle.setBrand("Toyota");
        vehicle.setModel("Corolla");
        vehicle.setType("Sedan");
        vehicle.setManufactureYear(2020);
        vehicle.setEngineType("Gasoline");
        vehicle.setMileage(10000);
        vehicle.setSeatCount(5);
        entityManager.persist(vehicle);
        entityManager.flush();

        // Act
        Optional<VehicleEntity> foundVehicle = vehicleRepository.findByLicensePlateNumber("ABC123");

        // Assert
        assertThat(foundVehicle).isPresent();
        assertThat(foundVehicle.get().getLicensePlateNumber()).isEqualTo("ABC123");
    }

    @Test
    void findByLicensePlateNumber_WhenVehicleDoesNotExist_ShouldReturnEmpty() {
        // Act
        Optional<VehicleEntity> foundVehicle = vehicleRepository.findByLicensePlateNumber("XYZ999");

        // Assert
        assertThat(foundVehicle).isNotPresent();
    }
}
