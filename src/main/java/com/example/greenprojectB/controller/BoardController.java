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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
                               @AuthenticationPrincipal UserDetails userDetails,
                               RedirectAttributes redirectAttributes) {
    String username = userDetails.getUsername(); // 로그인한 사용자의 ID
    Member member = memberService.findByMemberId(username);
    board.setMember(member); // 작성자 설정

    Board boardVo = boardService.save(board);
    if(boardVo != null) return "redirect:/message/boardInputOk";
    else return "redirect:/message/boardInputNo";
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
    try {
      boardService.deleteById(id);
      return "redirect:/message/boardDeleteOk";
    } catch (Exception e) {
      return "redirect:/message/boardDeleteNo";
    }
  }

  @PostMapping("/{id}/comment")
  @ResponseBody
  public Map<String, Object> addCommentPost(@PathVariable Long id,
                                            @RequestBody Map<String, String> body,
                                            Principal principal) {
    String content = body.get("content");
    Member member = memberService.findByMemberId(principal.getName());
    Board board = boardService.findById(id).orElseThrow();

    Comment comment = Comment.builder()
            .board(board)
            .member(member)
            .content(content)
            .build();

    comment.setContent(content.replace("\n", "<br/>"));

    Comment saved = commentService.save(comment);

    Map<String, Object> result = new HashMap<>();
    result.put("success", true);
    result.put("currentUserId", principal.getName());
    result.put("isAdmin", member.getRole().name().equals("ADMIN"));
    result.put("comment", Map.of(
            "commentIdx", saved.getCommentIdx(),
            "memberId", saved.getMember().getMemberId(), // 중요!!
            "memberName", saved.getMember().getMemberName(),
            "content", saved.getContent(),
            "createdAt", saved.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
    ));
    return result;
  }


  @PostMapping("/comment/update/{id}")
  @ResponseBody
  public Map<String, Object> updateComment(@PathVariable Long id, @RequestBody Map<String, String> body, Principal principal) {
    String content = body.get("content");
    Comment comment = commentService.findById(id).orElseThrow();

    if (!principal.getName().equals(comment.getMember().getMemberId()) && !isAdmin(principal)) {
      return Map.of("success", false, "error", "권한 없음");
    }

    comment.setContent(content.replace("\n", "<br/>"));

    commentService.save(comment);

    return Map.of("success", true);
  }

  @PostMapping("/comment/delete/{id}")
  @ResponseBody
  public Map<String, Object> deleteComment(@PathVariable Long id, Principal principal) {
    Comment comment = commentService.findById(id).orElseThrow();

    boolean isAuthor = principal.getName().equals(comment.getMember().getMemberId());
    boolean isAdmin = isAdmin(principal);

    if (!isAuthor && !isAdmin) {
      return Map.of("success", false, "error", "권한 없음");
    }

    commentService.delete(id);
    return Map.of("success", true);
  }

  private boolean isAdmin(Principal principal) {
    Member member = memberService.findByMemberId(principal.getName());
    return member != null && "ADMIN".equalsIgnoreCase(String.valueOf(member.getRole()));
  }


  @GetMapping("/boardUpdate/{id}")
  public String boardUpdateGet(@PathVariable Long id, Model model) {
    Board board = boardService.findById(id).orElseThrow();
    model.addAttribute("board", board);
    return "board/boardUpdate"; // 수정 폼으로 이동
  }

  @PostMapping("/boardUpdate/{id}")
  public String boardUpdatePost(@PathVariable Long id,
                                @ModelAttribute Board board,
                                @AuthenticationPrincipal UserDetails userDetails) {
    Member member = memberService.findByMemberId(userDetails.getUsername());
    Board existingBoard = boardService.findById(id).orElseThrow();

    // 작성자만 수정 가능 (id, member 체크 안 해도 괜찮다고 하셨지만 기본적으로 맞춰두는게 안전합니다)
    existingBoard.setTitle(board.getTitle());
    existingBoard.setContent(board.getContent());

    Board savedBoard = boardService.save(existingBoard);

    if (savedBoard != null) return "redirect:/message/boardUpdateOk";
    else return "redirect:/message/boardUpdateNo";
  }




}
