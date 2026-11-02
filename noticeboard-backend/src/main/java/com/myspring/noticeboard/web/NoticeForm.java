package com.myspring.noticeboard.web;

import com.myspring.noticeboard.notice.NoticeCategory;
import com.myspring.noticeboard.notice.OrgType;
import com.myspring.noticeboard.notice.dto.NoticeRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

// Thymeleaf 등록/수정 폼 전용 객체 (th:field는 getter/setter가 필요해서 record 대신 클래스 사용)
@Getter
@Setter
@NoArgsConstructor
public class NoticeForm {

    @NotBlank(message = "제목은 필수입니다.")
    @Size(max = 200, message = "제목은 200자 이하로 입력해주세요.")
    private String title;

    @NotBlank(message = "기관명은 필수입니다.")
    @Size(max = 100, message = "기관명은 100자 이하로 입력해주세요.")
    private String organization;

    @NotNull(message = "기관 구분을 선택해주세요.")
    private OrgType orgType;

    @NotNull(message = "지원 분야를 선택해주세요.")
    private NoticeCategory category;

    @Size(max = 200)
    private String target;

    @Size(max = 50)
    private String region;

    @Size(max = 100)
    private String supportAmount;

    @NotNull(message = "접수 시작일을 입력해주세요.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) // <input type="date"> 값(yyyy-MM-dd) 변환
    private LocalDate startDate;

    @NotNull(message = "마감일을 입력해주세요.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate deadline;

    @Size(max = 500)
    private String applyUrl;

    private String content;

    public NoticeRequest toRequest() {
        return new NoticeRequest(title, organization, orgType, category, target, region,
                supportAmount, startDate, deadline, applyUrl, content);
    }

    public static NoticeForm from(NoticeRequest request) {
        NoticeForm form = new NoticeForm();
        form.setTitle(request.title());
        form.setOrganization(request.organization());
        form.setOrgType(request.orgType());
        form.setCategory(request.category());
        form.setTarget(request.target());
        form.setRegion(request.region());
        form.setSupportAmount(request.supportAmount());
        form.setStartDate(request.startDate());
        form.setDeadline(request.deadline());
        form.setApplyUrl(request.applyUrl());
        form.setContent(request.content());
        return form;
    }
}
