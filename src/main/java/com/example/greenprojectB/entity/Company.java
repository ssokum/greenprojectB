package com.example.greenprojectB.entity;

import com.example.greenprojectB.constant.Role;
import com.example.greenprojectB.dto.CompanyDto;
import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Entity
@Table(name = "company")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "company_idx")
  private Long company_idx;

  @Column(length = 20, nullable = false)
  private int company_id;

  @NotNull
  private String password;

  @Column(length = 255, nullable = false)
  private String company_name;

  @Column(length = 20, nullable = false)
  private String business_number;

  @Column(length = 20, nullable = false)
  private String ceo_name;

  @Column(length = 255, nullable = false)
  private String address;

  @Column(unique = true, length = 50)
  private String company_email;

  @Column(length = 50, nullable = false)
  private String company_homepage;

  @Column(unique = true, length = 50)
  private String phone_number;

  @Column(unique = true, length = 50)
  private String fax_number;

  @Column(name = "company_content")
  private String company_content;

  @Column(name = "is_deleted")
  private int is_deleted;

  @CreatedDate
  private LocalDateTime created_at;

  @CreatedDate
  private LocalDateTime update_at;

  private Role role;


  // dto to Entity
  public static Company createMember(CompanyDto dto, PasswordEncoder passwordEncoder) {
//    Member member = Member.builder()
//            .build();

//    String password = passwordEncoder.encode(dto.getPassword());

    return Company.builder()
            .company_idx(dto.getCompanyIdx())
            .company_id(dto.getCompanyId())
            .password(passwordEncoder.encode(dto.getPassword()))
            .company_name(dto.getCompany_name())
            .business_number(dto.getBusiness_number())
            .ceo_name(dto.getCeo_name())
            .address(dto.getAddress())
            .company_email(dto.getCompany_email())
            .phone_number(dto.getBusiness_number())
            .fax_number(dto.getFax_number())
            .company_content(dto.getCompany_content())
            .is_deleted(dto.getIs_deleted())
            .created_at(dto.getCreated_at())
            .update_at(dto.getUpdate_at())
            .role(Role.OPERATOR)
            .build();
  }
}
