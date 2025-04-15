package com.example.rewrite.controller;

import com.example.rewrite.entity.Notice;
import com.example.rewrite.repository.Notice.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    private NoticeRepository noticeRepository;

    @GetMapping("/noticeList")
    public String notice(@PageableDefault(sort = "noticeId", direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        Page<Notice> noticePage = noticeRepository.findAll(pageable); // 페이지에이블
        model.addAttribute("noticePage", noticePage);

        // 페이지 블록 계산 - 10개씩
        int pageBlockSize = 10;
        int pageNow = noticePage.getPageable().getPageNumber() + 1; // 현재 페이지
        int totalPages = noticePage.getTotalPages();

        // 시작, 끝 계산기
        int startIdx = ((pageNow - 1) / pageBlockSize) * pageBlockSize + 1;
        int endIdx = Math.min(startIdx + pageBlockSize - 1, totalPages);

        model.addAttribute("startIdx", startIdx);
        model.addAttribute("endIdx", endIdx);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageNow", pageNow);

        return "notice/noticeList";
    }

    @GetMapping("/noticeDetail")
    public String noticeDetail(HttpSession session, RedirectAttributes redirectAttributes) {

        return "notice/noticeDetail";
    }
}
