package com.myspring.noticeboard.web;

import com.myspring.noticeboard.application.ApplicationService;
import com.myspring.noticeboard.notice.NoticeCategory;
import com.myspring.noticeboard.notice.NoticeService;
import com.myspring.noticeboard.notice.OrgType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// Thymeleaf 화면용 컨트롤러 (HTML 반환)
@Controller
@RequiredArgsConstructor
public class NoticeWebController {

    private final NoticeService noticeService;
    private final ApplicationService applicationService;

    @GetMapping("/")
    public String home() {
        return "redirect:/notices";
    }

    // 목록
    @GetMapping("/notices")
    public String list(@RequestParam(required = false) String keyword,
                       @RequestParam(required = false) OrgType orgType,
                       @RequestParam(required = false) NoticeCategory category,
                       @RequestParam(defaultValue = "false") boolean includeClosed,
                       @RequestParam(defaultValue = "deadline") String sort,
                       @RequestParam(defaultValue = "1") int page,
                       Model model) {

        model.addAttribute("result",
                noticeService.getList(keyword, orgType, category, includeClosed, sort, page, 10));
        model.addAttribute("appliedNoticeIds", applicationService.getAppliedNoticeIds());

        // 검색 조건 유지용
        model.addAttribute("keyword", keyword);
        model.addAttribute("orgType", orgType);
        model.addAttribute("category", category);
        model.addAttribute("includeClosed", includeClosed);
        model.addAttribute("sort", sort);

        model.addAttribute("orgTypes", OrgType.values());
        model.addAttribute("categories", NoticeCategory.values());
        return "notices/list";
    }

    // 상세
    @GetMapping("/notices/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("notice", noticeService.getDetail(id));
        model.addAttribute("myApplication", applicationService.findByNoticeId(id).orElse(null));
        return "notices/detail";
    }

    // 등록 폼
    @GetMapping("/notices/new")
    public String createForm(Model model) {
        model.addAttribute("form", new NoticeForm());
        addFormOptions(model, null);
        return "notices/form";
    }

    // 등록 처리
    @PostMapping("/notices")
    public String create(@Valid @ModelAttribute("form") NoticeForm form,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        checkDates(form, bindingResult);
        if (bindingResult.hasErrors()) {
            addFormOptions(model, null);
            return "notices/form"; // 입력값 유지한 채로 폼 다시 보여줌
        }

        Long id = noticeService.create(form.toRequest());
        redirectAttributes.addFlashAttribute("message", "공고가 등록되었습니다.");
        return "redirect:/notices/" + id;
    }

    // 수정 폼
    @GetMapping("/notices/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("form", NoticeForm.from(noticeService.getEditRequest(id)));
        addFormOptions(model, id);
        return "notices/form";
    }

    // 수정 처리
    @PostMapping("/notices/{id}/edit")
    public String edit(@PathVariable Long id,
                       @Valid @ModelAttribute("form") NoticeForm form,
                       BindingResult bindingResult,
                       Model model,
                       RedirectAttributes redirectAttributes) {
        checkDates(form, bindingResult);
        if (bindingResult.hasErrors()) {
            addFormOptions(model, id);
            return "notices/form";
        }

        noticeService.update(id, form.toRequest());
        redirectAttributes.addFlashAttribute("message", "공고가 수정되었습니다.");
        return "redirect:/notices/" + id;
    }

    // 삭제 (소프트 삭제)
    @PostMapping("/notices/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        noticeService.delete(id);
        redirectAttributes.addFlashAttribute("message", "공고가 삭제되었습니다.");
        return "redirect:/notices";
    }

    // ----- 내부 메서드 -----
    private void addFormOptions(Model model, Long noticeId) {
        model.addAttribute("orgTypes", OrgType.values());
        model.addAttribute("categories", NoticeCategory.values());
        model.addAttribute("noticeId", noticeId); // null이면 등록, 값이 있으면 수정
    }

    private void checkDates(NoticeForm form, BindingResult bindingResult) {
        if (form.getStartDate() != null && form.getDeadline() != null
                && form.getStartDate().isAfter(form.getDeadline())) {
            bindingResult.rejectValue("deadline", "invalidDate", "마감일은 접수 시작일 이후여야 합니다.");
        }
    }
}
