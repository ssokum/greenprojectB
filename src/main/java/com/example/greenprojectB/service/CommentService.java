package com.example.greenprojectB.service;

import com.example.greenprojectB.entity.Board;
import com.example.greenprojectB.entity.Comment;
import com.example.greenprojectB.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

  public void delete(Long id) {
    commentRepository.deleteById(id);
  }

  public Optional<Comment> findById(Long id) {
    return commentRepository.findById(id);
  }
}
