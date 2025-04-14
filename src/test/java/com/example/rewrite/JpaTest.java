package com.example.rewrite;

import com.example.rewrite.repository.NoticeRepository; // 리포지토리 임포트
import com.example.rewrite.service.NoticeService;    // 서비스 임포트
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired; // 빈 주입 어노테이션
import org.springframework.boot.test.context.SpringBootTest; // 스프링 부트 테스트 어노테이션

// AssertJ 라이브러리를 사용한 검증 (더 읽기 좋음)
import static org.assertj.core.api.Assertions.assertThat;
// JUnit Assertions도 사용 가능: import static org.junit.jupiter.api.Assertions.*;

/**
 * 스프링 부트 컨텍스트가 제대로 로드되고,
 * 필요한 빈(NoticeRepository, NoticeService)들이
 * 성공적으로 주입되는지 확인하는 간단한 테스트입니다.
 *
 * 주의: 이 테스트는 실제 DB 설정이나 H2 같은 테스트용 DB 설정을
 * application.properties 또는 application.yml 에서 읽어올 수 있습니다.
 */
@SpringBootTest // 이 어노테이션 하나로 스프링 부트 환경을 로드합니다. (통합 테스트)
public class JpaTest { // 클래스 이름은 JpaTest 등 자유롭게 지정 가능

    // @Autowired: 스프링 컨테이너가 관리하는 빈(Bean) 중에서
    // 해당 타입(NoticeRepository)의 빈을 찾아서 자동으로 주입해줍니다.
    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private NoticeService noticeService;

    @Test
    @DisplayName("스프링 컨텍스트 로드 및 빈 주입 확인")
    void contextLoads() {
        // 이 테스트의 목적은 애플리케이션 컨텍스트가 오류 없이 뜨고,
        // @Autowired 로 선언된 빈들이 null 이 아닌 상태로 제대로 주입되었는지 확인하는 것입니다.

        // AssertJ의 assertThat 사용 (JUnit의 assertNotNull(noticeRepository); 와 동일한 검증)
        assertThat(noticeRepository).isNotNull();
        assertThat(noticeService).isNotNull();

        // 주입된 객체의 클래스 이름 출력 (선택 사항)
        System.out.println("주입된 Repository 클래스: " + noticeRepository.getClass().getName());
        System.out.println("주입된 Service 클래스: " + noticeService.getClass().getName());

        // 여기까지 오류 없이 실행되면, 스프링 컨텍스트 로딩과 빈 주입은 성공한 것입니다.
    }
}