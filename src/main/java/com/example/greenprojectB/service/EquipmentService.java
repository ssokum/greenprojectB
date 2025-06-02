package com.example.greenprojectB.service;

import com.example.greenprojectB.entity.Equipment;
import com.example.greenprojectB.repository.EquipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EquipmentService {

  private final EquipmentRepository equipmentRepository;

  public Page<Equipment> getEquipmentPage(Pageable pageable) {
    return equipmentRepository.findAll(pageable);
  }

  public Equipment findById(Long id) {
    return equipmentRepository.findById(id).orElseThrow();
  }

  public Equipment save(Equipment equipment) {
    return equipmentRepository.save(equipment);
  }

  public void delete(Long id) {
    equipmentRepository.deleteById(id);
  }
}
