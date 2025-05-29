package com.example.greenprojectB.repository;

import com.example.greenprojectB.entity.EventLog;
import com.example.greenprojectB.entity.QEventLog;
import com.example.greenprojectB.entity.QSensor;
import com.example.greenprojectB.entity.Threshold;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

public class EventLogRepositoryImpl implements EventLogRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public EventLogRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public long countBySensorEvent(String fieldName) {
        QEventLog EventLog = QEventLog.eventLog;

        // 안전한 필드 지정 (화이트리스트 방식)
        PathBuilder<EventLog> entityPath = new PathBuilder<>(EventLog.class, "eventLog");

        return queryFactory
                .select(entityPath.count())
                .from(EventLog)
                .where(entityPath.getNumber(fieldName, Double.class).goe(0.0)) // 예: null이 아닌 값
                .fetchOne();
    }

}
