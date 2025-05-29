package com.example.greenprojectB.entity;

import com.example.greenprojectB.dto.NoticeDto;
import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "notice")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_idx")
    private Long idx; // 번호 (PK)

    @Column(name = "notice_title")
    private String title; // 제목

    @Column(columnDefinition = "TEXT")
    private String content; // 본문

    @Column(name = "notice_writer")
    private String writer; // 작성자

    @Column(nullable = false)
    private String noticePassword; // 수정용 비밀번호

    private int viewCount; // 조회수

    private LocalDateTime createDate; // 작성일

    private LocalDateTime updateDate; // 수정일

    // 자동 생성 시간 처리 , 작성/수정 시점 자동 처리
    @PrePersist
    public void onCreate() {
        this.createDate = LocalDateTime.now();
        this.updateDate = this.createDate;
        this.viewCount = 0;
    }

    @PreUpdate
    public void onUpdate() {
        this.updateDate = LocalDateTime.now();
    }
    //서비스에서 조회수 증가에 사용
    public void increaseViewCount() {
        this.viewCount++;
    }

    // dto to Entity
    public static Notice createNotice(NoticeDto dto) {
        return Notice.builder()
                .idx(dto.getIdx())
                .title(dto.getTitle())
                .content(dto.getContent())
                .noticePassword(dto.getNoticePassword())
                .viewCount(dto.getViewCount())
                .createDate(dto.getCreateDate())
                .updateDate(dto.getUpdateDate())
                .writer(dto.getWriter())
                .build();
    }
}

