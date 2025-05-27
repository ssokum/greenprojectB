package com.example.greenprojectB.dto;

import com.example.greenprojectB.entity.History;
import com.example.greenprojectB.entity.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoryDto {


  private Long id;

  private LocalDateTime historyDate;

  @NotEmpty(message = "내용을 입력해 주세요.")
  private String content;

  //Entity to Dto

  // Entity to DTO
  public HistoryDto createMemberDto(History history) {
    return HistoryDto.builder()
            .id(history.getId())
            .historyDate(history.getHistoryDate())
            .content(history.getContent())
            .build();
  }
}
