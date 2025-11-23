package com.efub.doppelganger.review.dto;

import lombok.Getter;

@Getter
public class ReviewCreateRequestDto {
    private String title;
    private String content;
    private int score;
}
