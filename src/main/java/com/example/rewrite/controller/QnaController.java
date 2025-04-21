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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/qna")
public class QnaController {

    @Autowired
    private QnaRepository qnaRepository;
    @GetMapping("/qnaList")
    public String inquiryList(HttpSession session, RedirectAttributes redirectAttributes, Pageable pageable, Model model) {

        // 아래 detail 과 동일하게 @PageableDefault 의 size 디폴트값이 10이므로, page만 받아서 진행함.
        Page<Qna> qnaPage = qnaRepository.findAll(pageable); // 페이지에이블
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

    @GetMapping("/qnaWrite")
    public String writeInquiry(HttpSession session, RedirectAttributes redirectAttributes) {
        return "qna/qnaWrite";
    }

    @GetMapping("/qnaAnswer")
    public String inquiryAnswer(HttpSession session, RedirectAttributes redirectAttributes) {
        return "qna/qnaAnswer";
    }

    @GetMapping("/qna")
    public String inquiry(HttpSession session, RedirectAttributes redirectAttributes) {
        return "qna/qna";
    }


}
