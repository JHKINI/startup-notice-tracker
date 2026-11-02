package com.myspring.noticeboard.application.dto;

import com.myspring.noticeboard.application.ApplyStatus;
import jakarta.validation.constraints.NotNull;

public record StatusChangeRequest(
        @NotNull(message = "변경할 상태는 필수입니다.")
        ApplyStatus status
) {
}
