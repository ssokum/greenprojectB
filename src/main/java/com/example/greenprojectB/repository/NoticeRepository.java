package com.example.greenprojectB.repository;

import com.example.greenprojectB.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface NoticeRepository extends JpaRepository<Notice, Long>, QuerydslPredicateExecutor<Notice> {
    Page<Notice> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String titleKeyword, String contentKeyword, Pageable pageable);
}
