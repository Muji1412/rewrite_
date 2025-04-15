import javax.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@Setter
@ToString(exclude = {"content"})
@NoArgsConstructor
@Entity
@Table(name = "NOTICE")
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // columnDefinition 제거: Hibernate가 Long + IDENTITY 전략에 맞춰 적절한 타입 (보통 BIGINT)으로 자동 매핑하도록 함
    @Column(name = "NOTICE_ID", nullable = false, updatable = false)
    private Long noticeId;

    // ... (나머지 코드는 동일)
    @Column(name = "TITLE", length = 200, nullable = true)
    private String title;
    // ... (나머지 코드는 동일)
    @Column(name = "TITLE3", length = 200, nullable = true)
    private String title3;
    // ... (나머지 코드는 동일)
    @Column(name = "TITLE4", length = 200, nullable = true)
    private String title4;

    @Lob
    // TEXT 타입도 columnDefinition 없이 @Lob 만으로 충분한 경우가 많음 (필요시 유지)
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