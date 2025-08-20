package com.linkrap.BE.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "scrap_views")
public class ScrapView {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer scrapViewId;

    // 어떤 스크랩을 봤는지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scrap_id")
    private Scrap scrapId;

    // 누가 봤는지(소유자만 기록할 것이므로 소유자 id와 같을 때만 insert)
    @Column(name = "user_id")
    private Integer userId;
}
