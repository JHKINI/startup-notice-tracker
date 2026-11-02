package com.myspring.noticeboard.notice.dto;

import com.myspring.noticeboard.notice.Notice;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

// 목록용 응답: 카드에 필요한 핵심 정보만 (상세 내용 content는 제외)
public record NoticeListResponse(
        Long id,
        String title,
        String organization,
        String orgType,        // "민간기관·교육기관" 같은 한글 라벨
        String category,
        String target,
        String supportAmount,
        LocalDate deadline,
        long dDay,             // 마감까지 남은 일수 (0 = 오늘마감, 음수 = 마감됨)
        String dDayLabel,      // "D-3", "오늘마감", "마감"
        int viewCount,
        String status
) {
    public static NoticeListResponse from(Notice notice) {
        long dDay = ChronoUnit.DAYS.between(LocalDate.now(), notice.getDeadline());

        return new NoticeListResponse(
                notice.getId(),
                notice.getTitle(),
                notice.getOrganization(),
                notice.getOrgType().getLabel(),
                notice.getCategory().getLabel(),
                notice.getTarget(),
                notice.getSupportAmount(),
                notice.getDeadline(),
                dDay,
                makeDDayLabel(dDay),
                notice.getViewCount(),
                notice.getStatus().name()
        );
    }

    public static String makeDDayLabel(long dDay) {
        if (dDay < 0) {
            return "마감";
        }
        if (dDay == 0) {
            return "오늘마감";
        }
        return "D-" + dDay;
    }
}
