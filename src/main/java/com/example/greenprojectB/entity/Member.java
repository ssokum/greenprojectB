package com.example.greenprojectB.entity;

import com.example.greenprojectB.constant.Role;
import com.example.greenprojectB.dto.MemberDto;
import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

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
  @Column(name = "member_id")
  private Long id;

  @Column(length = 20, nullable = false)
  private String name;

  @Column(unique = true, length = 50)
  private String email;

  @NotNull
  private String password;

  private String address;

  @Enumerated(EnumType.STRING)
  private Role role;

  // dto to Entity
  public static Member createMember(MemberDto dto, PasswordEncoder passwordEncoder) {
//    Member member = Member.builder()
//            .build();

//    String password = passwordEncoder.encode(dto.getPassword());

    return Member.builder()
            .name(dto.getName())
            .email(dto.getEmail())
            .password(passwordEncoder.encode(dto.getPassword()))
            .address(dto.getAddress())
            .role(Role.USER)
            .build();
  }
}
