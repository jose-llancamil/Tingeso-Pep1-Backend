package com.autofix.repairmanagementsystem.repositories;

import com.autofix.repairmanagementsystem.entities.DiscountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DiscountRepository extends JpaRepository<DiscountEntity, Long> {
    // Búsqueda de descuentos por descripción, ignorando mayúsculas y minúsculas.
    List<DiscountEntity> findByDescriptionContainingIgnoreCase(String description);

    // Búsqueda de descuentos por tipo.
    List<DiscountEntity> findByDiscountType(DiscountEntity.DiscountType discountType);

    // Búsqueda de descuentos aplicables a una marca específica de vehículo.
    List<DiscountEntity> findByApplicableBrand(String brand);
}
