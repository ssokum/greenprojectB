package com.example.greenprojectB.repository;

import com.example.greenprojectB.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository extends JpaRepository<History, Long> {
}
