package com.example.greenprojectB.dto;

import com.example.greenprojectB.entity.EventLog;
import com.example.greenprojectB.entity.History;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventLogDto {

    private int id;    // 번호
    private String companyId;       // 회사ID
    private String deviceCode;        // 위치
    private LocalDateTime eventTime;          // 발생 시간
    private String eventType;        // 임계값, another event
    private String eventSubType;        // co2, ammonia . . .
    private String message;        // 이벤트 메세지
    private Double data;        // 센서값

    // Entity to DTO
    public EventLogDto createEventLogDto(EventLog eventLog) {
        return EventLogDto.builder()
                .id(eventLog.getId())
                .companyId(eventLog.getCompanyId())
                .deviceCode(eventLog.getDeviceCode())
                .eventTime(eventLog.getEventTime())
                .eventType(eventLog.getEventType())
                .eventSubType(eventLog.getEventSubtype())
                .message(eventLog.getMessage())
                .data(eventLog.getData())

                .build();
    }
}
