package com.example.rewrite.controller;

import com.example.rewrite.entity.Notice;
import com.example.rewrite.entity.Qna;
import com.example.rewrite.entity.Users;
import com.example.rewrite.repository.Notice.NoticeRepository;
import com.example.rewrite.repository.qna.QnaRepository;
import com.example.rewrite.repository.users.UsersRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/qna")
public class QnaController {

    @Autowired
    private QnaRepository qnaRepository;

    @Autowired
    private UsersRepository usersRepository;

    // 문의 목록 페이지
    @GetMapping("/qnaList")
    public String inquiryList(HttpSession session, RedirectAttributes redirectAttributes,
                              @PageableDefault(size = 10, sort = "qna_id", direction = Sort.Direction.DESC) Pageable pageable,
                              Model model) {
        // 페이지에이블로 데이터 조회
        Page<Qna> qnaPage = qnaRepository.findAll(pageable);
        model.addAttribute("qnaPage", qnaPage);

        // 페이지 블록 계산 - 10개씩
        int pageBlockSize = 10;
        int pageNow = qnaPage.getPageable().getPageNumber() + 1; // 현재 페이지
        int totalPages = qnaPage.getTotalPages();

        // 시작, 끝 계산기
        int startIdx = ((pageNow - 1) / pageBlockSize) * pageBlockSize + 1;
        int endIdx = Math.min(startIdx + pageBlockSize - 1, totalPages);

        model.addAttribute("startIdx", startIdx);
        model.addAttribute("endIdx", endIdx);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageNow", pageNow);

        return "qna/qnaList";
    }

    // 문의 작성 페이지
    @GetMapping("/qnaWrite")
    public String writeInquiry(HttpSession session, RedirectAttributes redirectAttributes, Model model) {
        // 로그인 체크 (선택적)
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            redirectAttributes.addFlashAttribute("message", "로그인이 필요한 서비스입니다.");
            return "redirect:/login";
        }

        // 빈 Qna 객체를 모델에 추가하여 폼 바인딩에 사용
        model.addAttribute("qna", new Qna());

        return "qna/qnaWrite";
    }

    // 문의 작성 처리 (POST)
    @PostMapping("/qnaWrite")
    public String saveInquiry(@ModelAttribute Qna qna, HttpSession session,
                              RedirectAttributes redirectAttributes) {
        // 로그인 체크
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            redirectAttributes.addFlashAttribute("message", "로그인이 필요한 서비스입니다.");
            return "redirect:/login";
        }

        // 사용자 ID 설정
        qna.setUid(userId);

        // 등록일 설정
        qna.setRegDate(String.valueOf(LocalDateTime.now()));

        // 저장
        qnaRepository.save(qna);

        redirectAttributes.addFlashAttribute("message", "문의가 등록되었습니다.");
        return "redirect:/qna/qnaList";
    }

    @GetMapping("/qnaAnswer")
    public String inquiryAnswer(HttpSession session, RedirectAttributes redirectAttributes) {
        return "qna/qnaAnswer";
    }

    @GetMapping("/qna")
    public String inquiry(HttpSession session, RedirectAttributes redirectAttributes) {
        return "qna/qna";
    }

    @GetMapping("/inquiry/submit")
    public String submitInquiry(HttpSession session, RedirectAttributes redirectAttributes) {
        return "qna/qnaList";
    }

}
