package com.example.rewrite.service;

import com.example.rewrite.command.NoticeVO; // @Entity 클래스
import com.example.rewrite.entity.Notice;
import com.example.rewrite.repository.NoticeRepository;
import lombok.RequiredArgsConstructor; // final 필드 생성자 자동 주입
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // 조회 시 readOnly=true 추천

import java.util.Optional;

@Service
@RequiredArgsConstructor // final 필드 생성자 자동 생성 및 주입
public class NoticeService {

    private final NoticeRepository noticeRepository; // 생성자 주입 방식 권장

    /**
     * ID로 공지사항 1건 조회 (가장 기본적인 방법)
     * @param id 조회할 공지사항 ID
     * @return 조회된 NoticeVO 객체 (없으면 null 반환)
     */
    @Transactional(readOnly = true) // 데이터를 변경하지 않으므로 readOnly=true (성능 이점)
    public Notice findNoticeByIdSimple(Long id) {
        // 1. JpaRepository가 제공하는 findById 사용 (반환 타입: Optional<NoticeVO>)
        Optional<Notice> optionalNotice = noticeRepository.findById(id);

        // 2. Optional 객체에서 실제 NoticeVO 객체를 꺼냄
        //    - orElse(null): 객체가 있으면 꺼내고, 없으면 null 반환 (가장 간단)
        Notice notice = optionalNotice.orElse(null);

        // (참고) 만약 없으면 예외를 던지고 싶다면 아래처럼 사용
        // NoticeVO notice = optionalNotice.orElseThrow(() -> new RuntimeException(id + "번 공지 없음!"));

        if (notice == null) {
            System.out.println(id + "번 공지사항 없음!");
            // 실제로는 로깅을 하거나 다른 처리를 할 수 있음
        }

        return notice; // 찾은 NoticeVO 객체 또는 null 반환
    }
}