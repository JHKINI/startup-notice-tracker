package com.myspring.noticeboard.application;

// 지원 진행 상태
public enum ApplyStatus {
    INTERESTED("관심"),
    PREPARING("준비중"),
    SUBMITTED("제출완료"),
    PASSED("합격"),
    FAILED("불합격");

    private final String label;

    ApplyStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    /*
     * 상태 변경 규칙
     * - 관심 / 준비중 / 제출완료 끼리는 자유롭게 이동
     * - 합격 / 불합격은 "제출완료" 이후에만 가능
     * - 합격 / 불합격에서는 "제출완료"로만 되돌릴 수 있음 (결과 정정용)
     */
    public boolean canChangeTo(ApplyStatus next) {
        if (this == next) {
            return true;
        }
        if (next == PASSED || next == FAILED) {
            return this == SUBMITTED || this == PASSED || this == FAILED;
        }
        if (this == PASSED || this == FAILED) {
            return next == SUBMITTED;
        }
        return true;
    }
}
