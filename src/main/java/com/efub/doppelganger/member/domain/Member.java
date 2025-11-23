package com.efub.doppelganger.member.domain;

import com.efub.doppelganger.accommodation.domain.Accommodation;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_id")
    private Long id;

    @Column(unique = true)
    private String email;    //사용자 이메일 (로그인 ID)

    @Column
    private String password; //비밀번호

    @Column
    private String nickname;     //이름

    @Enumerated(EnumType.STRING)
    private LoginType loginType;    //로그인 타입

    @Column(length = 50)
    private String providerId;      //카카오에서 제공하는 고유 ID

    @Column(length = 500)
    private String bio;     //자기소개

    @Column
    private String bornYear;    //태어난 연도

    @Column(length = 100)
    private String job;     //직업

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "host", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Accommodation> accommodationList = new ArrayList<>();

    @Builder
    public Member(String email, String password, String nickname, LoginType loginType, String providerId, String bio, String bornYear, String job) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.loginType = loginType;
        this.providerId = providerId;
        this.bio = bio;
        this.bornYear = bornYear;
        this.job = job;
    }
}
