package com.efub.doppelganger.Security.OAuth;

import lombok.Getter;

import java.util.Map;

@Getter
public class KakaoUserInfo {
    private String id;
    private String email;
    private String nickname;

    public KakaoUserInfo(Map<String, Object> attributes) {
        this.id = String.valueOf(attributes.get("id"));

        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");

        if(kakaoAccount != null) {
            this.email = (String) kakaoAccount.get("email");

            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
            if(profile != null) {
                this.nickname = (String) profile.get("nickname");
            }
        }
    }
}
