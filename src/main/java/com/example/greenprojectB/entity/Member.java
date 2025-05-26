package com.example.greenprojectB.entity;

import com.example.greenprojectB.constant.Role;
import com.example.greenprojectB.dto.MemberDto;
import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Entity
@Table(name = "member")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "memberIdx")
  private Long memberIdx;

  @Column(name = "memberId", length= 255, nullable = false)
  private String memberId;

  @NotNull
  private String password;

  @Column(length = 20, nullable = false)
  private String memberName;

  @CreatedDate
  private LocalDateTime memberBirth;

  @Column(unique = true, length = 50)
  private String memberEmail;

  @Column(name = "gender")
  private String gender;

  @Column(unique = true, length = 50)
  private String phoneNumber;

  @Column(length = 20)
  private String memberJob;

  @Column(name = "is_deleted")
  private int isDeleted;

  @CreatedDate
  private LocalDateTime createdAt;

  @CreatedDate
  private LocalDateTime updateAt;

  @Enumerated(EnumType.STRING)
  private Role role;

  // dto to Entity
  public static Member createMember(MemberDto dto, PasswordEncoder passwordEncoder) {
//    Member member = Member.builder()
//            .build();

//    String password = passwordEncoder.encode(dto.getPassword());

    return Member.builder()
            .memberIdx(dto.getMemberIdx())
            .memberId(dto.getMemberId())
            .password(passwordEncoder.encode(dto.getPassword()))
            .memberName(dto.getMemberName())
            .memberBirth(dto.getMemberBirth())
            .memberEmail(dto.getMemberEmail())
            .gender(dto.getGender())
            .phoneNumber(dto.getPhoneNumber())
            .memberJob(dto.getMemberJob())
            .isDeleted(dto.getIsDelete())
            .createdAt(LocalDateTime.now())
            .updateAt(LocalDateTime.now())
            .role(Role.USER)
            .build();
  }
}
