package com.example.greenprojectB.entity;

import com.example.greenprojectB.dto.HistoryDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "history")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class History {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "history_id")
  private Long id;

  @Column(nullable = false)
  private LocalDateTime historyDate;

  private String content;

  //Dto to Entity
  public static History createHistory(HistoryDto dto) {
    return History.builder()
            .historyDate(dto.getHistoryDate())
            .content(dto.getContent())
            .build();
  }
}
