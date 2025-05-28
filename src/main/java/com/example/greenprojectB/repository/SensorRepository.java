package com.example.greenprojectB.repository;

import com.example.greenprojectB.entity.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;

public interface SensorRepository extends JpaRepository<Sensor,Long> {

    Sensor findBySensorId(int sensorId);

    ArrayList<Sensor> findByMeasureDatetimeBetween(LocalDateTime start, LocalDateTime end);

}
