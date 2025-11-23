package com.efub.doppelganger.global;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {

    // R201, A001 등 오류 코드를 담는 필드
    private final String code;

    // "Accommodation inventory is insufficient..." 등 상세 메시지를 담는 필드
    private final String message;
}