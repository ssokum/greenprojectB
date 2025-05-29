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
    public String noticeWritePost(@ModelAttribute NoticeDto noticeDto, MultipartHttpServletRequest workFile, Model model) {

        noticeDto.setWriter("관리자");

        int res = noticeService.setMultiFileUpload(workFile);

        noticeService.saveNotice(noticeDto);

        if(res != 0) return "redirect:/notice/noticeMain";
        else return "redirect:/message/fileUploadNo";
    }

    @GetMapping("/detail/{idx}")
    public String noticeDetailGet(@PathVariable("idx") Long idx, Model model, HttpServletRequest request) {
        Notice notice = noticeService.getNoticeByIdx(idx);
        if (notice == null) {
            return "redirect:/notice/noticeMain"; // 존재하지 않으면 목록으로
        }
        String realPath = request.getServletContext().getRealPath("/upload/");
        String[] files = new File(realPath).list();
        //System.out.println("filesl : " + files[0]);
        model.addAttribute("fileList", files);
        model.addAttribute("fileCnt", files.length);
        model.addAttribute("notice", notice);
        model.addAttribute("newLine", System.lineSeparator());

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
        // idx에 해당하는 공지사항 데이터 조회
        Notice notice = noticeService.getNoticeByIdx(idx);
        model.addAttribute("notice", notice);
        return "notice/noticeUpdate";  // templates/notice/updateForm.html 뷰를 리턴
    }

    @PostMapping("/noticeUpdate")
    public String noticeUpdatePost(@ModelAttribute NoticeDto noticeDto) {
        Long idx = noticeDto.getIdx(); // NoticeDto에 idx 필드가 있다고 가정
        noticeService.updateNotice(idx, noticeDto);
        return "redirect:/notice/detail/" + idx;
    }

    @GetMapping("/delete")
    public String deleteNoticeGet(@RequestParam Long idx) {
        noticeService.deleteNotice(idx);
        return "redirect:/notice/noticeMain";
    }

}
