package com.autofix.repairmanagementsystem.services;

import com.autofix.repairmanagementsystem.entities.ChargeEntity;
import com.autofix.repairmanagementsystem.repositories.ChargeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ChargeService {

    private final ChargeRepository chargeRepository;

    @Autowired
    public ChargeService(ChargeRepository chargeRepository) {
        this.chargeRepository = chargeRepository;
    }

    @Transactional
    public ChargeEntity createCharge(ChargeEntity charge) {
        return chargeRepository.save(charge);
    }

    public List<ChargeEntity> findAllCharges() {
        return chargeRepository.findAll();
    }

    public Optional<ChargeEntity> findChargeById(Long id) {
        return chargeRepository.findById(id);
    }

    @Transactional
    public ChargeEntity updateCharge(Long id, ChargeEntity chargeDetails) {
        ChargeEntity charge = chargeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Carga no encontrada con el ID: " + id));
        charge.setDescription(chargeDetails.getDescription());
        charge.setAmount(chargeDetails.getAmount());
        charge.setChargeType(chargeDetails.getChargeType());
        charge.setApplicableType(chargeDetails.getApplicableType());
        return chargeRepository.save(charge);
    }

    @Transactional
    public void deleteCharge(Long id) {
        chargeRepository.deleteById(id);
    }
}
