package com.example.greenprojectB.controller;

import com.example.greenprojectB.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

@RequiredArgsConstructor
@Controller
public class HomeController {
  private final AdminService adminService;

  // ckeditor에서의 파일 업로드 처리
  @PostMapping("/ckeditor/imageUploadCompany")
  @ResponseBody
  public void imageUploadPost(@RequestParam("upload") MultipartFile upload,
                              @RequestParam("CKEditorFuncNum") String callback,
                              HttpServletRequest request,
                              HttpServletResponse response) throws IOException {

    response.setCharacterEncoding("utf-8");
    response.setContentType("text/html;charset=utf-8");

    // 저장 경로 (실제 경로에 맞게 조정)
    String realPathPath = request.getServletContext().getRealPath("/company/");
    File folder = new File(realPathPath);
    if (!folder.exists()) folder.mkdirs();

    // 파일 저장
    String originalName = upload.getOriginalFilename();
    String newName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "_" + originalName;
    File file = new File(realPathPath, newName);
    upload.transferTo(file);

    String fileUrl = request.getContextPath() + "/company/" + newName;

    // 콜백 응답
    PrintWriter out = response.getWriter();
    out.println("<script type='text/javascript'>");
    out.println("window.parent.CKEDITOR.tools.callFunction(" + callback + ", '" + fileUrl + "', '이미지 업로드 완료');");
    out.println("</script>");
    out.flush();
  }


  // ckeditor에서의 파일 업로드 처리
  @PostMapping("/ckeditor/imageUploadBoard")
  @ResponseBody
  public void imageUploadBoardPost(@RequestParam("upload") MultipartFile upload,
                                   @RequestParam("CKEditorFuncNum") String callback,
                                   HttpServletRequest request,
                                   HttpServletResponse response) throws IOException {

    response.setCharacterEncoding("utf-8");
    response.setContentType("text/html;charset=utf-8");

    // 저장 경로 (실제 경로에 맞게 조정)
    String realPathPath = request.getServletContext().getRealPath("/board/");
    File folder = new File(realPathPath);
    if (!folder.exists()) folder.mkdirs();

    // 파일 저장
    String originalName = upload.getOriginalFilename();
    String newName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "_" + originalName;
    File file = new File(realPathPath, newName);
    upload.transferTo(file);

    String fileUrl = request.getContextPath() + "/board/" + newName;

    // 콜백 응답
    PrintWriter out = response.getWriter();
    out.println("<script type='text/javascript'>");
    out.println("window.parent.CKEDITOR.tools.callFunction(" + callback + ", '" + fileUrl + "', '이미지 업로드 완료');");
    out.println("</script>");
    out.flush();
  }

  // ckeditor에서의 파일 업로드 처리
  @PostMapping("/ckeditor/imageUploadFaq")
  @ResponseBody
  public void imageUploadFaqPost(@RequestParam("upload") MultipartFile upload,
                                 @RequestParam("CKEditorFuncNum") String callback,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws IOException {

    response.setCharacterEncoding("utf-8");
    response.setContentType("text/html;charset=utf-8");

    // 저장 경로 (실제 경로에 맞게 조정)
    String realPathPath = request.getServletContext().getRealPath("/faqImage/");
    File folder = new File(realPathPath);
    if (!folder.exists()) folder.mkdirs();

    // 파일 저장
    String originalName = upload.getOriginalFilename();
    String newName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "_" + originalName;
    File file = new File(realPathPath, newName);
    upload.transferTo(file);

    String fileUrl = request.getContextPath() + "/faqImage/" + newName;

    // 콜백 응답
    PrintWriter out = response.getWriter();
    out.println("<script type='text/javascript'>");
    out.println("window.parent.CKEDITOR.tools.callFunction(" + callback + ", '" + fileUrl + "', '이미지 업로드 완료');");
    out.println("</script>");
    out.flush();
  }

  // ckeditor에서의 파일 업로드 처리
  @PostMapping("/ckeditor/imageUploadEquipment")
  @ResponseBody
  public void imageUploadEquipmentPost(@RequestParam("upload") MultipartFile upload,
                                       @RequestParam("CKEditorFuncNum") String callback,
                                       HttpServletRequest request,
                                       HttpServletResponse response) throws IOException {

    response.setCharacterEncoding("utf-8");
    response.setContentType("text/html;charset=utf-8");

    // 저장 경로 (실제 경로에 맞게 조정)
    String realPathPath = request.getServletContext().getRealPath("/equipmentImage/");
    File folder = new File(realPathPath);
    if (!folder.exists()) folder.mkdirs();

    // 파일 저장
    String originalName = upload.getOriginalFilename();
    String newName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "_" + originalName;
    File file = new File(realPathPath, newName);
    upload.transferTo(file);

    String fileUrl = request.getContextPath() + "/equipmentImage/" + newName;

    // 콜백 응답
    PrintWriter out = response.getWriter();
    out.println("<script type='text/javascript'>");
    out.println("window.parent.CKEDITOR.tools.callFunction(" + callback + ", '" + fileUrl + "', '이미지 업로드 완료');");
    out.println("</script>");
    out.flush();
  }

}
