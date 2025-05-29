package com.example.greenprojectB.entity;


import com.example.greenprojectB.dto.SummarySensorDto;
import com.example.greenprojectB.dto.ThresholdDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "threshold")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Threshold {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //오토인크리먼트 설정
    @Column(name = "idx") //센서 번호
    private int idx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", referencedColumnName = "company_id", nullable = false)
    private Company company;

    @Column(name = "sensor_name", length = 50, nullable = false)
    private String sensorName;

    @Column(name = "device_code", length = 20)// 기기 번호
    private String deviceCode;


    @Column(name = "low")
    private double low;

    @Column(name = "high")
    private double high;

    @CreatedDate
    private LocalDateTime updatedAt;

    @Column(name = "updated_by", length = 50,nullable = false)
    private String updatedBy;

    @Transient
    private SummarySensorDto summarySensor;



    // dto to Entity / dto를 엔티티로 변환
    public  static Threshold toEntity(ThresholdDto dto){
        return Threshold.builder()
                .idx(dto.getIdx())
                .company(dto.getCompany())
                .sensorName(dto.getSensorName())
                .deviceCode(dto.getDeviceCode())
                .low(0)
                .high(0)
                .updatedAt(dto.getUpdatedAt())
                .updatedBy(dto.getUpdatedBy())
                .summarySensor(null)
                .build();
    }

}
