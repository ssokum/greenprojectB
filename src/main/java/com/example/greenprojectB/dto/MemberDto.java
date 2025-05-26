package com.example.greenprojectB.dto;

import com.example.greenprojectB.constant.Role;
import com.example.greenprojectB.entity.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDto {

  private Long memberIdx;

  @NotEmpty(message = "아이디는 필수입력 입니다.")
  private String memberId;

  @NotEmpty(message = "비밀번호는 필수입력 입니다.")
  @Length(min = 4, max = 20, message = "비밀번호는 4~20 이하로 입력해 주세요")
  private String password;

  @NotEmpty(message = "이름은 필수입력 입니다.")
  private String memberName;

  private LocalDateTime memberBirth;

  @NotEmpty(message = "이메일은 필수입력 입니다.")
  @Email(message = "이메일 형식을 확인하세요")
  private String memberEmail;

  private String gender;

  @NotEmpty(message = "번호는 필수입력 입니다.")
  private String phoneNumber;

  private String memberJob;

  private int isDelete;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  private Role role;

  // Entity to DTO
  public MemberDto createMemberDto(Optional<Member> opMember) {
    return MemberDto.builder()
            .memberIdx(opMember.get().getMemberIdx())
            .memberId(opMember.get().getMemberId())
            .password(opMember.get().getPassword())
            .memberName(opMember.get().getMemberName())
            .memberBirth(opMember.get().getMemberBirth())
            .memberEmail(opMember.get().getMemberEmail())
            .gender(opMember.get().getGender())
            .phoneNumber(opMember.get().getPhoneNumber())
            .memberJob(opMember.get().getMemberJob())
            .isDelete(opMember.get().getIsDeleted())
            .createdAt(opMember.get().getCreatedAt())
            .updatedAt(opMember.get().getUpdateAt())
            .role(opMember.get().getRole())
            .build();
  }

}
