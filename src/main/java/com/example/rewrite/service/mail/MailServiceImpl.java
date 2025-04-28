package com.example.rewrite.service.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

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
                "요청하신 비밀번호는 " + password + " 입니다." +
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

    @Override
    @Async
    public void sendWelcomeEmail(String toEmail, String userId, String name) {
        log.info("[MailService] 웰컴 이메일 발송 시작: {}", toEmail);

        try {
            // MimeMessage 객체 생성 (HTML을 지원하는 메시지 객체)
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            // 이메일 정보 설정
            helper.setTo(toEmail);
            helper.setSubject("[Rewrite] " + name + "님, 회원가입을 환영합니다.");

            // HTML 내용 설정
            String htmlContent =
                    "<div style='background-color:#f0f8ff; padding:20px;'>" +
                            "  <div style='max-width:600px; margin:0 auto; background-color:#ffffff; padding:20px; border-radius:10px; font-family: Arial, sans-serif;'>" +
                            "    <h1 style='color:#2e8b57; text-align:center;'>환영합니다! 다시 쓰다 플랫폼에 오신 것을 환영합니다! ♻️</h1>" +
                            "    <p style='font-size:16px; line-height:1.5;'>안녕하세요 <strong>" + name + "</strong>님,</p>" +
                            "    <p style='font-size:16px; line-height:1.5;'><strong>다시 쓰다</strong> 중고물품 거래 플랫폼에 가입해 주셔서 감사합니다. 환경을 생각하고, 자원을 아끼는 현명한 선택을 하셨습니다.</p>" +
                            "    <p style='font-size:16px; line-height:1.5;'>이제 다양한 중고 물품을 안전하고 편리하게 거래하실 수 있습니다.</p>" +
                            "    <div style='text-align:center; margin-top:30px;'>" +
                            "      <a href='https://rewrite.com/login?userId=" + userId + "' style='background-color:#2e8b57; color:white; padding:12px 24px; text-decoration:none; border-radius:5px; font-weight:bold;'>거래 시작하기</a>" +
                            "    </div>" +
                            "    <p style='margin-top:30px; font-size:14px; color:#666; text-align:center;'>문의사항이 있으시면 언제든지 연락주세요!</p>" +
                            "  </div>" +
                            "</div>";

            helper.setText(htmlContent, true); // true 파라미터가 HTML 메일임을 나타냄

            // 이메일 발송
            mailSender.send(mimeMessage);

            log.info("[MailService] 웰컴 이메일 발송 완료: {}", toEmail);
        } catch (MailException | MessagingException e) {
            log.error("[MailService] 웰컴 이메일 발송 실패 - Email: {}, Error: {}", toEmail, e.getMessage(), e);
        }
    }
}
