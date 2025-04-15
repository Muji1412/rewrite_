package com.example.rewrite.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/qna")
public class QnaController {

    @GetMapping("/qnaList")
    public String inquiryList(HttpSession session, RedirectAttributes redirectAttributes) {
        return "Qna/QnaList";
    }
    @GetMapping("/qnaWrite")
    public String writeInquiry(HttpSession session, RedirectAttributes redirectAttributes) {
        return "Qna/QnaWrite";
    }
    @GetMapping("/qnaAnswer")
    public String inquiryAnswer(HttpSession session, RedirectAttributes redirectAttributes) {
        return "qna/QnaAnswer";
    }

}
