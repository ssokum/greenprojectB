package com.example.greenprojectB.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SummarySensorDto {
    // 센서 데이터 종합 정리

    private int summarySensorId;    // 번호
    private String companyId;       // 회사ID
    private String sensor;          // 센서 종류
    private double minValue;        // 최솟값
    private double meanValue;       // 평균값
    private double maxValue;        // 최댓값
    private int event;              // 이벤트 발생 횟수



}
