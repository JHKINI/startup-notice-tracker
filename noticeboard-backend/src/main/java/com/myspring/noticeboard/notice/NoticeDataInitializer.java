package com.myspring.noticeboard.notice;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

// 서버 시작 시 공고가 하나도 없으면 샘플 데이터를 넣어줌 (테스트 편의용)
@Component
@RequiredArgsConstructor
public class NoticeDataInitializer implements CommandLineRunner {

    private final NoticeRepository noticeRepository;

    @Override
    public void run(String... args) {
        if (noticeRepository.count() > 0) {
            return;
        }

        LocalDate today = LocalDate.now();

        List<Notice> samples = List.of(
                Notice.builder()
                        .title("대학생 예비창업 아이디어 IR 경진대회 참가자 모집")
                        .organization("OO금융재단").orgType(OrgType.PRIVATE)
                        .category(NoticeCategory.COMMERCIALIZATION)
                        .target("대학생, 예비창업자").region("전국").supportAmount("최대 1,000만원")
                        .startDate(today.minusDays(30)).deadline(today)
                        .applyUrl("https://example.com/apply/1")
                        .content("대학생 예비창업팀의 아이디어를 IR 발표로 검증하는 프로그램입니다.")
                        .build(),
                Notice.builder()
                        .title("기술창업 피보팅 전략 특강")
                        .organization("주식회사 OO랩").orgType(OrgType.PRIVATE)
                        .category(NoticeCategory.MENTORING)
                        .target("초기창업자(3년 이내)").region("온라인").supportAmount("무료")
                        .startDate(today.minusDays(20)).deadline(today.plusDays(1))
                        .applyUrl("https://example.com/apply/2")
                        .content("시장 변화에 맞춘 피보팅 사례와 전략을 다룹니다.")
                        .build(),
                Notice.builder()
                        .title("사회적경제 현장사례 아카데미")
                        .organization("OO구 사회적경제지원센터").orgType(OrgType.PUBLIC)
                        .category(NoticeCategory.STARTUP_EDUCATION)
                        .target("예비창업자, 사회적기업 관심자").region("서울").supportAmount("교육 무료")
                        .startDate(today.minusDays(10)).deadline(today.plusDays(3))
                        .applyUrl("https://example.com/apply/3")
                        .content("착한 사업을 실제로 운영하는 기업의 현장 사례를 배웁니다.")
                        .build(),
                Notice.builder()
                        .title("2026 하반기 예비창업패키지 추가 모집")
                        .organization("창업지원기관 OO").orgType(OrgType.PUBLIC)
                        .category(NoticeCategory.COMMERCIALIZATION)
                        .target("예비창업자").region("전국").supportAmount("최대 1억원")
                        .startDate(today.minusDays(5)).deadline(today.plusDays(14))
                        .applyUrl("https://example.com/apply/4")
                        .content("사업화 자금과 창업 교육, 멘토링을 지원합니다.")
                        .build(),
                Notice.builder()
                        .title("청년 창업공간 입주기업 모집")
                        .organization("OO시 창업지원센터").orgType(OrgType.PUBLIC)
                        .category(NoticeCategory.FACILITY)
                        .target("만 39세 이하 청년 창업자").region("경기").supportAmount("사무공간 1년 무상")
                        .startDate(today.minusDays(3)).deadline(today.plusDays(21))
                        .applyUrl("https://example.com/apply/5")
                        .content("독립 사무공간과 회의실, 네트워킹 프로그램을 제공합니다.")
                        .build(),
                Notice.builder()
                        .title("스타트업 데모데이 참가기업 모집 (마감된 공고)")
                        .organization("OO벤처스").orgType(OrgType.PRIVATE)
                        .category(NoticeCategory.EVENT)
                        .target("시드~프리A 단계 스타트업").region("서울").supportAmount("투자 연계")
                        .startDate(today.minusDays(40)).deadline(today.minusDays(2))
                        .applyUrl("https://example.com/apply/6")
                        .content("투자자 앞에서 발표할 스타트업을 모집합니다.")
                        .build()
        );

        noticeRepository.saveAll(samples);
    }
}
