package com.example.greenprojectB.repository;

import com.example.greenprojectB.entity.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public interface SensorRepository extends JpaRepository<Sensor,Long> {

    Sensor findBySensorId(int sensorId);

    @Query("SELECT DISTINCT s.deviceCode FROM Sensor s WHERE s.companyId = :companyId")
    List<String> findDistinctDevicesByCompanyId(@Param("companyId") String companyId);


    ArrayList<Sensor> findByMeasureDatetimeBetween(LocalDateTime start, LocalDateTime end);
    ArrayList<Sensor> findByDeviceCodeAndMeasureDatetimeBetween(String deviceCode, LocalDateTime start, LocalDateTime end);


}
