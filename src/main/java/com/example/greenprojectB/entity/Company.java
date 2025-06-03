package com.example.greenprojectB.entity;

import com.example.greenprojectB.constant.Role;
import com.example.greenprojectB.dto.CompanyDto;
import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
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
@DynamicInsert
@EntityListeners(value = {AuditingEntityListener.class})
public class Company {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "company_idx")
  private Long companyIdx;

  @Column(name = "company_id", length = 20, nullable = false, unique = true)
  private String companyId;

  @Column(name = "company_name",length = 255, nullable = false)
  private String companyName;

  @Column(name = "business_number", length = 20, nullable = false)
  private String businessNumber;

  @Column(name = "ceo_name",length = 20, nullable = false)
  private String ceoName;

  @Column(length = 255, nullable = false)
  private String address;

  @Column(name = "company_email",unique = true, length = 50)
  private String companyEmail;

  @Column(name = "company_homepage",length = 50)
  private String companyHomepage;

  @Column(name = "phone_number",unique = true, length = 50)
  private String phoneNumber;

  @Column(name = "fax_number",unique = true, length = 50)
  private String faxNumber;

  @Lob
  @Column(name = "company_content",nullable = false, columnDefinition = "TEXT")
  private String companyContent;

  @Column(name = "is_deleted")
  private int isDeleted;

  @CreatedDate
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  @LastModifiedDate
  @Column(name = "update_at")
  private LocalDateTime updateAt;

  @Column(name = "role")
  private String role;


  // dto to Entity
  public static Company createCompany(CompanyDto dto) {
//    Member member = Member.builder()
//            .build();

//    String password = passwordEncoder.encode(dto.getPassword());

    return Company.builder()
            .companyIdx(dto.getCompanyIdx())
            .companyId(dto.getCompanyId())
            .companyName(dto.getCompanyName())
            .businessNumber(dto.getBusinessNumber())
            .ceoName(dto.getCeoName())
            .address(dto.getAddress())
            .companyEmail(dto.getCompanyEmail())
            .companyHomepage(dto.getCompanyHomepage())
            .phoneNumber(dto.getPhoneNumber())
            .faxNumber(dto.getFaxNumber())
            .companyContent(dto.getCompanyContent())
            .isDeleted(0)
            .createdAt(null)
            .updateAt(null)
            .role("COMPANY")
            .build();
  }
}
