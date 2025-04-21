package com.example.rewrite.repository.qna;

import com.example.rewrite.entity.Qna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QnaRepository  extends JpaRepository<Qna, Long> {

    //Qna 관련 메서드들?
}
