package com.efub.doppelganger.Security.JWT;

import com.efub.doppelganger.Security.CustomMemberDetails;
import com.efub.doppelganger.Security.CustomMemberDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomMemberDetailsService customMemberDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1. 요청 헤더에서 JWT 토큰 추출
        String token = jwtTokenProvider.resolveToken(request);

        // 2. 토큰이 있고 유효한지 검증
        if(token != null && jwtTokenProvider.validateToken(token)) {
            // 3. 토큰에서 사용자 이메일 추출
            String email = jwtTokenProvider.getEmail(token);

            //4. 이메일로 사용자 정보 조회
            UserDetails memberDetail = customMemberDetailsService.loadUserByUsername(email);

            //5. 인증 객체 생성
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(memberDetail, null, memberDetail.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            //6. SecurityContext에 인증 정보 저장
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        //7. 다음 필터로 진행
        filterChain.doFilter(request, response);
    }
}
