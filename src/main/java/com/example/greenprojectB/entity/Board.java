package com.example.greenprojectB.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "board")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Board {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "boardIdx")
  private Long boardIdx;

  @Column(nullable = false, length = 200)
  private String title;

  @Lob
  @Column(nullable = false, columnDefinition = "TEXT")
  private String content;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id", nullable = false)
  private Member member;

  @Column(nullable = false)
  private int viewCount;

  @Column(nullable = false)
  private LocalDateTime createdAt;

  @PrePersist
  protected void onCreate() {
    this.createdAt = LocalDateTime.now();
    this.viewCount = 0;
  }

  @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
  @OrderBy("createdAt ASC")
  @Builder.Default
  private List<Comment> comments = new ArrayList<>();

}
