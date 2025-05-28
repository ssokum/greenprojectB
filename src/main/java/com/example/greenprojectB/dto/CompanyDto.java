package com.example.greenprojectB.dto;

import com.example.greenprojectB.constant.Role;
import com.example.greenprojectB.entity.Company;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyDto {

  private Long companyIdx;

  @NotEmpty(message = "아이디는 필수입력 입니다.")
  private String companyId;

  @NotEmpty(message = "비밀번호는 필수입력 입니다.")
  @Length(min = 4, max = 20, message = "비밀번호는 4~20 이하로 입력해 주세요")
  private String password;

  @NotEmpty(message = "이름은 필수입력 입니다.")
  private String company_name;

  @NotEmpty(message = "비즈니스 번호는 필수입력 입니다.")
  private String business_number;


  private String ceo_name;
  private String address;

  @NotEmpty(message = "이메일은 필수입력 입니다.")
  @Email(message = "이메일 형식을 확인하세요")
  private String company_email;

  private String company_homepage;

  @NotEmpty(message = "번호는 필수입력 입니다.")
  private String phone_number;

  @NotEmpty(message = "팩스 번호는 필수입력 입니다.")
  private String fax_number;

  private String company_content;
  private int is_deleted;
  private LocalDateTime created_at;
  private LocalDateTime update_at;
  private Role role;


  // dto to Entity
  public static CompanyDto createMemberDto(Optional<Company> opCompany, PasswordEncoder passwordEncoder) {
//    Member member = Member.builder()
//            .build();

//    String password = passwordEncoder.encode(dto.getPassword());

    return CompanyDto.builder()
/*            .companyIdx(opCompany.get().getCompany_idx())
            .companyId(opCompany.get().getCompany_id())
            .password(passwordEncoder.encode(opCompany.get().getPassword()))
            .company_name(opCompany.get().getCompany_name())
            .business_number(opCompany.get().getBusiness_number())
            .ceo_name(opCompany.get().getCeo_name())
            .address(opCompany.get().getAddress())
            .company_email(opCompany.get().getCompany_email())
            .company_homepage(opCompany.get().getCompany_homepage())
            .phone_number(opCompany.get().getPhone_number() )
            .fax_number(opCompany.get().getFax_number())
            .company_content(opCompany.get().getCompany_content())
            .is_deleted(opCompany.get().getIs_deleted())
            .created_at(opCompany.get().getCreated_at())
            .update_at(opCompany.get().getUpdate_at())
            .role(opCompany.get().getRole())*/
            .build();
  }
}
