package com.example.rewrite.controller;


import com.example.rewrite.command.NoticeDTO;
import com.example.rewrite.entity.Notice;
import com.example.rewrite.repository.Notice.NoticeRepository;
import com.example.rewrite.service.notice.NoticeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private NoticeService noticeService;

    @GetMapping("/modifyUsers") public String modifyUsers() {
        return "admin/modifyUsers";
    }
    @GetMapping("/noticeWrite") public String noticeWrite(Model model) {return "admin/noticeWrite";}


    @PostMapping("/write")
    public String write(NoticeDTO noticeDTO, Model model) {
        log.info("디버깅 - write 왔는지 체크");
        noticeService.uploadNotice(noticeDTO);
        return "redirect:/notice/noticeList";
    }
    @PostMapping("/noticeDelete/{noticeId}")
    public String deleteNoticePost(@PathVariable("noticeId") Long id) {
        log.info("공지사항 삭제 요청 (POST) - ID: {}", id);
        try {
            noticeService.deleteNotice(id);
        } catch (Exception e) {
            log.error("공지사항 삭제 중 오류 발생 - ID: {}", id, e);
        }
        return "redirect:/notice/noticeList";
    }

}
