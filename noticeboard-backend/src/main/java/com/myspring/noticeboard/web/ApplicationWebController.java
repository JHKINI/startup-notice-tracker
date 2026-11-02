package com.myspring.noticeboard.web;

import com.myspring.noticeboard.application.ApplicationService;
import com.myspring.noticeboard.application.ApplyStatus;
import com.myspring.noticeboard.global.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/applications")
@RequiredArgsConstructor
public class ApplicationWebController {

    private final ApplicationService applicationService;

    // 지원 관리 보드
    @GetMapping
    public String board(Model model) {
        model.addAttribute("board", applicationService.getBoard());
        model.addAttribute("statuses", ApplyStatus.values());
        return "applications/board";
    }

    // 공고를 지원 관리에 추가
    @PostMapping
    public String create(@RequestParam Long noticeId,
                         @RequestParam(required = false) String returnUrl,
                         RedirectAttributes redirectAttributes) {
        try {
            applicationService.create(noticeId);
            redirectAttributes.addFlashAttribute("message", "지원 관리에 추가했습니다. (상태: 관심)");
        } catch (IllegalStateException | NotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:" + safeReturnUrl(returnUrl);
    }

    // 상태 변경
    @PostMapping("/{id}/status")
    public String changeStatus(@PathVariable Long id,
                               @RequestParam ApplyStatus status,
                               RedirectAttributes redirectAttributes) {
        try {
            applicationService.changeStatus(id, status);
            redirectAttributes.addFlashAttribute("message", "상태를 '" + status.getLabel() + "'(으)로 변경했습니다.");
        } catch (IllegalStateException | NotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/applications";
    }

    // 메모 수정
    @PostMapping("/{id}/memo")
    public String updateMemo(@PathVariable Long id,
                             @RequestParam(required = false) String memo,
                             RedirectAttributes redirectAttributes) {
        try {
            applicationService.updateMemo(id, memo);
            redirectAttributes.addFlashAttribute("message", "메모를 저장했습니다.");
        } catch (IllegalArgumentException | NotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/applications";
    }

    // 삭제
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            applicationService.delete(id);
            redirectAttributes.addFlashAttribute("message", "지원 관리에서 삭제했습니다.");
        } catch (NotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/applications";
    }

    // 외부 사이트로 리다이렉트되는 것 방지 (내부 경로만 허용)
    private String safeReturnUrl(String returnUrl) {
        if (returnUrl != null && returnUrl.startsWith("/") && !returnUrl.startsWith("//")) {
            return returnUrl;
        }
        return "/applications";
    }
}
