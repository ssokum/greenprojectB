package com.example.greenprojectB.service;

import com.example.greenprojectB.dto.NoticeDto;
import com.example.greenprojectB.entity.Notice;
import com.example.greenprojectB.repository.NoticeRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    // 검색 포함 공지 리스트 조회 (pag, pageSize, keyword, searchType)
    public Page<Notice> getNoticeMain(int pag, int pageSize, String keyword, String searchType) {
        PageRequest pageRequest = PageRequest.of(pag, pageSize, Sort.by(Sort.Order.desc("updateDate"), Sort.Order.desc("createDate")));


        if (!StringUtils.hasText(keyword)) {
            // 검색어 없으면 전체 조회
            return noticeRepository.findAll(pageRequest);
        }

        // searchType은 항상 "title_content"로 고정됨
        // 제목 or 내용 포함 검색
        return noticeRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(keyword, keyword, pageRequest);
    }

    public int saveNotice(NoticeDto dto) {
        try {
            Notice notice = Notice.createNotice(dto);
            noticeRepository.save(notice);
            return 1; // 저장 성공
        } catch (Exception e) {
            e.printStackTrace(); // 로그 확인용
            return 0; // 저장 실패
        }
    }

    @Transactional
    public Notice getNoticeByIdx(Long idx) {
        Notice notice = noticeRepository.findById(idx).orElse(null);
        if (notice != null) {
            notice.increaseViewCount();
        }
        return notice;
    }


    @Transactional
    public int updateNotice(Long idx, NoticeDto noticeDto) {
        try {
            Notice notice = noticeRepository.findById(idx)
                    .orElseThrow(() -> new RuntimeException("공지 없음"));

            notice.setTitle(noticeDto.getTitle());
            notice.setContent(noticeDto.getContent());
            notice.setNoticePassword(noticeDto.getNoticePassword());

            // 새 첨부파일 처리
            if (noticeDto.getOFileNames() != null && !noticeDto.getOFileNames().isEmpty()) {
                notice.setOFileNames(noticeDto.getOFileNames());
                notice.setSFileNames(noticeDto.getSFileNames());
            }

            noticeRepository.save(notice);

            return 1; // 성공
        } catch (Exception e) {
            e.printStackTrace(); // 로그 남기기 (실무에서는 로깅 권장)
            return 0; // 실패
        }
    }


    public void deleteNotice(Long idx) {
        noticeRepository.deleteById(idx);
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

    public int setMultiFileUpload(MultipartHttpServletRequest workFile, NoticeDto noticeDto) {
        int res = 0;

        try {
            List<MultipartFile> fileList = workFile.getFiles("mFile");

            String oFileNames = "";
            String sFileNames = "";

            int uploadCount = 0;

            for (MultipartFile file : fileList) {
                if (!file.isEmpty()) {
                    String oFileName = file.getOriginalFilename();
                    String sFileName = oFileName.substring(0, oFileName.lastIndexOf(".")) +
                            "_" + UUID.randomUUID().toString().substring(0, 4) +
                            oFileName.substring(oFileName.lastIndexOf("."));

                    writeFile(file, sFileName, "upload");

                    oFileNames += (oFileNames.isEmpty() ? "" : "/") + oFileName;
                    sFileNames += (sFileNames.isEmpty() ? "" : "/") + sFileName;

                    uploadCount++;
                }
            }

            if (uploadCount > 0) {
                noticeDto.setOFileNames(oFileNames);
                noticeDto.setSFileNames(sFileNames);
                res = 1;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    public int addMultiFileUpload(MultipartHttpServletRequest workFile, NoticeDto noticeDto) {
        int res = 0;

        try {
            List<MultipartFile> fileList = workFile.getFiles("newFile");

            // 🔸 기존 공지사항 가져오기 (DB에서)
            Optional<Notice> optional = noticeRepository.findById(noticeDto.getIdx());
            String oFileNames = "";
            String sFileNames = "";

            if (optional.isPresent()) {
                Notice original = optional.get();
                oFileNames = original.getOFileNames() != null ? original.getOFileNames() : "";
                sFileNames = original.getSFileNames() != null ? original.getSFileNames() : "";
            }

            int uploadCount = 0;

            for (MultipartFile file : fileList) {
                if (!file.isEmpty()) {
                    String oFileName = file.getOriginalFilename();
                    String sFileName = oFileName.substring(0, oFileName.lastIndexOf(".")) +
                            "_" + UUID.randomUUID().toString().substring(0, 4) +
                            oFileName.substring(oFileName.lastIndexOf("."));

                    writeFile(file, sFileName, "upload");

                    oFileNames += (oFileNames.isEmpty() ? "" : "/") + oFileName;
                    sFileNames += (sFileNames.isEmpty() ? "" : "/") + sFileName;

                    uploadCount++;
                }
            }

            if (uploadCount > 0) {
                noticeDto.setOFileNames(oFileNames);
                noticeDto.setSFileNames(sFileNames);
                res = 1;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }


    @Transactional
    public void updateFiles(Long idx, String updatedSFileNames, String updatedOFileNames) {
        Notice notice = noticeRepository.findById(idx)
                .orElseThrow(() -> new RuntimeException("공지사항을 찾을 수 없습니다."));
        notice.setSFileNames(updatedSFileNames);
        notice.setOFileNames(updatedOFileNames);
        noticeRepository.save(notice);
    }

    @Transactional
    public Notice getPrevNotice(Long idx) {
        return noticeRepository.findTopByIdxLessThanOrderByIdxDesc(idx).orElse(null);
    }

    @Transactional
    public Notice getNextNotice(Long idx) {
        return noticeRepository.findTopByIdxGreaterThanOrderByIdxAsc(idx).orElse(null);
    }

}

