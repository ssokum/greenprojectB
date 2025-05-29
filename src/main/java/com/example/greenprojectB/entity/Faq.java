package com.example.greenprojectB.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "faq")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Faq {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "faqIdx")
  private Long faqIdx;

  @Column(nullable = false, length = 200)
  private String title;

  @Lob
  @Column(columnDefinition = "LONGTEXT")
  private String content;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "mid", nullable = false)
  private Member member;


}
