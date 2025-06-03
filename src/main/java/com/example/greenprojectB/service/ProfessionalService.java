package com.example.greenprojectB.service;

import com.example.greenprojectB.dto.SummarySensorDto;
import com.example.greenprojectB.entity.Company;
import com.example.greenprojectB.entity.EventLog;
import com.example.greenprojectB.entity.Sensor;
import com.example.greenprojectB.dto.Stats;
import com.example.greenprojectB.entity.Threshold;
import com.example.greenprojectB.repository.CompanyRepository;
import com.example.greenprojectB.repository.EventLogRepository;
import com.example.greenprojectB.repository.SensorRepository;
import com.example.greenprojectB.repository.ThresholdRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProfessionalService {
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

    public ArrayList<Sensor> getChartSensors(int curTime) {
        Sensor sensor = sensorRepository.findBySensorId(3935953);
        if (sensor != null) {
            LocalDateTime baseTime = sensor.getMeasureDatetime().plusMinutes(curTime);
            LocalDateTime targetTime = baseTime.plusMinutes(1);

            return sensorRepository.findByMeasureDatetimeBetween(baseTime, targetTime);
        }

        return null;
    }

    public ArrayList<Sensor> getChartSensors(String companyId, String deviceCode, int curTime) {
        Sensor sensor = sensorRepository.findBySensorId(3935953);
        if (sensor != null) {
            LocalDateTime baseTime = sensor.getMeasureDatetime().plusMinutes(curTime);
            LocalDateTime targetTime = baseTime.plusMinutes(2);

            return sensorRepository.findByCompanyIdAndDeviceCodeAndMeasureDatetimeBetween(companyId, deviceCode, baseTime, targetTime);
        }

        return null;
    }

    public ArrayList<Sensor> getChartSensors(String companyId, String deviceCode, int base, int curTime) {
        Sensor sensor = sensorRepository.findBySensorId(3935953);
        if (sensor != null) {
            LocalDateTime baseTime = sensor.getMeasureDatetime().plusMinutes(base);
            LocalDateTime targetTime = baseTime.plusMinutes(curTime);

            return sensorRepository.findByCompanyIdAndDeviceCodeAndMeasureDatetimeBetween(companyId, deviceCode, baseTime, targetTime);
        }

        return null;
    }

    public ArrayList<Sensor> getChartSensors(String deviceCode, LocalDateTime begin, LocalDateTime end) {
        return sensorRepository.findByDeviceCodeAndMeasureDatetimeBetween(deviceCode, begin, end);
    }


    public Company getCompany(String companyId) {
        return companyRepository.findByCompanyId(companyId)
                .orElseThrow(() -> new RuntimeException("해당 사용자를 찾을 수 없습니다."));
    }

    public List<String> getDeviceCode(String companyId) {
        return sensorRepository.findDistinctDevicesByCompanyId(companyId);
    }

    public ArrayList<Threshold> getThreshold(String companyId) {
        return thresholdRepository.findByCompany_CompanyId(companyId);
    }

    public ArrayList<Threshold> getThreshold(String companyId, String deviceCode) {
        return thresholdRepository.findByCompany_CompanyIdAndDeviceCode(companyId, deviceCode);
    }

    public Threshold getThreshold(String companyId, String deviceCode, String type) {
        return thresholdRepository.findByCompany_CompanyIdAndDeviceCodeAndSensorName(companyId, deviceCode, type);
    }

    public void createThreshold(List<String> deviceCode, Company company, String sName) {
        for (String string : deviceCode) {
            for (String s : sensorValue) {
                Threshold threshold = new Threshold();

                threshold.setCompany(company);
                threshold.setDeviceCode(string);
                threshold.setSensorName(s);
                threshold.setUpdatedBy(sName);

                System.out.println("threshold====================================> " + threshold);

                thresholdRepository.save(threshold);
            }
        }

    }

    public EventLog createEventLog(EventLog eventLog) {
        return eventLogRepository.save(eventLog);
    }

    public void inputThreshold(Threshold threshold){
        thresholdRepository.save(threshold);
    }

    private void writeFile(MultipartFile file, String sFileName, String urlPath) throws IOException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String realPath = request.getSession().getServletContext().getRealPath("/" + urlPath + "/");

        FileOutputStream fos = new FileOutputStream(realPath + sFileName);

        if (file.getBytes().length != -1) {
            fos.write(file.getBytes());
        }
        fos.flush();
        fos.close();
    }

    public String fileCsvToMysql(MultipartFile fName) {
        String res = "0";

        String oFileName = fName.getOriginalFilename();
        String sFileName = oFileName.substring(0, oFileName.lastIndexOf(".")) + "_" + UUID.randomUUID().toString().substring(0, 4) + oFileName.substring(oFileName.lastIndexOf("."));
        log.info("==================>> 원본파이명 : " + oFileName);
        log.info("==================>> 저장파이명 : " + sFileName);

        try {
            writeFile(fName, sFileName, "excel");
            res = "1";
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 업로드된 파일을 Line단위로 읽어와서 ','를 기준으로 분리한후 DB에 저장한다.
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String realPath = request.getSession().getServletContext().getRealPath("/excel/" + sFileName);

        try {
            BufferedReader br = new BufferedReader(new FileReader(realPath));
            String line, str = "";
            int cnt = 0;
            br.readLine();
            while ((line = br.readLine()) != null) {
                cnt++;
                //str += cnt + " : " + line + "\n";
                str += cnt + " : " + line + "<br>";

                int k = 1;
                String[] sensors = line.split(",");
                Sensor sensor = new Sensor();
                //user3.setId(Long.parseLong(users[k])); k++;
                sensor.setSensorId(Integer.parseInt(sensors[k]));
                k++;
                sensor.setCompanyId(sensors[k]);
                k++;
                sensor.setDeviceCode(sensors[k]);
                k++;
                sensor.setMeasureDatetime(LocalDateTime.parse(sensors[k]));
                k++;
                sensor.setAmmonia(Double.parseDouble(sensors[k]));
                k++;
                sensor.setBenzene(Double.parseDouble(sensors[k]));
                k++;
                sensor.setCarbon(Double.parseDouble(sensors[k]));
                k++;
                sensor.setCo2(Double.parseDouble(sensors[k]));
                k++;
                sensor.setFloor_1(Double.parseDouble(sensors[k]));
                k++;
                sensor.setFloor_2(Double.parseDouble(sensors[k]));
                k++;
                sensor.setFloor_3(Double.parseDouble(sensors[k]));
                k++;
                sensor.setHcho(Double.parseDouble(sensors[k]));
                k++;
                sensor.setHumidity(Double.parseDouble(sensors[k]));
                k++;
                sensor.setIlluminance(Double.parseDouble(sensors[k]));
                k++;
                sensor.setIndoor(Double.parseDouble(sensors[k]));
                k++;
                sensor.setNo2(Double.parseDouble(sensors[k]));
                k++;
                sensor.setNo_touch_temp(Double.parseDouble(sensors[k]));
                k++;
                sensor.setNoise(Double.parseDouble(sensors[k]));
                k++;
                sensor.setOzone(Double.parseDouble(sensors[k]));
                k++;
                sensor.setPm10(Double.parseDouble(sensors[k]));
                k++;
                sensor.setPm1_0(Double.parseDouble(sensors[k]));
                k++;
                sensor.setPm2_5(Double.parseDouble(sensors[k]));
                k++;
                sensor.setSo2(Double.parseDouble(sensors[k]));
                k++;
                sensor.setVoc(Double.parseDouble(sensors[k]));
                k++;

                // DB에 저장처리
                sensorRepository.save(sensor);
            }
            System.out.println("=======>>str : " + str);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    public ArrayList<SummarySensorDto> createSummaryList(String deviceCode) {
        ArrayList<SummarySensorDto> list = new ArrayList<>();

        for (int i = 0; i < sensorValue.length; i++) {
            SummarySensorDto dto = new SummarySensorDto();

            dto.setSensorName(sensorValue[i]);
            dto.setDeviceCode(deviceCode);
            dto.setStats(new Stats());
            dto.setEventCnt(0);

            list.add(dto);
        }

        return list;
    }
}
