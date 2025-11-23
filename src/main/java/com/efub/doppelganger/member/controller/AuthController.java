package com.efub.doppelganger.member.controller;

import com.efub.doppelganger.member.dto.request.LoginRequest;
import com.efub.doppelganger.member.dto.request.SignUpRequest;
import com.efub.doppelganger.member.dto.response.MemberResponse;
import com.efub.doppelganger.member.dto.response.TokenResponse;
import com.efub.doppelganger.member.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    //회원가입
    @PostMapping("/signup")
    public ResponseEntity<MemberResponse> signUp(@RequestBody SignUpRequest request) {
        MemberResponse response = authService.signup(request);
        return ResponseEntity.ok(response);
    }

    //로그인
    @PostMapping("/login/general")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
        TokenResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}
