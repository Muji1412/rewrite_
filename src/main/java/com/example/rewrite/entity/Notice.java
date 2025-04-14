package com.example.rewrite.entity; // 패키지 경로 변경 권장 (예: entity 또는 domain)


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString(exclude = {"content"})
@NoArgsConstructor
@Entity
@Table(name = "NOTICE")
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NOTICE_ID", nullable = false, updatable = false, columnDefinition = "bigint unsigned")
    private Long noticeId;

    @Column(name = "TITLE", length = 200, nullable = true)
    private String title;

    @Lob
    @Column(name = "CONTENT", columnDefinition = "TEXT", nullable = true)
    private String content;

    @Column(name = "IMG", length = 255, nullable = true)
    private String img;

    @CreationTimestamp
    @Column(name = "REG_DATE", nullable = true, updatable = false)
    private LocalDateTime regDate;

    public Notice(String title, String content, String img) {
        this.title = title;
        this.content = content;
        this.img = img;
    }
}