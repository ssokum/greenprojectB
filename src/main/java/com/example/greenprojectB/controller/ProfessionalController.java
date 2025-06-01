package com.example.greenprojectB.controller;

import com.example.greenprojectB.dto.SummarySensorDto;
import com.example.greenprojectB.entity.Company;
import com.example.greenprojectB.entity.Sensor;
import com.example.greenprojectB.entity.Threshold;
import com.example.greenprojectB.service.CompanyService;
import com.example.greenprojectB.service.ProfessionalService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/professional")
public class ProfessionalController {
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

  private ArrayList<Sensor> allSensorData = new ArrayList<>();

  private final CompanyService companyService;
  private final ProfessionalService professionalService;

  // ----- professional ------
  @GetMapping("/dashBoard")
  public String companyProfessionalGet(HttpServletRequest request, Model model) {

    String sName = (String)request.getAttribute("sName"); // 유저 이름
    Company company = companyService.getCompanyDto("1000003"); //임시로 1000003
    List<String> deviceCode = professionalService.getDeviceCode("1000003");
    //Threshold가 없다면
    if(professionalService.getThreshold("1000003").isEmpty()){
      professionalService.createThreshold(deviceCode, company, "1000003");
    }

    model.addAttribute("deviceCode", deviceCode);
    model.addAttribute("sensorValue", sensorValue);
    model.addAttribute("sName", company.getCompanyId());

    return "professional/dashBoard";
  }

  public void updateAllSensorData(int curTime) {
    allSensorData.addAll(professionalService.getChartSensors(curTime));
  }

  @ResponseBody
  @PostMapping("/updateSensorData")
  public ArrayList<Sensor> updateSensorDataPost(String deviceCode) {
    ArrayList<Sensor> sensors = new ArrayList<>();

    for (Sensor sensor : allSensorData) {
      if (deviceCode.equals(sensor.getDeviceCode())) {
        sensors.add(sensor);
      }
    }

    return sensors;
  }

  @ResponseBody
  @PostMapping("/getThreshold")
  public ArrayList<Threshold> getThresholdPost(String deviceCode, String companyId) {
    return professionalService.getThreshold(deviceCode, companyId);
  }

  @ResponseBody
  @PostMapping("/getChartByDateRange")
  public ArrayList<Sensor> getChartByDateRangePost(String deviceCode, LocalDateTime begin, LocalDateTime end) {
    return professionalService.getChartSensors(deviceCode, begin, end);
  }

  @GetMapping("/register-device")
  public String professionalRegisterDeviceGet(Threshold threshold, Model model) {
    return "professional/register-device";
  }

  @GetMapping("/setup-device")
  public String professionalSetupDeviceGet(Threshold threshold, Model model) {
    return "professional/setup-device";
  }

  @ResponseBody
  @PostMapping("/getSummarySensors")
  public ArrayList<SummarySensorDto> getSummarySensorsPost(HttpServletRequest request, String deviceCode) {
    String sName = (String)request.getAttribute("sName"); // 유저 이름
    Company company = companyService.getCompanyDto("1000003"); //임시로 1000003
    ArrayList<SummarySensorDto> summarySensors = professionalService.createSummaryList(deviceCode);
    ArrayList<Sensor> sensors = new ArrayList<>();

    for (Sensor sensor : allSensorData) {
      if (deviceCode.equals(sensor.getDeviceCode())) {
        sensors.add(sensor);
      }
    }


    for (Sensor s : sensors) {
      List<Double> values = List.of(
              s.getAmmonia(),
              s.getBenzene(),
              s.getCarbon(),
              s.getCo2(),
              s.getFloor_1(),
              s.getFloor_2(),
              s.getFloor_3(),
              s.getHcho(),
              s.getHumidity(),
              s.getIlluminance(),
              s.getIndoor(),
              s.getNo2(),
              s.getNo_touch_temp(),
              s.getNoise(),
              s.getOzone(),
              s.getPm10(),
              s.getPm1_0(),
              s.getPm2_5(),
              s.getSo2(),
              s.getVoc()
      );

      for (int i = 0; i < values.size(); i++) {
        summarySensors.get(i).getStats().accept(values.get(i));
      }
    }

    return summarySensors;
  }

}
