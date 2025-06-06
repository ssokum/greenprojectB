package com.example.greenprojectB.repository;

import com.example.greenprojectB.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

  Optional<Member> findByMemberId(String memberId);

  int deleteByMemberId(String memberId);
}
