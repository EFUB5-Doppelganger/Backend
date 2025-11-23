package com.efub.doppelganger.member.controller;

import com.efub.doppelganger.Security.CustomMemberDetails;
import com.efub.doppelganger.member.domain.Member;
import com.efub.doppelganger.member.dto.request.UpdateMemberRequest;
import com.efub.doppelganger.member.dto.response.MemberResponse;
import com.efub.doppelganger.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    // 현재 로그인한 사용자 정보 조회
    @GetMapping("/profile")
    public ResponseEntity<MemberResponse> getCurrentMember(
            @AuthenticationPrincipal CustomMemberDetails memberDetails) {

        Member member = memberDetails.getMember();
        MemberResponse response = new MemberResponse(member);

        return ResponseEntity.ok(response);
    }

    // 현재 로그인한 사용자 정보 수정
    @PatchMapping("/profile")
    public ResponseEntity<MemberResponse> updateMember(
            @AuthenticationPrincipal CustomMemberDetails memberDetails,
            @RequestBody UpdateMemberRequest request) {
        Member member = memberDetails.getMember();
        MemberResponse response = memberService.updateMember(member.getId(), request);
        return ResponseEntity.ok(response);
    }
}
