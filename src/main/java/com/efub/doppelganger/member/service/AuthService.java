package com.efub.doppelganger.member.service;

import com.efub.doppelganger.Security.JWT.JwtTokenProvider;
import com.efub.doppelganger.member.domain.LoginType;
import com.efub.doppelganger.member.domain.Member;
import com.efub.doppelganger.member.dto.request.LoginRequest;
import com.efub.doppelganger.member.dto.request.SignUpRequest;
import com.efub.doppelganger.member.dto.response.MemberResponse;
import com.efub.doppelganger.member.dto.response.TokenResponse;
import com.efub.doppelganger.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    //회원가입
    @Transactional
    public MemberResponse signup(SignUpRequest request) {
        // 1. 이메일 중복 체크
        if (memberRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
        //2. 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        //3. 사용자 생성
        Member member = Member.builder()
                .email(request.getEmail())
                .password(encodedPassword)
                .nickname(request.getNickname())
                .loginType(LoginType.NORMAL)
                .build();

        //4. 데이터베이스에 저장
        Member savedMember = memberRepository.save(member);
        return new MemberResponse(savedMember);
    }

    //로그인
    @Transactional(readOnly = true)
    public TokenResponse login(LoginRequest request) {
        //1. 사용자 조회
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        //2. 일반 로그인 사용자인지 확인
        if(member.getLoginType() !=  LoginType.NORMAL) {
            throw new IllegalArgumentException("소셜 로그인 사용자입니다.");
        }

        //3. 비밀번호 검증
        if(!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        //4. JWT 토큰 생성
        String token = jwtTokenProvider.generateToken(member.getEmail());
        return new TokenResponse(token);
    }
}
