package com.example.greenprojectB.entity;

import com.example.greenprojectB.dto.EventLogDto;
import com.example.greenprojectB.dto.ThresholdDto;
import jakarta.persistence.*;
import jdk.jfr.Name;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "eventLog")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class EventLog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "company_id", length = 20)
  private String companyId;

  @Column(name = "device_code", length = 20)// 기기 번호
  private String deviceCode;

  @CreatedDate
  @Column(name = "event_time")
  private LocalDateTime eventTime;

  @Column(name = "event_type", length = 100)
  private String eventType;

  @Column(name = "event_subtype", length = 100)
  private String eventSubtype;

  @Column(length = 100)
  private String message;

  @Column(name = "data")
  private double data;

  // dto to Entity / dto를 엔티티로 변환
  public  static EventLog toEntity(EventLogDto dto){
    return EventLog.builder()
            .id(dto.getId())
            .companyId(dto.getCompanyId())
            .deviceCode(dto.getDeviceCode())
            .eventTime(dto.getEventTime())
            .eventType(dto.getEventType())
            .eventSubtype(dto.getEventSubType())
            .message(dto.getMessage())
            .data(dto.getData())
            .build();

  }

}
