package com.example.rewrite.NoticeTest;

import com.example.rewrite.entity.Notice;
import com.example.rewrite.repository.Notice.NoticeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;

@SpringBootTest
public class NoticeTest {

    @Autowired
    private NoticeRepository noticeRepository;

    @Test
    public void uploadTest() {
        Notice notice = Notice.builder()
                .content("내용입니다.")
                .img("https://storage.googleapis.com/rewrite_project/notice/%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202025-04-15%20151045.png")
                .title("테스트 - 제목입니다.")
                .build();

        noticeRepository.save(notice);

    }
    @Test
    @Transactional
    public void getListTest() {
        List<Notice> noticeList = noticeRepository.findAll();
        for (Notice notice : noticeList) {
            System.out.println(notice);
        }
    }

}
