package com.myspring.noticeboard.global;

import java.util.Map;

public record ErrorResponse(
        int status,
        String message,
        Map<String, String> errors   // 입력값 검증 실패 시 필드별 메시지
) {
    public static ErrorResponse of(int status, String message) {
        return new ErrorResponse(status, message, Map.of());
    }
}
