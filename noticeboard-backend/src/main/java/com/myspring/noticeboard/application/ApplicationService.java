package com.myspring.noticeboard.application;

import com.myspring.noticeboard.application.dto.ApplicationResponse;
import com.myspring.noticeboard.global.NotFoundException;
import com.myspring.noticeboard.notice.Notice;
import com.myspring.noticeboard.notice.NoticeRepository;
import com.myspring.noticeboard.notice.NoticeStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final NoticeRepository noticeRepository;

    // ===== Create: 공고를 지원 관리에 추가 =====
    @Transactional
    public Long create(Long noticeId) {
        Notice notice = noticeRepository.findByIdAndDeletedFalse(noticeId)
                .orElseThrow(() -> new NotFoundException("공고를 찾을 수 없습니다. id=" + noticeId));

        if (notice.getStatus() == NoticeStatus.CLOSED) {
            throw new IllegalStateException("마감된 공고는 지원 관리에 추가할 수 없습니다.");
        }
        if (applicationRepository.existsByNoticeId(noticeId)) {
            throw new IllegalStateException("이미 지원 관리에 추가된 공고입니다.");
        }

        Application saved = applicationRepository.save(new Application(notice));
        return saved.getId();
    }

    // ===== Read =====
    public List<ApplicationResponse> getList(ApplyStatus status) {
        List<ApplicationResponse> result = new ArrayList<>();
        for (Application application : applicationRepository.findAllWithNotice()) {
            if (status == null || application.getStatus() == status) {
                result.add(ApplicationResponse.from(application));
            }
        }
        return result;
    }

    // 보드 화면용: 상태별로 묶기 (관심 → 준비중 → 제출완료 → 합격 → 불합격 순서 유지)
    public Map<ApplyStatus, List<ApplicationResponse>> getBoard() {
        Map<ApplyStatus, List<ApplicationResponse>> board = new LinkedHashMap<>();
        for (ApplyStatus status : ApplyStatus.values()) {
            board.put(status, new ArrayList<>());
        }
        for (Application application : applicationRepository.findAllWithNotice()) {
            board.get(application.getStatus()).add(ApplicationResponse.from(application));
        }
        return board;
    }

    public Set<Long> getAppliedNoticeIds() {
        return new HashSet<>(applicationRepository.findAllNoticeIds());
    }

    public Optional<ApplicationResponse> findByNoticeId(Long noticeId) {
        return applicationRepository.findByNoticeId(noticeId)
                .map(ApplicationResponse::from);
    }

    // ===== Update =====
    @Transactional
    public ApplicationResponse changeStatus(Long id, ApplyStatus status) {
        Application application = findApplication(id);
        application.changeStatus(status);
        return ApplicationResponse.from(application);
    }

    @Transactional
    public ApplicationResponse updateMemo(Long id, String memo) {
        if (memo != null && memo.length() > 500) {
            throw new IllegalArgumentException("메모는 500자 이하로 입력해주세요.");
        }
        Application application = findApplication(id);
        application.updateMemo(memo);
        return ApplicationResponse.from(application);
    }

    // ===== Delete =====
    @Transactional
    public void delete(Long id) {
        Application application = findApplication(id);
        applicationRepository.delete(application);
    }

    private Application findApplication(Long id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("지원 내역을 찾을 수 없습니다. id=" + id));
    }
}
