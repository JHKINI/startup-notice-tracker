package com.myspring.noticeboard.global;

import org.springframework.data.domain.Page;

import java.util.List;

// Page 객체를 그대로 내보내지 않고 필요한 값만 정리해서 응답
public record PageResponse<T>(
        List<T> content,
        int page,           // 현재 페이지 (1부터 시작)
        int size,
        long totalElements,
        int totalPages,
        boolean hasNext
) {
    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber() + 1,
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.hasNext()
        );
    }
}
