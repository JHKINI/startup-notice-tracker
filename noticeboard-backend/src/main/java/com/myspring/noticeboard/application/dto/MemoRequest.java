package com.myspring.noticeboard.application.dto;

import jakarta.validation.constraints.Size;

public record MemoRequest(
        @Size(max = 500, message = "메모는 500자 이하로 입력해주세요.")
        String memo
) {
}
