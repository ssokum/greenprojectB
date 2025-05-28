package com.example.greenprojectB.controller;

import com.example.greenprojectB.entity.Board;
import com.example.greenprojectB.entity.Comment;
import com.example.greenprojectB.entity.Member;
import com.example.greenprojectB.service.BoardService;
import com.example.greenprojectB.service.CommentService;
import com.example.greenprojectB.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

  private final BoardService boardService;
  private final MemberService memberService;
  private final CommentService commentService;

  @GetMapping("/boardList")
  public String boardListGet(Model model,
                             @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
    Page<Board> boardPage = boardService.getBoardPage(pageable);
    model.addAttribute("boardPage", boardPage);
    return "board/boardList";
  }

  @GetMapping("/boardInput")
  public String boardInputGet(Model model) {
    model.addAttribute("board", new Board());
    return "board/boardInput";
  }

  @PostMapping("/boardInput")
  public String boardInputPost(@ModelAttribute Board board,
                               @AuthenticationPrincipal UserDetails userDetails) {
    String username = userDetails.getUsername(); // 로그인한 사용자의 ID
    Member member = memberService.findByMemberId(username);
    board.setMember(member); // 작성자 설정

    boardService.save(board);
    return "redirect:/board/boardList";
  }

  @GetMapping("/boardDetail/{id}")
  public String boardDetailGet(@PathVariable Long id, Model model, Principal principal) {
    Board board = boardService.findByIdAndIncreaseView(id);
    model.addAttribute("board", board);

    if (principal != null) {
      String loginId = principal.getName();
      Member member = memberService.findByMemberId(loginId); // memberService에서 Member 엔티티 조회
      model.addAttribute("loginId", loginId);
      model.addAttribute("loginRole", member.getRole().name()); // "USER", "ADMIN" 등
    }

    return "board/boardDetail";
  }

  @PostMapping("/boardDelete/{id}")
  public String boardDeletePost(@PathVariable Long id) {
    boardService.deleteById(id);
    return "redirect:/board/boardList";
  }

  @PostMapping("/{id}/comment")
  @ResponseBody
  public Map<String, Object> addComment(@PathVariable Long id,
                                        @RequestBody Map<String, String> body,
                                        Principal principal) {
    String content = body.get("content");

    Member member = memberService.findByMemberId(principal.getName()); // null 여부 확인 필요
    Board board = boardService.findById(id).orElseThrow();

    Comment comment = Comment.builder()
            .board(board)
            .member(member)
            .content(content)
            .build();

    Comment saved = commentService.save(comment);

    Map<String, Object> result = new HashMap<>();
    result.put("success", true);
    result.put("comment", Map.of(
            "memberName", saved.getMember().getMemberName(),
            "content", saved.getContent(),
            "createdAt", saved.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
    ));
    return result;
  }


}
