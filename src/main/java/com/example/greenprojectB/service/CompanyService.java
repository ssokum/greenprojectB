package com.example.greenprojectB.service;

import com.example.greenprojectB.entity.Company;
import com.example.greenprojectB.entity.History;
import com.example.greenprojectB.repository.CompanyRepository;
import com.example.greenprojectB.repository.HistoryRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompanyService {

  private final CompanyRepository companyRepository;
  private final HistoryRepository historyRepository;

  public List<History> getHistoryList() {
    return historyRepository.findAll();
  }

  public String setFileUpload(MultipartFile sFile) {
    String res = "";

    String oFileName = sFile.getOriginalFilename();
    String sFileName = oFileName.substring(0,oFileName.lastIndexOf(".")) + "_" + UUID.randomUUID().toString().substring(0,4) + oFileName.substring(oFileName.lastIndexOf("."));
    log.info("===========================> 원본파일명 : " + oFileName);
    log.info("===========================> 저장파일명 : " + sFileName);

    try {
      writeFile(sFile, sFileName, "company");
      res = sFileName;
    } catch (Exception e) { e.printStackTrace();}

    return res;
  }

  private void writeFile(MultipartFile file, String sFileName, String urlPath) throws IOException {
    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    String realPath = request.getSession().getServletContext().getRealPath("/"+urlPath+"/");    //첫번째 폴더. 이경우는 webapp이 된다

    FileOutputStream fos = new FileOutputStream(realPath + sFileName);

    if(file.getBytes().length != -1) {
      fos.write(file.getBytes());
    }
    fos.flush();
    fos.close();
  }


  public void setCompanyInput(Company company) {
    companyRepository.save(company);
  }

  public Optional<Company> findByCompanyId(Long company_idx) { return companyRepository.findById(company_idx); }

  public Company getCompanyDto(String id) {
    return companyRepository.findByCompanyId(id).orElseThrow(() -> new RuntimeException("해당 사용자를 찾을 수 없습니다."));
  }
}
