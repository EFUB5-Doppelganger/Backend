package com.efub.doppelganger.Security.OAuth;

import com.efub.doppelganger.Security.CustomMemberDetails;
import com.efub.doppelganger.Security.JWT.JwtTokenProvider;
import com.efub.doppelganger.member.domain.Member;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        // 1. OAuth2Member에서 사용자 정보 추출
        CustomMemberDetails memberDetails = (CustomMemberDetails) authentication.getPrincipal();
        Member member = memberDetails.getMember();

        System.out.println("OAuth2 로그인 성공 ");

        // 2. JWT 토큰 생성
        String token = jwtTokenProvider.generateToken(member.getEmail());

        // 3. 프론트엔드 리다이렉트
        String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:3000/oauth/callback")
                .queryParam("token", token)
                .build()
                .toUriString();

        // 4. 리다이렉트 실행
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
