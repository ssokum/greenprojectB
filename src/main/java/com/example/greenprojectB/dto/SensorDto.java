package com.example.greenprojectB.dto;

import com.example.greenprojectB.entity.Sensor;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SensorDto {

    private int sensorId;
    private int companyId;
    private String deviceCode;
    private LocalDateTime measureDatetime;

    private double indoor;
    private double humidity;
    private double co2;
    private double voc;

    private double pm1_0;
    private double pm2_5;
    private double pm10;

    private double floor1;
    private double floor2;
    private double floor3;

    private double noise;
    private double noTouchTemp;
    private double illuminance;

    private double carbon;
    private double so2;
    private double no2;
    private double ozone;
    private double hcho;
    private double benzene;
    private double ammonia;


    //entity to dto / entity를 dto로 변환
    public  static SensorDto toDto(Optional<Sensor> opSensor){
        return SensorDto.builder()
                .sensorId(opSensor.get().getSensorId())
                .companyId(opSensor.get().getCompanyId())
                .deviceCode(opSensor.get().getDeviceCode())
                .measureDatetime(opSensor.get().getMeasureDatetime())
                .indoor(opSensor.get().getIndoor())
                .humidity(opSensor.get().getHumidity())
                .co2(opSensor.get().getCo2())
                .voc(opSensor.get().getVoc())
                .pm1_0(opSensor.get().getPm1_0())
                .pm2_5(opSensor.get().getPm2_5())
                .pm10(opSensor.get().getPm10())
                .floor1(opSensor.get().getFloor1())
                .floor2(opSensor.get().getFloor2())
                .floor3(opSensor.get().getFloor3())
                .noise(opSensor.get().getNoise())
                .noTouchTemp(opSensor.get().getNoTouchTemp())
                .illuminance(opSensor.get().getIlluminance())
                .carbon(opSensor.get().getCarbon())
                .so2(opSensor.get().getSo2())
                .no2(opSensor.get().getNo2())
                .ozone(opSensor.get().getOzone())
                .hcho(opSensor.get().getHcho())
                .benzene(opSensor.get().getBenzene())
                .ammonia(opSensor.get().getAmmonia())
                .build();
    }

}
