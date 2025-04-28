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

        String baseUrl = "https://https://rewrite.o-r.kr/";
        String loginUrl = baseUrl + "user/login?userId=" + userId;
        String privacyPolicyUrl = baseUrl + "/privacy";

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("[ReWrite] " + name + "님, 새로운 가족이 되신 것을 환영합니다! ♻️");
            helper.setFrom("noreply@rewrite.com");

            String htmlContent =
                    "<div style='background-color:#f8f9fa; padding:40px 20px; font-family: \"Malgun Gothic\", \"맑은 고딕\", sans-serif;'>" +
                            "  <div style='max-width:600px; margin:0 auto; background-color:#ffffff; padding:30px; border-radius:12px; box-shadow: 0 4px 12px rgba(0,0,0,0.08);'>" +
                            // "    <div style='text-align:center; margin-bottom:25px;'>" +
                            // "      <img src='YOUR_LOGO_URL_HERE' alt='ReWrite 로고' style='max-width:150px;'>" +
                            // "    </div>" +
                            "    <h1 style='color:#343a40; text-align:center; font-size:26px; margin-bottom:15px; font-weight: 600;'>" +
                            name + "님, ReWrite의 새로운 가족이 되신 것을 환영합니다! <span style='font-size: 20px;'>♻️</span>" +
                            "    </h1>" +
                            "    <p style='font-size:16px; line-height:1.7; color:#495057; margin-bottom: 20px;'>" +
                            "      안녕하세요, <strong>" + name + "</strong>님!" +
                            "    </p>" +
                            "    <p style='font-size:16px; line-height:1.7; color:#495057; margin-bottom: 20px;'>" +
                            "      ReWrite에 오신 것을 진심으로 환영합니다. 저희와 함께 자원 순환이라는 의미 있는 여정을 시작해주셔서 정말 기쁩니다." +
                            "    </p>" +
                            "    <p style='font-size:16px; line-height:1.7; color:#495057; margin-bottom: 25px;'>" +
                            name + "님의 참여는 잠들어 있는 물건에 새 생명을 불어넣고, 우리 환경에 긍정적인 변화를 만드는 소중한 발걸음이 될 거예요. ReWrite는 단순한 중고 거래 플랫폼을 넘어, '가치 있는 소비'와 '지속 가능한 삶'을 함께 고민하고 실천하는 커뮤니티를 만들어가고 있습니다." +
                            "    </p>" +
                            "    <h2 style='color:#20c997; font-size:20px; margin-top:30px; margin-bottom:15px; border-left: 4px solid #20c997; padding-left: 10px; font-weight: 600;'>" +
                            "      ReWrite에서는 이런 경험을 하실 수 있어요:" +
                            "    </h2>" +
                            "    <ul style='font-size:16px; line-height:1.8; color:#495057; padding-left: 20px; margin-bottom: 30px; list-style-position: outside;'>" + // list-style-position 추가
                            "      <li><strong>믿음직한 거래:</strong> 안전결제 시스템과 사용자 후기를 통해 안심하고 거래하세요.</li>" +
                            "      <li><strong>손쉬운 이용:</strong> 몇 번의 터치만으로 간편하게 물건을 등록하고 찾아볼 수 있습니다.</li>" +
                            "      <li><strong>따뜻한 소통:</strong> 1:1 채팅으로 판매자, 구매자와 편안하게 대화하며 궁금증을 해결하세요.</li>" +
                            "    </ul>" +
                            "    <h2 style='color:#20c997; font-size:20px; margin-top:30px; margin-bottom:15px; border-left: 4px solid #20c997; padding-left: 10px; font-weight: 600;'>" +
                            "      지금 바로 ReWrite를 시작해보세요!" +
                            "    </h2>" +
                            "    <p style='font-size:16px; line-height:1.7; color:#495057; margin-bottom: 30px;'>" +
                            name + "님의 프로필을 완성하고 첫 거래를 시작해보는 건 어떠세요? 멋진 물건을 발견하거나, 안 쓰는 물건에 새 주인을 찾아주는 즐거움을 경험하실 수 있을 거예요." +
                            "    </p>" +
                            "    <div style='text-align:center; margin: 35px 0;'>" +
                            "      <a href='" + loginUrl + "' style='background-color:#20c997; color:white; padding: 14px 28px; text-decoration:none; border-radius:8px; font-weight:bold; font-size: 17px; display: inline-block; box-shadow: 0 2px 4px rgba(0,0,0,0.1);'>" +
                            "        ReWrite 시작하기" +
                            "      </a>" +
                            "    </div>" +
                            "    <p style='font-size:15px; line-height:1.7; color:#6c757d; margin-top:30px; text-align:center;'>" +
                            "      궁금한 점이나 도움이 필요하시면 언제든 편하게<br>고객센터(<a href='mailto:help@rewrite.com' style='color:#20c997; text-decoration:none; font-weight: 600;'>help@rewrite.com</a>)로 문의해주세요.<br>ReWrite 팀이 항상 " + name + "님의 곁에서 돕겠습니다." +
                            "    </p>" +
                            "    <p style='margin-top:35px; font-size:15px; color:#495057; text-align:center; font-weight: 600;'>" +
                            "      다시 한번 환영의 인사를 전하며,<br>" +
                            "      ReWrite 팀 드림" +
                            "    </p>" +
                            "    <hr style='border: none; border-top: 1px solid #e9ecef; margin: 30px 0;'>" +
                            "    <p style='font-size:12px; color:#adb5bd; text-align:center; line-height:1.6;'>" +
                            "      ⓒ 2025 ReWrite Inc. | 서울시 성북구<br>" +
                            "      <a href='" + privacyPolicyUrl + "' style='color:#adb5bd; text-decoration:none;'>개인정보처리방침</a> | " +
                            "    </p>" +
                            "  </div>" +
                            "</div>";


            helper.setText(htmlContent, true); // true: HTML 메일 사용

            // --- 이메일 발송 ---
            mailSender.send(mimeMessage);

            log.info("[MailService] 웰컴 이메일 발송 완료: {}", toEmail);
        } catch (MailException | MessagingException e) {
            log.error("[MailService] 웰컴 이메일 발송 실패 - Email: {}, UserId: {}, Name: {}, Error: {}", toEmail, userId, name, e.getMessage(), e);
        }
    }
}
