package com.example.rewrite.repository;

import com.example.rewrite.command.NoticeVO;
import com.example.rewrite.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {
    List<NoticeVO> findNoticeVOByNoticeId(Long noticeId);
}
