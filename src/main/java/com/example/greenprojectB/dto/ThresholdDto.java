package com.example.greenprojectB.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThresholdDto {
    
    private int idx;    // 번호
    private int companyId;       // 회사ID
    private String deviceCode;        // 위치
    private double sensor;          // 센서값
    private double min;        // 최솟값
    private double max;        // 최댓값
    private LocalDateTime updatedAt;        // 수정날짜
    private String updatedBy;        // 임계값을 변경한 대상

}
