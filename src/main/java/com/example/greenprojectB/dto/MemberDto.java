package com.example.greenprojectB.dto;

import com.example.greenprojectB.constant.Role;
import com.example.greenprojectB.entity.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.Optional;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDto {

  private Long id;

  @NotEmpty(message = "이름은 필수입력 입니다.")
  private String name;

  @NotEmpty(message = "이메일은 필수입력 입니다.")
  @Email(message = "이메일 형식을 확인하세요")
  private String email;

  @NotEmpty(message = "비밀번호는 필수입력 입니다.")
  @Length(min = 4, max = 20, message = "비밀번호는 4~20 이하로 입력해 주세요")
  private String password;

  private String address;

  private Role role;

  // Entity to DTO
  public MemberDto createMemberDto(Optional<Member> opMember) {
    return MemberDto.builder()
            .id(opMember.get().getId())
            .name(opMember.get().getName())
            .email(opMember.get().getEmail())
            .password(opMember.get().getPassword())
            .address(opMember.get().getAddress())
            .role(opMember.get().getRole())
            .build();
  }

}
