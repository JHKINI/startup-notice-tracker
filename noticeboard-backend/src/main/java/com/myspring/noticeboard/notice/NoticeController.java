package com.myspring.noticeboard.notice;

import com.myspring.noticeboard.global.PageResponse;
import com.myspring.noticeboard.notice.dto.NoticeDetailResponse;
import com.myspring.noticeboard.notice.dto.NoticeListResponse;
import com.myspring.noticeboard.notice.dto.NoticeRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/notices")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    // 등록: POST /api/notices
    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody NoticeRequest request) {
        Long id = noticeService.create(request);
        return ResponseEntity.created(URI.create("/api/notices/" + id)).build(); // 201 + Location 헤더
    }

    // 목록: GET /api/notices?keyword=&orgType=PRIVATE&category=MENTORING&includeClosed=false&sort=deadline&page=1&size=10
    @GetMapping
    public ResponseEntity<PageResponse<NoticeListResponse>> getList(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) OrgType orgType,
            @RequestParam(required = false) NoticeCategory category,
            @RequestParam(defaultValue = "false") boolean includeClosed,
            @RequestParam(defaultValue = "deadline") String sort,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(
                noticeService.getList(keyword, orgType, category, includeClosed, sort, page, size));
    }

    // 상세: GET /api/notices/{id}
    @GetMapping("/{id}")
    public ResponseEntity<NoticeDetailResponse> getDetail(@PathVariable Long id) {
        return ResponseEntity.ok(noticeService.getDetail(id));
    }

    // 수정: PUT /api/notices/{id}
    @PutMapping("/{id}")
    public ResponseEntity<NoticeDetailResponse> update(@PathVariable Long id,
                                                       @Valid @RequestBody NoticeRequest request) {
        return ResponseEntity.ok(noticeService.update(id, request));
    }

    // 삭제: DELETE /api/notices/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        noticeService.delete(id);
        return ResponseEntity.noContent().build(); // 204
    }
}
