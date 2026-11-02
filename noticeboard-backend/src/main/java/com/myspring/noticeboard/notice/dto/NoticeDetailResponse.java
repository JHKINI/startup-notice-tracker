package com.myspring.noticeboard.notice.dto;

import com.myspring.noticeboard.notice.Notice;

import java.time.LocalDate;
import java.time.LocalDateTime;

// 상세용 응답: 모든 정보 포함
public record NoticeDetailResponse(
        Long id,
        String title,
        String organization,
        String orgType,
        String category,
        String target,
        String region,
        String supportAmount,
        LocalDate startDate,
        LocalDate deadline,
        String applyUrl,
        String content,
        int viewCount,
        String status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static NoticeDetailResponse from(Notice notice) {
        return new NoticeDetailResponse(
                notice.getId(),
                notice.getTitle(),
                notice.getOrganization(),
                notice.getOrgType().getLabel(),
                notice.getCategory().getLabel(),
                notice.getTarget(),
                notice.getRegion(),
                notice.getSupportAmount(),
                notice.getStartDate(),
                notice.getDeadline(),
                notice.getApplyUrl(),
                notice.getContent(),
                notice.getViewCount(),
                notice.getStatus().name(),
                notice.getCreatedAt(),
                notice.getUpdatedAt()
        );
    }
}
