package com.example.greenprojectB.service;

import com.example.greenprojectB.entity.Board;
import com.example.greenprojectB.repository.BoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {

  private final BoardRepository boardRepository;

  public List<Board> getAllBoardList() {
    return boardRepository.findAll();
  }

  public Board save(Board board) {
    return boardRepository.save(board);
  }

  public Optional<Board> findById(Long id) {
    return boardRepository.findById(id);
  }

  public void deleteById(Long id) {
    boardRepository.deleteById(id);
  }

  @Transactional
  public Board findByIdAndIncreaseView(Long id) {
    Board board = boardRepository.findById(id).orElseThrow();
    board.setViewCount(board.getViewCount() + 1);
    return boardRepository.save(board);
  }

  public Page<Board> getBoardPage(Pageable pageable) {
    return boardRepository.findAll(pageable);
  }

}
