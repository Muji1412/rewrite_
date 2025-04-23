package com.example.rewrite.NoticeTest;

import com.example.rewrite.entity.Notice;
import com.example.rewrite.entity.Product;
import com.example.rewrite.repository.Notice.NoticeRepository;
import com.example.rewrite.repository.product.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;

@SpringBootTest
public class NoticeTest {

    @Autowired
    private NoticeRepository noticeRepository;
    @Autowired
    private ProductRepository productRepository;

    @Test
    @Transactional
    public void uploadTest() {
        Notice notice = Notice.builder()
                .content("내용입니다.")
                .img("https://storage.googleapis.com/rewrite_project/notice/%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202025-04-15%20151045.png")
                .title("테스트 - 제목입니다.")
                .build();

        noticeRepository.save(notice);

    }
    @Test
    public void uploadTest2() {
        for (int i = 0; i <= 200; i++) {
            Notice notice = Notice.builder()
                    .content("테스트 내용 " + i)
                    .title("테스트 제목 " + i)
                    .build();
            noticeRepository.save(notice);
        }
    }


    @Test
    public void getListTest() {
        List<Notice> noticeList = noticeRepository.findAll();
        for (Notice notice : noticeList) {
            System.out.println(notice);
        }
    }
    @Test
    public void getListTest2() {
        List<Product> list = productRepository.findProductsByUserUid(11L);
        for (Product product : list) {
            System.out.println(product);
        }
    }


}
