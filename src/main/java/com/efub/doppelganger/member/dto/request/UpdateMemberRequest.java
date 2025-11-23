package com.efub.doppelganger.member.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateMemberRequest {
    String nickname;
    String bio;
    String bornYear;
    String job;
}
