package com.efub.doppelganger.Security.OAuth;

import com.efub.doppelganger.Security.CustomMemberDetails;
import com.efub.doppelganger.member.domain.LoginType;
import com.efub.doppelganger.member.domain.Member;
import com.efub.doppelganger.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuth2MemberService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 1. 부모 클래스의 loadUser로 OAuth2User 정보 가져오기
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 2. 어떤 OAuth2 제공자인지 확인
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // 3. OAuth2User의 attributes (사용자 정보) 가져오기
        Map<String, Object> attributes = oAuth2User.getAttributes();

        if("kakao".equals(registrationId)){
            return processKakaoUser(attributes);
        }
        throw new OAuth2AuthenticationException("지원하지 않는 로그인 방식입니다.");
    }

    private OAuth2User processKakaoUser(Map<String, Object> attributes) {
        // 1. 카카오 사용자 정보 파싱
        KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(attributes);

        System.out.println("카카오 로그인 - ID: " + kakaoUserInfo.getId());
        System.out.println("카카오 로그인 - Email " + kakaoUserInfo.getEmail());
        System.out.println("카카오 로그인 - 닉네임 " + kakaoUserInfo.getNickname());

        // 2. 기존 사용자 조회
        Member member = memberRepository.findByProviderIdAndLoginType(kakaoUserInfo.getId(), LoginType.KAKAO)
                .orElseGet(() -> {
                    // 3. 신규 사용자면 회원가입 처리
                    System.out.println("신규 카카오 사용자 회원가입");

                    //이메일 중복 체크
                    if(memberRepository.findByEmail(kakaoUserInfo.getEmail()).isPresent()) {
                        throw new IllegalArgumentException("이미 해당 이메일로 가입된 계정이 있습니다." + kakaoUserInfo.getEmail());
                    }

                    Member newMember = Member.builder()
                            .email(kakaoUserInfo.getEmail())
                            .nickname(kakaoUserInfo.getNickname())
                            .providerId(kakaoUserInfo.getId())
                            .loginType(LoginType.KAKAO)
                            .password(null)
                            .build();

                    return memberRepository.save(newMember);
                });
        return new CustomMemberDetails(member, attributes);
    }
}
