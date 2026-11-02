package com.myspring.noticeboard.notice.dto;

import com.myspring.noticeboard.notice.NoticeCategory;
import com.myspring.noticeboard.notice.OrgType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

// 등록/수정 요청에 같이 사용
public record NoticeRequest(

        @NotBlank(message = "제목은 필수입니다.")
        @Size(max = 200, message = "제목은 200자 이하로 입력해주세요.")
        String title,

        @NotBlank(message = "기관명은 필수입니다.")
        @Size(max = 100)
        String organization,

        @NotNull(message = "기관 구분(PUBLIC/PRIVATE)은 필수입니다.")
        OrgType orgType,

        @NotNull(message = "지원 분야는 필수입니다.")
        NoticeCategory category,

        @Size(max = 200)
        String target,

        @Size(max = 50)
        String region,

        @Size(max = 100)
        String supportAmount,

        @NotNull(message = "접수 시작일은 필수입니다.")
        LocalDate startDate,

        @NotNull(message = "마감일은 필수입니다.")
        LocalDate deadline,

        @Size(max = 500)
        String applyUrl,

        String content
) {
}
