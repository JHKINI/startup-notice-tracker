package com.myspring.noticeboard.notice;

// 지원 분야
public enum NoticeCategory {
    COMMERCIALIZATION("사업화"),
    MENTORING("멘토링·컨설팅·교육"),
    STARTUP_EDUCATION("창업교육"),
    FACILITY("시설·공간·보육"),
    FUNDING("융자·투자"),
    EVENT("행사·네트워크"),
    ETC("기타");

    private final String label;

    NoticeCategory(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
