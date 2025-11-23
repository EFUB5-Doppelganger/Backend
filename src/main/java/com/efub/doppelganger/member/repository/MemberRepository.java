package com.efub.doppelganger.member.repository;

import com.efub.doppelganger.member.domain.LoginType;
import com.efub.doppelganger.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    // 이메일로 사용자 찾기 - 일반 로그인
    Optional<Member> findByEmail(String email);

    // 카카오 ID로 사용자 찾기 - 카카오 로그인
    Optional<Member> findByProviderIdAndLoginType(String providerId, LoginType longinType);
}
