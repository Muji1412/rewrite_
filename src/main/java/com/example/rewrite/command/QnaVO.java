package com.example.rewrite.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QnaVO {
    private String qna_id;
    private String answer;
    private String category;
    private String content;
    private String key2;
    private String reg_data;
    private String title;
}
