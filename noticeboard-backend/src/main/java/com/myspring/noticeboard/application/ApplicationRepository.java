package com.myspring.noticeboard.application;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    boolean existsByNoticeId(Long noticeId);

    Optional<Application> findByNoticeId(Long noticeId);

    // fetch join: 지원 목록을 가져올 때 공고도 한 번에 가져와서 N+1 문제 방지
    @Query("""
            select a from Application a
            join fetch a.notice n
            where n.deleted = false
            order by n.deadline asc, a.id desc
            """)
    List<Application> findAllWithNotice();

    // 목록 화면에서 "이미 추가한 공고" 표시용
    @Query("select a.notice.id from Application a")
    List<Long> findAllNoticeIds();
}
