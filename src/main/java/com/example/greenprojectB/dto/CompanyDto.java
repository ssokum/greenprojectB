package com.example.greenprojectB.dto;

import com.example.greenprojectB.constant.Role;
import com.example.greenprojectB.entity.Company;
import jakarta.persistence.Transient;
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
  private String companyName;

  @NotEmpty(message = "비즈니스 번호는 필수입력 입니다.")
  private String businessNumber;


  private String ceoName;
  private String address;

  @NotEmpty(message = "이메일은 필수입력 입니다.")
  @Email(message = "이메일 형식을 확인하세요")
  private String companyEmail;

  private String companyHomepage;

  @NotEmpty(message = "번호는 필수입력 입니다.")
  private String phoneNumber;

  @NotEmpty(message = "팩스 번호는 필수입력 입니다.")
  private String faxNumber;

  private String companyContent;
  private int isDeleted;
  private LocalDateTime createdAt;
  private LocalDateTime updateAt;
  private Role role;

  // Entity to DTO
  public static CompanyDto createMemberDto(Optional<Company> opCompany ) {
//    Member member = Member.builder()
//            .build();

//    String password = passwordEncoder.encode(dto.getPassword());

    return CompanyDto.builder()
            .companyIdx(opCompany.get().getCompanyIdx())
            .companyId(opCompany.get().getCompanyId())
            .password(opCompany.get().getPassword())
            .companyName(opCompany.get().getCompanyName())
            .businessNumber(opCompany.get().getBusinessNumber())
            .ceoName(opCompany.get().getCeoName())
            .address(opCompany.get().getAddress())
            .companyEmail(opCompany.get().getCompanyEmail())
            .companyHomepage(opCompany.get().getCompanyHomepage())
            .phoneNumber(opCompany.get().getPhoneNumber() )
            .faxNumber(opCompany.get().getFaxNumber())
            .companyContent(opCompany.get().getCompanyContent())
            .isDeleted(opCompany.get().getIsDeleted())
            .createdAt(opCompany.get().getCreatedAt())
            .updateAt(opCompany.get().getUpdateAt())
            .role(opCompany.get().getRole())
            .build();
  }
}
