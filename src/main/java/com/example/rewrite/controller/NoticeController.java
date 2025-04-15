package com.example.rewrite.controller;

import com.example.rewrite.entity.Notice;
import com.example.rewrite.repository.Notice.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

    @GetMapping("/notice")
    public String notice(HttpSession session, RedirectAttributes redirectAttributes) {

        List<Notice> notices = noticeRepository.findAll();
        redirectAttributes.addFlashAttribute("notices", notices);

        return "noticeList";
    }

    @GetMapping("/noticeDetail")
    public String noticeDetail(HttpSession session, RedirectAttributes redirectAttributes) {

        return "user/noticeDetail";
    }
}
