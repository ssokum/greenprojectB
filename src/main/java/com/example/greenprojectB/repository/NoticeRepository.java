package com.example.greenprojectB.repository;

import com.example.greenprojectB.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long>, QuerydslPredicateExecutor<Notice> {
    // 검색 (제목 또는 내용)
    Page<Notice> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String titleKeyword, String contentKeyword, Pageable pageable);

    // 이전글 (현재 idx보다 작은 것 중 가장 큰 것)
    Optional<Notice> findTopByIdxLessThanOrderByIdxDesc(Long idx);

    // 다음글 (현재 idx보다 큰 것 중 가장 작은 것)
    Optional<Notice> findTopByIdxGreaterThanOrderByIdxAsc(Long idx);
}
