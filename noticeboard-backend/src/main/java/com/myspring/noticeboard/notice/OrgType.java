package com.myspring.noticeboard.notice;

// 공고 주관 기관 구분 (K-Startup 상단 탭과 동일)
public enum OrgType {
    PUBLIC("중앙부처·지자체·공공기관"),
    PRIVATE("민간기관·교육기관");

    private final String label;

    OrgType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
