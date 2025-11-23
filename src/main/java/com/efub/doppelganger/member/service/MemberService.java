package com.efub.doppelganger.member.service;

import com.efub.doppelganger.member.domain.Member;
import com.efub.doppelganger.member.dto.request.UpdateMemberRequest;
import com.efub.doppelganger.member.dto.response.MemberResponse;
import com.efub.doppelganger.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    //프로필 수정
    @Transactional
    public MemberResponse updateMember(Long memberId, UpdateMemberRequest request) {
        //1. 회원 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        //2. 닉네임 수정
        if(request.getNickname() != null && !request.getNickname().trim().isEmpty()) {
            member.setNickname(request.getNickname());
        }

        //3. bio 수정
        if(request.getBio() != null) {
            member.setBio(request.getBio());
        }

        //4. 출생년도 수정
        if(request.getBornYear() != null) {
            member.setBornYear(request.getBornYear());
        }

        //5. 직업 수정
        if(request.getJob() != null) {
            member.setJob(request.getJob());
        }
        Member updatedMember = memberRepository.save(member);
        return new MemberResponse(updatedMember);
    }
}
