package com.example.greenprojectB.repository;

import com.example.greenprojectB.entity.EventLog;
import com.example.greenprojectB.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventLogRepository extends JpaRepository<EventLog, Long>, EventLogRepositoryCustom {

    long countByEventType(String eventType);

}
