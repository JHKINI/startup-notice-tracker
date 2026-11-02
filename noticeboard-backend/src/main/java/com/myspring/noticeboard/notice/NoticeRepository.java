package com.myspring.noticeboard.notice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    // 삭제 안 된 공고만 단건 조회
    Optional<Notice> findByIdAndDeletedFalse(Long id);

    // 검색 + 필터 (파라미터가 null이면 그 조건은 무시)
    @Query("""
            select n from Notice n
            where n.deleted = false
              and (:keyword is null
                   or n.title like concat('%', :keyword, '%')
                   or n.organization like concat('%', :keyword, '%'))
              and (:orgType is null or n.orgType = :orgType)
              and (:category is null or n.category = :category)
              and (:status is null or n.status = :status)
            """)
    Page<Notice> search(@Param("keyword") String keyword,
                        @Param("orgType") OrgType orgType,
                        @Param("category") NoticeCategory category,
                        @Param("status") NoticeStatus status,
                        Pageable pageable);
}
