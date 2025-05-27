package com.example.greenprojectB.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThresholdDto {
    // 센서 데이터 종합 정리

    private int idx;    // 번호
    private String companyId;       // 회사ID
    private String divceCode;        // 위치
    private String sensor;          // 센서 종류
    private double min;        // 최솟값
    private double max;        // 최댓값

}
