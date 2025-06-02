package com.example.greenprojectB.controller;

import com.example.greenprojectB.dto.NoticeDto;
import com.example.greenprojectB.entity.Notice;
import com.example.greenprojectB.service.NoticeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/notice")
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping("/noticeMain")
    public String noticeMainGet(Model model,
                                @RequestParam(name = "pag", defaultValue = "0", required = false) int pag,
                                @RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize,
                                @RequestParam(name = "keyword", required = false) String keyword) {

        String searchType = "title_content";  // 항상 제목+내용 검색

        Page<Notice> dtos = noticeService.getNoticeMain(pag, pageSize, keyword, searchType);

        int totPage = dtos.getTotalPages();
        int curScrStartNo = (int) dtos.getTotalElements() - (pag * pageSize);
        int blockSize = 3;
        int curBlock = (pag) / blockSize;
        int lastBlock = (totPage - 1) / blockSize;

        model.addAttribute("noticeList", dtos);
        model.addAttribute("pag", pag + 1);
        model.addAttribute("totPage", totPage);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("curScrStartNo", curScrStartNo);
        model.addAttribute("blockSize", blockSize);
        model.addAttribute("curBlock", curBlock);
        model.addAttribute("lastBlock", lastBlock);

        model.addAttribute("keyword", keyword);

        model.addAttribute("newLine", System.lineSeparator());

        return "notice/noticeMain";
    }

    @GetMapping("/noticeWrite")
    public String noticeWriteGet() {
        return "notice/noticeWrite";
    }

    @PostMapping("/noticeWrite")
    public String noticeWritePost(@ModelAttribute NoticeDto noticeDto, MultipartHttpServletRequest workFile) {
        noticeDto.setWriter("관리자");
        noticeService.setMultiFileUpload(workFile, noticeDto);

        int res = noticeService.saveNotice(noticeDto);

        if (res == 1) {
            return "redirect:/message/noticeWriteOk";
        } else {
            return "redirect:/message/noticeWriteNo";
        }
    }

    @GetMapping("/detail/{idx}")
    public String noticeDetailGet(@PathVariable("idx") Long idx, Model model, HttpServletRequest request) {
        Notice notice = noticeService.getNoticeByIdx(idx);
        if (notice == null) {
            return "redirect:/notice/noticeMain"; // 존재하지 않으면 목록으로
        }

        // DB에 저장된 서버 저장용 파일명 가져오기 (예: "file1_1234.jpg/file2_5678.png")
        String sFileNames = notice.getOFileNames();

        List<String> fileList;
        if (sFileNames != null && !sFileNames.trim().isEmpty()) {
            // "/" 로 나누어서 리스트로 변환
            fileList = Arrays.asList(sFileNames.split("/"));
        } else {
            fileList = Collections.emptyList();
        }

        model.addAttribute("fileList", fileList);
        model.addAttribute("fileCnt", fileList.size());
        model.addAttribute("notice", notice);
        model.addAttribute("newLine", System.lineSeparator());

        // 이전/다음 글 처리
        model.addAttribute("prevNotice", noticeService.getPrevNotice(idx));
        model.addAttribute("nextNotice", noticeService.getNextNotice(idx));

        return "notice/noticeDetail";
    }

    @GetMapping("/noticeCheck")
    public String showPasswordCheckPage(@RequestParam("idx") Long idx,
                                        @RequestParam("action") String action,
                                        Model model) {
        model.addAttribute("idx", idx);
        model.addAttribute("action", action);
        return "notice/noticeCheck";
    }

    @PostMapping("/noticeCheck")
    public String handlePasswordCheck(@RequestParam("idx") Long idx,
                                      @RequestParam("action") String action,
                                      @RequestParam("password") String password,
                                      RedirectAttributes redirectAttributes) {

        // 실제 비밀번호 확인 (예: DB에서 가져와 비교)
        Notice notice = noticeService.getNoticeByIdx(idx);
        if (notice == null || !notice.getNoticePassword().equals(password)) {
            redirectAttributes.addAttribute("error", "비밀번호가 일치하지 않습니다.");
            redirectAttributes.addAttribute("idx", idx);
            redirectAttributes.addAttribute("action", action);
            return "redirect:/notice/noticeCheck";
        }

        // 비밀번호 일치 시 분기 처리
        if ("update".equals(action)) {
            return "redirect:/notice/noticeUpdate?idx=" + idx;
        } else if ("delete".equals(action)) {
            return "redirect:/notice/delete?idx=" + idx;
        } else {
            return "redirect:/notice/noticeMain";
        }
    }
    @GetMapping("/noticeUpdate")
    public String noticeUpdateGet(@RequestParam("idx") Long idx, Model model) {
        Notice notice = noticeService.getNoticeByIdx(idx);

        if (notice == null) {
            return "redirect:/notice/noticeMain"; // 존재하지 않으면 목록으로
        }

        // 서버 저장 파일명(sFileNames)과 원본 파일명(oFileNames) 가져오기
        String sFileNames = notice.getSFileNames();
        String oFileNames = notice.getOFileNames();

        List<String> sFileList = (sFileNames != null && !sFileNames.trim().isEmpty())
                ? Arrays.asList(sFileNames.split("/"))
                : Collections.emptyList();

        List<String> oFileList = (oFileNames != null && !oFileNames.trim().isEmpty())
                ? Arrays.asList(oFileNames.split("/"))
                : Collections.emptyList();

        model.addAttribute("notice", notice);
        model.addAttribute("sFileList", sFileList);  // 실제 삭제에 사용
        model.addAttribute("oFileList", oFileList);  // 화면 표시용

        return "/notice/noticeUpdate";  // 템플릿 경로
    }


    @PostMapping("/noticeUpdate")
    public String noticeUpdatePost(@ModelAttribute NoticeDto noticeDto, MultipartHttpServletRequest workFile) {
        Long idx = noticeDto.getIdx();
        noticeDto.setWriter("관리자");

        Notice existingNotice = noticeService.getNoticeByIdx(idx);

        if (noticeDto.getNoticePassword() == null || noticeDto.getNoticePassword().trim().isEmpty()) {
            noticeDto.setNoticePassword(existingNotice.getNoticePassword());
        }

        noticeService.addMultiFileUpload(workFile, noticeDto);

        int res = noticeService.updateNotice(idx, noticeDto);

        if (res > 0) {
            return "redirect:/message/noticeUpdateOk?idx=" + idx;
        } else {
            return "redirect:/message/noticeUpdateNo?idx=" + idx;
        }
    }


    @ResponseBody
    @PostMapping("/fileDeleteCheck")
    public String fileDeleteCheckPost(HttpServletRequest request, Long idx, String file) {
        System.out.println("fileDeleteCheckPost 호출됨, idx=" + idx + ", file=" + file);

        String realPath = request.getServletContext().getRealPath("/upload/");
        System.out.println("realPath = " + realPath);

        if(realPath == null) {
            System.out.println("realPath가 null입니다. 파일 삭제 불가");
            return "0";
        }

        File targetFile = new File(realPath, file); // 안전하게 경로 조합
        System.out.println("삭제 대상 파일 절대경로: " + targetFile.getAbsolutePath());

        boolean deleted = false;
        if (targetFile.exists()) {
            deleted = targetFile.delete();
            System.out.println("파일 삭제 시도 결과: " + deleted);
        } else {
            System.out.println("삭제 대상 파일이 존재하지 않습니다.");
        }

        if (deleted) {
            Notice notice = noticeService.getNoticeByIdx(idx);  // 조회 후

            String sFileNames = notice.getSFileNames();
            String oFileNames = notice.getOFileNames();

            List<String> sList = sFileNames != null ? new ArrayList<>(Arrays.asList(sFileNames.split("/"))) : new ArrayList<>();
            List<String> oList = oFileNames != null ? new ArrayList<>(Arrays.asList(oFileNames.split("/"))) : new ArrayList<>();

            int index = sList.indexOf(file); // 삭제된 파일의 인덱스
            System.out.println("삭제 대상 파일명(file): '" + file + "'");
            System.out.println("sList 내용: " + sList);
            System.out.println("indexOf 결과: " + index);
            if (index != -1) {
                sList.remove(index);
                if (oList.size() > index) {
                    oList.remove(index);
                }
            }

            String updatedSFileNames = String.join("/", sList);
            String updatedOFileNames = String.join("/", oList);

            System.out.println("업데이트된 sFileNames: " + updatedSFileNames);
            System.out.println("업데이트된 oFileNames: " + updatedOFileNames);

            noticeService.updateFiles(idx, updatedSFileNames, updatedOFileNames);

            return "1";
        }

        return "0";
    }


    @ResponseBody
    @PostMapping("/fileSelectDelete")
    public String fileSelectDeletePost(HttpServletRequest request, Long idx, String delItems) {
        if (delItems == null || delItems.isEmpty()) return "0";

        // 마지막 '/' 제거 (예: "file1/file2/" -> "file1/file2")
        if (delItems.endsWith("/")) {
            delItems = delItems.substring(0, delItems.length() - 1);
        }
        String[] fileNames = delItems.split("/");

        String realPath = request.getServletContext().getRealPath("/upload/");

        boolean allDeleted = true;
        for (String fileName : fileNames) {
            File file = new File(realPath + fileName);
            if (file.exists()) {
                boolean deleted = file.delete();
                if (!deleted) allDeleted = false;
            }
        }

        if (allDeleted) {
            // 공지사항 데이터 조회
            Notice notice = noticeService.getNoticeByIdx(idx);
            if (notice == null) return "0";

            // sFileNames 와 oFileNames 둘 다 갱신 필요할 수 있음
            List<String> sFileList = notice.getSFileNames() != null
                    ? new ArrayList<>(Arrays.asList(notice.getSFileNames().split("/")))
                    : new ArrayList<>();

            List<String> oFileList = notice.getOFileNames() != null
                    ? new ArrayList<>(Arrays.asList(notice.getOFileNames().split("/")))
                    : new ArrayList<>();

            // 삭제된 파일명들 제거
            for (String delFile : fileNames) {
                int idxToRemove = sFileList.indexOf(delFile);
                if (idxToRemove != -1) {
                    sFileList.remove(idxToRemove);
                    if (oFileList.size() > idxToRemove) {
                        oFileList.remove(idxToRemove);
                    }
                }
            }

            String updatedSFileNames = String.join("/", sFileList);
            String updatedOFileNames = String.join("/", oFileList);

            // DB에 반영 (새 메서드 필요)
            noticeService.updateFiles(idx, updatedSFileNames, updatedOFileNames);

            return "1";
        }

        return "0";
    }



    @GetMapping("/delete")
    public String deleteNoticeGet(@RequestParam Long idx) {
        noticeService.deleteNotice(idx);
        return "redirect:/notice/noticeMain";
    }

}
