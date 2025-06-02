package com.example.greenprojectB.controller;

import com.example.greenprojectB.Handler.TimeHandler;
import com.example.greenprojectB.dto.SummarySensorDto;
import com.example.greenprojectB.dto.ThresholdDto;
import com.example.greenprojectB.entity.Company;
import com.example.greenprojectB.entity.Sensor;
import com.example.greenprojectB.entity.Threshold;
import com.example.greenprojectB.service.CompanyService;
import com.example.greenprojectB.service.ProfessionalService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
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

  private ArrayList<Sensor> allSenserData = new ArrayList<>();

  private final CompanyService companyService;
  private final ProfessionalService professionalService;
  private final TimeHandler timeHandler;

  // ----- professional ------
  @GetMapping("/dashBoard")
  public String companyProfessionalGet(HttpServletRequest request, Model model) {

    String sName = (String)request.getAttribute("sName"); // 유저 이름
    Company company = companyService.getCompanyDto("1000003"); //임시로 1000003
    List<String> deviceCode = professionalService.getDeviceCode("1000003");
    Collections.sort(deviceCode);

    //Threshold가 없다면
    if(professionalService.getThreshold("1000003").isEmpty()){
      professionalService.createThreshold(deviceCode, company, "1000003");
    }

    model.addAttribute("deviceCode", deviceCode);
    model.addAttribute("sensorValue", sensorValue);
    model.addAttribute("sName", company.getCompanyId());

    return "professional/dashBoard";
  }


  @ResponseBody
  @PostMapping("/updateSensorData")
  public ArrayList<Sensor> updateSensorDataPost(String deviceCode, String type, HttpServletRequest request) {
    //String sName = (String)request.getAttribute("sName"); // 유저 이름
    Company company = companyService.getCompanyDto("1000003"); //임시로 1000003
    int curTime = timeHandler.curTimeGet();

    if(type.equals("update"))
      return professionalService.getChartSensors(company.getCompanyId(), deviceCode, curTime);
    else if(type.equals("create"))
      return professionalService.getChartSensors(company.getCompanyId(), deviceCode, 0, timeHandler.curTimeGet());
    else
      return null;
  }

  @ResponseBody
  @PostMapping("/getThresholdList")
  public ArrayList<Threshold> getThresholdPost(String deviceCode, String companyId) {
    ArrayList<Threshold> thresholds = professionalService.getThreshold(companyId, deviceCode);
    ArrayList<Sensor> allSensorData = professionalService.getChartSensors(companyId, deviceCode, 0, timeHandler.curTimeGet());
    ArrayList<Sensor> sensors = new ArrayList<>();
    double sum[] = new double[sensorValue.length];
    int count = 0;

    for (Sensor sensor : allSensorData) {
      if (deviceCode.equals(sensor.getDeviceCode())) {
        sensors.add(sensor);
      }
    }

    for (int i = 0; i < sensors.size(); i++) {
      Sensor s = sensors.get(i);

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

      for (int j = 0; j < values.size(); j++) {
        sum[j] += values.get(j);
      }

      count++;
    }

    for (Threshold threshold : thresholds) {
      for (int i = 0; i < sensorValue.length; i++) {
        if (threshold.getSensorName().equalsIgnoreCase(sensorValue[i])) {
          double mean = count == 0 ? 0 : sum[i] / count;
          threshold.setMean(mean);
          break;
        }
      }
    }
    return thresholds;
  }

    @ResponseBody
    @PostMapping("/getThreshold")
    public Threshold getThresholdPost(String deviceCode, String companyId, String type) {
        return professionalService.getThreshold(companyId, deviceCode, type);
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
  public String professionalSetupDeviceGet(HttpServletRequest request, Model model) {
    String sName = (String)request.getAttribute("sName"); // 유저 이름
    Company company = companyService.getCompanyDto("1000003"); //임시로 1000003
    List<String> deviceCode = professionalService.getDeviceCode("1000003");
    Collections.sort(deviceCode);

    //Threshold가 없다면
    if(professionalService.getThreshold("1000003").isEmpty()){
      professionalService.createThreshold(deviceCode, company, "1000003");
    }

    model.addAttribute("deviceCode", deviceCode);
    model.addAttribute("sensorValue", sensorValue);
    model.addAttribute("sName", company.getCompanyId());

    return "professional/setup-device";
  }

  @PostMapping("/setup-device")
  public String professionalSetupDeviceGet(ThresholdDto dto,
                                           @RequestParam("idxItems") List<String> idx,
                                           @RequestParam("sensorNameItems") List<String> sensorName,
                                           @RequestParam("meanItems") List<String> mean,
                                           @RequestParam("highNameItems") List<String> high,
                                           @RequestParam("lowNameItems") List<String> low
                                           ) {

      Threshold threshold = null;
      for (int i = 0; i < sensorName.size(); i++) {
          threshold = new Threshold();

          Company company = companyService.findByCompanyId(dto.getCompanyId()).orElse(null);

          threshold.setIdx(Integer.parseInt(idx.get(i)));
          threshold.setCompany(company);
          threshold.setDeviceCode(dto.getDeviceCode());
          threshold.setSensorName(sensorName.get(i));
          threshold.setMean(Double.parseDouble(mean.get(i)));
          threshold.setHigh(Double.parseDouble(high.get(i)));
          threshold.setLow(Double.parseDouble(low.get(i)));
          threshold.setUpdatedBy(dto.getUpdatedBy());

          professionalService.inputThreshold(threshold);
      }

      return "redirect:/professional/setup-device";
  }

  @ResponseBody
  @PostMapping("/getSummarySensors")
  public ArrayList<SummarySensorDto> getSummarySensorsPost(HttpServletRequest request, String deviceCode) {
    String sName = (String)request.getAttribute("sName"); // 유저 이름
    Company company = companyService.getCompanyDto("1000003"); //임시로 1000003
    ArrayList<Sensor> allSensorData = professionalService.getChartSensors(company.getCompanyId(), deviceCode, 0, timeHandler.curTimeGet());
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
