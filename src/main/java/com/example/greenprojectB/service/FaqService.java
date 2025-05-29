package com.example.greenprojectB.service;

import com.example.greenprojectB.entity.Faq;
import com.example.greenprojectB.repository.FaqRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FaqService {

  private final FaqRepository faqRepository;

  public Page<Faq> getFaqPage(Pageable pageable) {
    return faqRepository.findAll(pageable);
  }

  public Faq findById(Long id) {
    return faqRepository.findById(id).orElseThrow();
  }

  public Faq save(Faq faq) {
    return faqRepository.save(faq);
  }

  public void delete(Long id) {
    faqRepository.deleteById(id);
  }
}
