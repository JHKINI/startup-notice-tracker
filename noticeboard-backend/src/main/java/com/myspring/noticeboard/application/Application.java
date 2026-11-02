package com.myspring.noticeboard.application;

import com.myspring.noticeboard.notice.Notice;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

// 내가 관리하는 지원 건 (공고 1개당 1개)
@Entity
@Table(name = "application",
        uniqueConstraints = @UniqueConstraint(name = "uk_application_notice", columnNames = "notice_id"))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "notice_id", nullable = false)
    private Notice notice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ApplyStatus status;

    @Column(length = 500)
    private String memo;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Application(Notice notice) {
        this.notice = notice;
        this.status = ApplyStatus.INTERESTED; // 처음 추가하면 "관심"
    }

    public void changeStatus(ApplyStatus next) {
        if (!this.status.canChangeTo(next)) {
            throw new IllegalStateException(
                    "'" + this.status.getLabel() + "'에서 '" + next.getLabel() + "'(으)로 변경할 수 없습니다.");
        }
        this.status = next;
    }

    public void updateMemo(String memo) {
        this.memo = memo;
    }
}
