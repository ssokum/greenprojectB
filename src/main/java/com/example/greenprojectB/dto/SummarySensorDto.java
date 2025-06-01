package com.example.greenprojectB.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SummarySensorDto {

    private String sensorName;
    private String deviceCode;
    private Stats stats;
    private long eventCnt;

}
