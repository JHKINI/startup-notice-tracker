package com.myspring.noticeboard.application;

import com.myspring.noticeboard.application.dto.ApplicationCreateRequest;
import com.myspring.noticeboard.application.dto.ApplicationResponse;
import com.myspring.noticeboard.application.dto.MemoRequest;
import com.myspring.noticeboard.application.dto.StatusChangeRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

// REST API (JSON) - Thymeleaf 화면과 별개로 Postman 등에서 테스트용
@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    // 추가: POST /api/applications  {"noticeId": 1}
    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody ApplicationCreateRequest request) {
        Long id = applicationService.create(request.noticeId());
        return ResponseEntity.created(URI.create("/api/applications/" + id)).build();
    }

    // 목록: GET /api/applications?status=PREPARING
    @GetMapping
    public ResponseEntity<List<ApplicationResponse>> getList(@RequestParam(required = false) ApplyStatus status) {
        return ResponseEntity.ok(applicationService.getList(status));
    }

    // 상태 변경: PATCH /api/applications/1/status  {"status": "SUBMITTED"}
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApplicationResponse> changeStatus(@PathVariable Long id,
                                                            @Valid @RequestBody StatusChangeRequest request) {
        return ResponseEntity.ok(applicationService.changeStatus(id, request.status()));
    }

    // 메모 수정: PATCH /api/applications/1/memo  {"memo": "사업계획서 초안 작성"}
    @PatchMapping("/{id}/memo")
    public ResponseEntity<ApplicationResponse> updateMemo(@PathVariable Long id,
                                                          @Valid @RequestBody MemoRequest request) {
        return ResponseEntity.ok(applicationService.updateMemo(id, request.memo()));
    }

    // 삭제: DELETE /api/applications/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        applicationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
