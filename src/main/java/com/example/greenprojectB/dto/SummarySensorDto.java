package com.example.greenprojectB.dto;

import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SummarySensorDto {

    private String sensorName;
    private double min;
    private double mean;
    private double max;
    private long eventCnt;

}
