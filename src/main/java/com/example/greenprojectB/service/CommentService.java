package com.example.greenprojectB.service;

import com.example.greenprojectB.entity.Board;
import com.example.greenprojectB.entity.Comment;
import com.example.greenprojectB.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

  private final CommentRepository commentRepository;

  public List<Comment> getCommentsByBoard(Board board) {
    return commentRepository.findByBoard(board);
  }

  public Comment save(Comment comment) {
    return commentRepository.save(comment);
  }

  public void deleteById(Long id) {
    commentRepository.deleteById(id);
  }
}
