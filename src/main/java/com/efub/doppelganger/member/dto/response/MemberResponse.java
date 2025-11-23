package com.efub.doppelganger.member.dto.response;

import com.efub.doppelganger.member.domain.LoginType;
import com.efub.doppelganger.member.domain.Member;
import lombok.Getter;

@Getter
public class MemberResponse {
    private Long id;
    private String email;
    private String nickname;
    private LoginType loginType;
    private String bio;
    private String bornYear;
    private String job;

    public MemberResponse(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.nickname = member.getNickname();
        this.loginType = member.getLoginType();
        this.bio = member.getBio();
        this.bornYear = member.getBornYear();
        this.job = member.getJob();
    }
}
