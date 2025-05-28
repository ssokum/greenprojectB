package com.example.greenprojectB.repository;

import com.example.greenprojectB.entity.Board;
import com.example.greenprojectB.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
  List<Comment> findByBoard(Board board);
}
