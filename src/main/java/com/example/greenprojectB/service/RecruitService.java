package com.example.greenprojectB.service;

import com.example.greenprojectB.entity.Recruit;
import com.example.greenprojectB.repository.RecruitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecruitService {

  private final RecruitRepository recruitRepository;

  public Recruit save(Recruit recruit) {
    return recruitRepository.save(recruit);
  }

  public Page<Recruit> findAll(Pageable pageable) {
    return recruitRepository.findAll(pageable);
  }

  public Recruit findById(Long id) {
    return recruitRepository.findById(id).orElse(null);
  }

  public void delete(Long id) {
    recruitRepository.deleteById(id);
  }
}
