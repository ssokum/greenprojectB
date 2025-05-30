package com.example.greenprojectB.dto;

import com.example.greenprojectB.entity.Member;
import com.example.greenprojectB.entity.Notice;
import com.example.greenprojectB.entity.Sensor;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeDto {
    private Long idx;                   // 공지 번호
    private String title;               // 제목
    private String content;             // 본문
    private String noticePassword;      // 수정용 비밀번호
    private LocalDateTime createDate;   // 작성일
    private LocalDateTime updateDate;   // 수정일
    private int viewCount;              // 조회수
    private String writer;              // 작성자
    private String oFileNames;           // 사용자가 올린 실제 파일명
    private String sFileNames;           // 서버에 저장된 파일명

    //entity to dto / entity를 dto로 변환
    public static NoticeDto createNoticeDto(Optional<Notice> opNotice) {
        return NoticeDto.builder()
                .idx(opNotice.get().getIdx())
                .title(opNotice.get().getTitle())
                .content(opNotice.get().getContent())
                .noticePassword(opNotice.get().getNoticePassword())
                .viewCount(opNotice.get().getViewCount())
                .createDate(opNotice.get().getCreateDate())
                .updateDate(opNotice.get().getUpdateDate())
                .writer(opNotice.get().getWriter())
                .oFileNames(opNotice.get().getOFileNames())
                .sFileNames(opNotice.get().getSFileNames())
                .build();
    }
}
