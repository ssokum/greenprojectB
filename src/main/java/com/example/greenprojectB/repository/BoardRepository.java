package com.example.greenprojectB.repository;

import com.example.greenprojectB.entity.Board;
import com.example.greenprojectB.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
