package com.example.greenprojectB.service;

import com.example.greenprojectB.entity.Member;
import com.example.greenprojectB.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminService {

    private final MemberRepository memberRepository;

    public Member getMemberId(String sName){
        return memberRepository.findByMemberId(sName)
                .orElseThrow(() -> new RuntimeException("해당 사용자를 찾을 수 없습니다."));
    }
    public ArrayList<Member> getMembers(){
        return (ArrayList<Member>) memberRepository.findAll();
    }

    @Transactional
    public int setDeleteMember(String memberId){
        return memberRepository.deleteByMemberId(memberId);
    }


}
