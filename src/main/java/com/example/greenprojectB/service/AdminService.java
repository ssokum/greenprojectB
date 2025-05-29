package com.example.greenprojectB.service;

import com.example.greenprojectB.dto.SummarySensorDto;
import com.example.greenprojectB.entity.Company;
import com.example.greenprojectB.entity.Sensor;
import com.example.greenprojectB.entity.Threshold;
import com.example.greenprojectB.repository.CompanyRepository;
import com.example.greenprojectB.repository.EventLogRepository;
import com.example.greenprojectB.repository.SensorRepository;
import com.example.greenprojectB.repository.ThresholdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    String[] sensorValue = {
            "ammonia",
            "benzene",
            "carbon",
            "co2",
            "floor_1",
            "floor_2",
            "floor_3",
            "hcho",
            "humidity",
            "illuminance",
            "indoor",
            "no2",
            "no_touch_temp",
            "noise",
            "ozone",
            "pm10",
            "pm1_0",
            "pm2_5",
            "so2",
            "voc"
    };

    private final SensorRepository sensorRepository;
    private final EventLogRepository eventLogRepository;
    private final CompanyRepository companyRepository;
    private final ThresholdRepository thresholdRepository;


    public ArrayList<Sensor> getChartSensors(int minute){
        Sensor sensor = sensorRepository.findBySensorId(3935953);
        if( sensor != null ){
            LocalDateTime baseTime = sensor.getMeasureDatetime();
            LocalDateTime targetTime = baseTime.plusMinutes(minute).plusMinutes(5);
            return sensorRepository.findByMeasureDatetimeBetween(baseTime, targetTime);
        }

        return null;
    }

    public ArrayList<SummarySensorDto> getSummarySensors(){
        ArrayList<SummarySensorDto> list = new ArrayList<>();

        for(int i = 0;  i< sensorValue.length; i++) {
            SummarySensorDto summarySensorDto = new SummarySensorDto();
            summarySensorDto.setSensorName(sensorValue[i]);
            summarySensorDto.setMin(0);
            summarySensorDto.setMean(0);
            summarySensorDto.setMax(0);
            summarySensorDto.setEventCnt(eventLogRepository.countByEventType(sensorValue[i]));

            list.add(summarySensorDto);
        }

        return list;
    }

    public Company getCompany(String companyId){
        return companyRepository.findByCompanyId(companyId)
                .orElseThrow(() -> new RuntimeException("해당 사용자를 찾을 수 없습니다."));
    }

    public List<String> getDeviceCode(String companyId){
        return sensorRepository.findDistinctDevicesByCompanyId(companyId);
    }

    public ArrayList<Threshold> getThreshold(String companyId, String deviceCode){
        //return thresholdRepository.findByCompany_CompanyIdAndDeviceCode(companyId, deviceCode);
        return null;
    }
}
