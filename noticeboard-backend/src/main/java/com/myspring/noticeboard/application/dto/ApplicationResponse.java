package com.myspring.noticeboard.application.dto;

import com.myspring.noticeboard.application.Application;
import com.myspring.noticeboard.application.ApplyStatus;
import com.myspring.noticeboard.notice.Notice;
import com.myspring.noticeboard.notice.dto.NoticeListResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public record ApplicationResponse(
        Long id,
        Long noticeId,
        String noticeTitle,
        String organization,
        LocalDate deadline,
        String dDayLabel,
        ApplyStatus status,
        String statusLabel,
        String memo,
        boolean urgent,      // 마감 3일 이내인데 아직 제출 전
        boolean missed,      // 마감이 지났는데 제출 안 함
        LocalDateTime updatedAt
) {
    public static ApplicationResponse from(Application application) {
        Notice notice = application.getNotice();
        long dDay = ChronoUnit.DAYS.between(LocalDate.now(), notice.getDeadline());

        boolean notSubmitted = application.getStatus() == ApplyStatus.INTERESTED
                || application.getStatus() == ApplyStatus.PREPARING;

        boolean urgent = notSubmitted && dDay >= 0 && dDay <= 3;
        boolean missed = notSubmitted && dDay < 0;

        return new ApplicationResponse(
                application.getId(),
                notice.getId(),
                notice.getTitle(),
                notice.getOrganization(),
                notice.getDeadline(),
                NoticeListResponse.makeDDayLabel(dDay),
                application.getStatus(),
                application.getStatus().getLabel(),
                application.getMemo(),
                urgent,
                missed,
                application.getUpdatedAt()
        );
    }
}
