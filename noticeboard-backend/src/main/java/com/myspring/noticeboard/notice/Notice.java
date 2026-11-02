package com.myspring.noticeboard.notice;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "notice")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA용 기본 생성자 (외부에서 new 금지)
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;              // 공고 제목

    @Column(nullable = false, length = 100)
    private String organization;       // 주관 기관명

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrgType orgType;           // 공공 / 민간

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private NoticeCategory category;   // 지원 분야

    @Column(length = 200)
    private String target;             // 지원 대상 (예: 대학생, 예비창업자)

    @Column(length = 50)
    private String region;             // 지역 (예: 전국, 서울)

    @Column(length = 100)
    private String supportAmount;      // 지원 규모 (예: 최대 1,000만원)

    @Column(nullable = false)
    private LocalDate startDate;       // 접수 시작일

    @Column(nullable = false)
    private LocalDate deadline;        // 마감일

    @Column(length = 500)
    private String applyUrl;           // 신청 링크

    @Column(columnDefinition = "TEXT")
    private String content;            // 상세 내용

    @Column(nullable = false)
    private int viewCount;             // 조회수

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private NoticeStatus status;       // OPEN / CLOSED

    @Column(nullable = false)
    private boolean deleted;           // 소프트 삭제 여부

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Builder
    public Notice(String title, String organization, OrgType orgType, NoticeCategory category,
                  String target, String region, String supportAmount,
                  LocalDate startDate, LocalDate deadline, String applyUrl, String content) {
        this.title = title;
        this.organization = organization;
        this.orgType = orgType;
        this.category = category;
        this.target = target;
        this.region = region;
        this.supportAmount = supportAmount;
        this.startDate = startDate;
        this.deadline = deadline;
        this.applyUrl = applyUrl;
        this.content = content;
        this.viewCount = 0;
        this.deleted = false;
        this.status = calculateStatus(deadline);
    }

    // 수정 (setter 대신 의미 있는 메서드로 변경)
    public void update(String title, String organization, OrgType orgType, NoticeCategory category,
                       String target, String region, String supportAmount,
                       LocalDate startDate, LocalDate deadline, String applyUrl, String content) {
        this.title = title;
        this.organization = organization;
        this.orgType = orgType;
        this.category = category;
        this.target = target;
        this.region = region;
        this.supportAmount = supportAmount;
        this.startDate = startDate;
        this.deadline = deadline;
        this.applyUrl = applyUrl;
        this.content = content;
        this.status = calculateStatus(deadline); // 마감일이 바뀌면 상태도 다시 계산
    }

    public void increaseViewCount() {
        this.viewCount++;
    }

    // 실제로 지우지 않고 deleted 표시만 함
    public void delete() {
        this.deleted = true;
    }

    public void close() {
        this.status = NoticeStatus.CLOSED;
    }

    private NoticeStatus calculateStatus(LocalDate deadline) {
        if (deadline.isBefore(LocalDate.now())) {
            return NoticeStatus.CLOSED;
        }
        return NoticeStatus.OPEN;
    }
}
