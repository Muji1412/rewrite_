package com.example.rewrite.service.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;

    @Async // 비동기 실행 (별도 스레드에서 동작)
    public void sendUserIdByEmail(String toEmail, String userId) {
        log.info("[MailService] 아이디 찾기 메일 발송 시작: {}", toEmail);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("[Rewrite] 아이디 찾기 결과 안내");
        message.setText("안녕하세요.\n\n" +
                "계정의 아이디는 [ " + userId + " ] 입니다.\n\n" + // Users 엔티티의 'id' 필드 값
                "서비스를 이용해주셔서 감사합니다.");

        try {
            mailSender.send(message);
            log.info("[MailService] 아이디 찾기 메일 발송 성공: {}", toEmail);
        } catch (MailException e) {
            // 실패 로그를 상세히 남김 (예: 설정 오류, 네트워크 문제 등 파악)
            log.error("[MailService] 아이디 찾기 메일 발송 실패 - Email: {}, Error: {}", toEmail, e.getMessage(), e);
            // 필요하다면, 실패 시 알림 발송, 재시도 로직 등을 추가 고려
        }
    }

    @Override
    @Async
    public void sendUserIdPasswordByEmail(String toEmail, String userId, String password) {
        log.info("[MailService] 비밀번호 찾기 메일 발송 시작: {}", toEmail);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("[Rewrite] 비밀번호 찾기 결과 안내");
        message.setText("안녕하세요.\n\n" +
                "요청하신 비밀번호는" + password + "입니다." +
                "\n\n" +
                "로그인 하신 후 즉시 비밀번호를 변경하여 사용해주시길 바랍니다." +
                "서비스를 이용해주셔서 감사합니다.");

        try {
            mailSender.send(message);
            log.info("[MailService] 비밀번호 찾기 메일 발송 성공: {}", toEmail);
        }catch (MailException e) {
            // {} -> 이게 placeHolder임. xml 설정하는것 처럼, 뒤에 들어가있는 애들이 차례로 들어감.
            // 단, SLF4J의 기능인데, 로깅 메서드의 마지막 파라미터로 Throwable 타입을 넣어주면 플레이스홀더가 아닌 스택 트레이서로 넣어줌.
            log.error("[MailService] 비밀번호 찾기 메일 발송 실패 - Email: {}, Error: {}", toEmail, e.getMessage(), e);
        }

    }
}
