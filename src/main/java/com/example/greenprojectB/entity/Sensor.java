package com.example.greenprojectB.entity;


import com.example.greenprojectB.dto.SensorDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "sensor")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //오토인크리먼트 설정
    @Column(name = "sensor_id") //센서 번호
    private int sensorId;

    @Column(name = "company_id")
    private int companyId;

    @Column(name = "device_code", length = 20)// 기기 번호
    private String deviceCode;

    @Column(name = "measure_datetime")// 측정날짜
    private LocalDateTime measureDatetime;

    @Column(name = "indoor")//실내온도
    private double indoor;

    @Column(name = "humidity")//상대습도
    private double humidity;

    @Column(name = "co2")//이산화탄소
    private double co2;

    @Column(name = "voc")//유기화합물 VOC
    private double voc;

    @Column(name = "pm1.0")//초미세먼지 PM1.0
    private double pm1_0;

    @Column(name = "PM2.5")//초미세먼지 PM2.5
    private double pm2_5;

    @Column(name = "pm10")//초미세먼지 PM10
    private double pm10;

    @Column(name = "floor_1")//1층 온도
    private double floor1;

    @Column(name = "floor_2")//2층 온도
    private double floor2;

    @Column(name = "floor_3")//3층 온도
    private double floor3;

    @Column(name = "noise")//소음
    private double noise;

    @Column(name = "no_touch_temp")//메인홀 온도(비접촉)
    private double noTouchTemp;

    @Column(name = "illuminance")//조도
    private double illuminance;

    @Column(name = "carbon")//일산화탄소
    private double carbon;

    @Column(name = "so2")//이산화황
    private double so2;

    @Column(name = "no2")//이산화질소
    private double no2;

    @Column(name = "ozone")//오존농도
    private double ozone;

    @Column(name = "hcho")//포름알데히드
    private double hcho;

    @Column(name = "benzene")//벤젠
    private double benzene;

    @Column(name = "ammonia")//암모니아
    private double ammonia;

    // dto to Entity / dto를 엔티티로 변환
    public  static Sensor toEntity(SensorDto dto){
        return Sensor.builder()
                .sensorId(dto.getSensorId())
                .companyId(dto.getCompanyId())
                .deviceCode(dto.getDeviceCode())
                .measureDatetime(dto.getMeasureDatetime())
                .indoor(dto.getIndoor())
                .humidity(dto.getHumidity())
                .co2(dto.getCo2())
                .voc(dto.getVoc())
                .pm1_0(dto.getPm1_0())
                .pm2_5(dto.getPm2_5())
                .pm10(dto.getPm10())
                .floor1(dto.getFloor1())
                .floor2(dto.getFloor2())
                .floor3(dto.getFloor3())
                .noise(dto.getNoise())
                .noTouchTemp(dto.getNoTouchTemp())
                .illuminance(dto.getIlluminance())
                .carbon(dto.getCarbon())
                .so2(dto.getSo2())
                .no2(dto.getNo2())
                .ozone(dto.getOzone())
                .hcho(dto.getHcho())
                .benzene(dto.getBenzene())
                .ammonia(dto.getAmmonia())
                .build();
    }

}
