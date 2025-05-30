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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    // 검색 포함 공지 리스트 조회 (pag, pageSize, keyword, searchType)
    public Page<Notice> getNoticeMain(int pag, int pageSize, String keyword, String searchType) {
        PageRequest pageRequest = PageRequest.of(pag, pageSize, Sort.by("createDate").descending());


        if (!StringUtils.hasText(keyword)) {
            // 검색어 없으면 전체 조회
            return noticeRepository.findAll(pageRequest);
        }

        // searchType은 항상 "title_content"로 고정됨
        // 제목 or 내용 포함 검색
        return noticeRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(keyword, keyword, pageRequest);
    }

    public void saveNotice(NoticeDto dto) {
        Notice notice = Notice.createNotice(dto);
        noticeRepository.save(notice);
    }

    @Transactional
    public Notice getNoticeByIdx(Long idx) {
        Notice notice = noticeRepository.findById(idx).orElse(null);
        if (notice != null) {
            notice.increaseViewCount();
        }
        return notice;
    }


    public void updateNotice(Long idx, NoticeDto noticeDto) {
        Notice notice = noticeRepository.findById(idx).orElseThrow(() -> new RuntimeException("공지 없음"));
        notice.setTitle(noticeDto.getTitle());
        notice.setContent(noticeDto.getContent());
        // 파일 처리 및 기타 필요한 필드 수정

        noticeRepository.save(notice);
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
            int fileSizes = 0;

            // 업로드된 실제 파일 수 체크
            int uploadCount = 0;

            for (MultipartFile file : fileList) {
                if (!file.isEmpty()) {
                    String oFileName = file.getOriginalFilename();
                    String sFileName = oFileName.substring(0, oFileName.lastIndexOf(".")) +
                            "_" + UUID.randomUUID().toString().substring(0, 4) +
                            oFileName.substring(oFileName.lastIndexOf("."));

                    writeFile(file, sFileName, "upload");

                    oFileNames += oFileName + "/";
                    sFileNames += sFileName + "/";
                    fileSizes += file.getSize();
                    uploadCount++;
                }
            }

            if (uploadCount > 0) {
                oFileNames = oFileNames.substring(0, oFileNames.length() - 1);
                sFileNames = sFileNames.substring(0, sFileNames.length() - 1);


                // DTO에 저장
                noticeDto.setOFileNames(oFileNames);
                noticeDto.setSFileNames(sFileNames);

                System.out.println("================>> 원본파일 : " + oFileNames);
                System.out.println("================>> 저장파일 : " + sFileNames);

                res = 1;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }
}

