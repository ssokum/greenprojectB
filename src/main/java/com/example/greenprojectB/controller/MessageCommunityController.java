package com.example.greenprojectB.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MessageCommunityController {

	@RequestMapping(value = "/message/{msgFlag}", method = RequestMethod.GET)
	public String getMessage(Model model, @PathVariable String msgFlag,
							 HttpSession session,
							 @RequestParam(name="mid", defaultValue = "", required = false) String mid,
							 @RequestParam(name="idx", defaultValue = "0", required = false) int idx,
							 @RequestParam(name="pag", defaultValue = "1", required = false) int pag,
							 @RequestParam(name="pageSize", defaultValue = "10", required = false) int pageSize,
							 @RequestParam(name="search", defaultValue = "", required = false) String search,
							 @RequestParam(name="searchString", defaultValue = "", required = false) String searchString,
							 @RequestParam(name="part", defaultValue = "전체", required = false) String part,
							 @RequestParam(name="mSw", defaultValue = "1", required = false) String mSw,
							 @RequestParam(name="tempFlag", defaultValue = "", required = false) String tempFlag
	) {

		if(msgFlag.equals("boardInputOk")) {
			model.addAttribute("message", "게시글이 등록되었습니다.");
			model.addAttribute("url", "/board/boardList");
		}
		else if(msgFlag.equals("boardInputNo")) {
			model.addAttribute("message", "게시글 업로드가 실패되었습니다.");
			model.addAttribute("url", "/board/boardInput");
		}
		else if(msgFlag.equals("boardDeleteOk")) {
			model.addAttribute("message", "게시글이 삭제되었습니다.");
			model.addAttribute("url", "/board/boardList");
		}
		else if(msgFlag.equals("boardDeleteNo")) {
			model.addAttribute("message", "게시글 삭제가 실패되었습니다.");
			model.addAttribute("url", "/board/boardList");
		}
		else if(msgFlag.equals("boardUpdateOk")) {
			model.addAttribute("message", "게시글이 수정되었습니다.");
			model.addAttribute("url", "/board/boardList");
		}
		else if(msgFlag.equals("boardUpdateNo")) {
			model.addAttribute("message", "게시글 수정이 실패되었습니다.");
			model.addAttribute("url", "/board/boardList");
		}
		else if(msgFlag.equals("faqInputOk")) {
			model.addAttribute("message", "작성하신 FAQ가 등록되었습니다.");
			model.addAttribute("url", "/faq/faqList");
		}
		else if(msgFlag.equals("faqInputNo")) {
			model.addAttribute("message", "작성하신 FAQ등록이 실패되었습니다");
			model.addAttribute("url", "/faq/faqInput");
		}
		else if(msgFlag.equals("faqDeleteOk")) {
			model.addAttribute("message", "FAQ가 삭제되었습니다");
			model.addAttribute("url", "/faq/faqList");
		}
		else if(msgFlag.equals("faqDeleteNo")) {
			model.addAttribute("message", "FAQ삭제가 실패되었습니다");
			model.addAttribute("url", "/faq/faqList");
		}
		else if(msgFlag.equals("faqUpdateOk")) {
			model.addAttribute("message", "FAQ가 수정되었습니다.");
			model.addAttribute("url", "/faq/faqList");
		}
		else if(msgFlag.equals("faqNotFound")) {
			model.addAttribute("message", "요청하신 FAQ를 찾을 수 없습니다.");
			model.addAttribute("url", "/faq/faqList");
		}
		else if(msgFlag.equals("noAuth")) {
			model.addAttribute("message", "오류");
			model.addAttribute("url", "/recruit/recruitList");
		}
		else if(msgFlag.equals("recruitNotFound")) {
			model.addAttribute("message", "인재채용 게시글을 찾을 수 없습니다.");
			model.addAttribute("url", "/recruit/recruitList");
		}
		else if(msgFlag.equals("onlyEnterprise")) {
			model.addAttribute("message", "기업회원만 작성가능합니다.");
			model.addAttribute("url", "/recruit/recruitList");
		}
		else if(msgFlag.equals("recruitInputOk")) {
			model.addAttribute("message", "인재채용 게시글이 등록되었습니다.");
			model.addAttribute("url", "/recruit/recruitList");
		}
		else if(msgFlag.equals("recruitInputNo")) {
			model.addAttribute("message", "인재채용 게시글 등록에 실패하였습니다.");
			model.addAttribute("url", "/recruit/recruitList");
		}
		else if(msgFlag.equals("recruitDeleteOk")) {
			model.addAttribute("message", "인재채용 게시글이 삭제되었습니다.");
			model.addAttribute("url", "/recruit/recruitList");
		} else if(msgFlag.equals("fileUploadNo")) {
			model.addAttribute("message", "파일 업로드 실패~~ 다시 시도해주세요");
			model.addAttribute("url", "/notice/noticeWrite");
		}

		return "include/message";
	}

}
