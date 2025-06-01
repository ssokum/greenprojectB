package com.example.greenprojectB.dto;

import com.example.greenprojectB.entity.Company;
import com.example.greenprojectB.entity.Threshold;
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
    private Company company;       // 회사ID
    private String sensorName;      // 센서 이름
    private String deviceCode;        // 위치
    private double low;        // 최솟값
    private double high;        // 최댓값
    private LocalDateTime updatedAt;        // 수정날짜
    private String updatedBy;        // 임계값을 변경한 대상

    // Entity to DTO
    public ThresholdDto createThresholdDto(Threshold threshold) {
        return ThresholdDto.builder()
                .idx(threshold.getIdx())
                .company(threshold.getCompany())
                .sensorName(threshold.getSensorName())
                .deviceCode(threshold.getDeviceCode())
                .high(threshold.getHigh())
                .low(threshold.getLow())
                .updatedAt(threshold.getUpdatedAt())
                .updatedBy(threshold.getUpdatedBy())
                .build();
    }
}
