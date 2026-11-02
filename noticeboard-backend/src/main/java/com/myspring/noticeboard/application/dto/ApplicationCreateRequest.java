package com.myspring.noticeboard.application.dto;

import jakarta.validation.constraints.NotNull;

public record ApplicationCreateRequest(
        @NotNull(message = "공고 id는 필수입니다.")
        Long noticeId
) {
}
