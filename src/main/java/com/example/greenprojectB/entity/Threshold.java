package com.example.greenprojectB.entity;


import com.example.greenprojectB.dto.ThresholdDto;
import jakarta.persistence.*;
import lombok.*;
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

    @Column(name = "company_id")
    private int companyId;

    @Column(name = "device_code", length = 20)// 기기 번호
    private String deviceCode;

    @Column(name = "sensor", length = 30, nullable = false)
    private double sensor;

    @Column(name = "min")
    private double min;

    @Column(name = "max")
    private double max;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "updated_by", length = 50,nullable = false)
    private String updatedBy;



    // dto to Entity / dto를 엔티티로 변환
    public  static Threshold toEntity(ThresholdDto dto){
        return Threshold.builder()
                .idx(dto.getIdx())
                .companyId(dto.getCompanyId())
                .deviceCode(dto.getDeviceCode())
                .sensor(dto.getSensor())
                .min(dto.getMin())
                .max(dto.getMax())
                .updatedAt((dto.getUpdatedAt()))
                .updatedBy((dto.getUpdatedBy()))
                .build();
    }

}
