package com.example.greenprojectB.entity;

import com.example.greenprojectB.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recruit {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "recruitIdx")
  private Long recruitIdx;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String content;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  @CreatedDate
  @Column(updatable = false)
  private LocalDateTime createdAt;
}
