package com.myspring.noticeboard.notice;

import com.myspring.noticeboard.global.NotFoundException;
import com.myspring.noticeboard.global.PageResponse;
import com.myspring.noticeboard.notice.dto.NoticeDetailResponse;
import com.myspring.noticeboard.notice.dto.NoticeListResponse;
import com.myspring.noticeboard.notice.dto.NoticeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 기본은 읽기 전용, 쓰기 메서드에만 @Transactional 따로 붙임
public class NoticeService {

    private static final int MAX_PAGE_SIZE = 50;

    private final NoticeRepository noticeRepository;

    // ===== Create =====
    @Transactional
    public Long create(NoticeRequest request) {
        validateDates(request);

        Notice notice = Notice.builder()
                .title(request.title())
                .organization(request.organization())
                .orgType(request.orgType())
                .category(request.category())
                .target(request.target())
                .region(request.region())
                .supportAmount(request.supportAmount())
                .startDate(request.startDate())
                .deadline(request.deadline())
                .applyUrl(request.applyUrl())
                .content(request.content())
                .build();

        Notice saved = noticeRepository.save(notice);
        return saved.getId();
    }

    // ===== Read (목록) =====
    public PageResponse<NoticeListResponse> getList(String keyword, OrgType orgType, NoticeCategory category,
                                                    boolean includeClosed, String sort, int page, int size) {
        // 빈 문자열 검색어는 검색 안 한 것으로 처리
        String searchKeyword = (keyword == null || keyword.isBlank()) ? null : keyword.trim();

        // 기본: 진행중(OPEN) 공고만 보여줌
        NoticeStatus status = includeClosed ? null : NoticeStatus.OPEN;

        int safePage = Math.max(page, 1) - 1;                      // 1페이지 → 0
        int safeSize = Math.min(Math.max(size, 1), MAX_PAGE_SIZE); // 1 ~ 50
        Pageable pageable = PageRequest.of(safePage, safeSize, makeSort(sort));

        Page<NoticeListResponse> result = noticeRepository
                .search(searchKeyword, orgType, category, status, pageable)
                .map(NoticeListResponse::from);

        return PageResponse.from(result);
    }

    // ===== Read (상세) =====
    @Transactional // 조회수 증가 때문에 쓰기 트랜잭션
    public NoticeDetailResponse getDetail(Long id) {
        Notice notice = findNotice(id);
        notice.increaseViewCount(); // 변경 감지로 자동 UPDATE
        return NoticeDetailResponse.from(notice);
    }

    // 수정 화면에 기존 값을 채우기 위한 조회 (조회수 증가 X)
    public NoticeRequest getEditRequest(Long id) {
        Notice notice = findNotice(id);
        return new NoticeRequest(
                notice.getTitle(),
                notice.getOrganization(),
                notice.getOrgType(),
                notice.getCategory(),
                notice.getTarget(),
                notice.getRegion(),
                notice.getSupportAmount(),
                notice.getStartDate(),
                notice.getDeadline(),
                notice.getApplyUrl(),
                notice.getContent()
        );
    }

    // ===== Update =====
    @Transactional
    public NoticeDetailResponse update(Long id, NoticeRequest request) {
        validateDates(request);

        Notice notice = findNotice(id);
        notice.update(
                request.title(),
                request.organization(),
                request.orgType(),
                request.category(),
                request.target(),
                request.region(),
                request.supportAmount(),
                request.startDate(),
                request.deadline(),
                request.applyUrl(),
                request.content()
        );
        return NoticeDetailResponse.from(notice);
    }

    // ===== Delete (소프트 삭제) =====
    @Transactional
    public void delete(Long id) {
        Notice notice = findNotice(id);
        notice.delete();
    }

    // ----- 내부 메서드 -----
    private Notice findNotice(Long id) {
        return noticeRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("공고를 찾을 수 없습니다. id=" + id));
    }

    private void validateDates(NoticeRequest request) {
        if (request.startDate().isAfter(request.deadline())) {
            throw new IllegalArgumentException("접수 시작일은 마감일보다 늦을 수 없습니다.");
        }
    }

    // sort 파라미터: deadline(마감임박순, 기본) / latest(최신순) / views(조회순)
    private Sort makeSort(String sort) {
        if ("latest".equals(sort)) {
            return Sort.by(Sort.Direction.DESC, "createdAt");
        }
        if ("views".equals(sort)) {
            return Sort.by(Sort.Direction.DESC, "viewCount");
        }
        return Sort.by(Sort.Direction.ASC, "deadline")
                .and(Sort.by(Sort.Direction.DESC, "id"));
    }
}
