package com.efub.doppelganger.util;

import com.efub.doppelganger.Security.CustomMemberDetails;
import com.efub.doppelganger.member.domain.Member;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentMemberUtil {
    public CustomMemberDetails getCurrentUser() {
        Object principal = SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        if(principal instanceof CustomMemberDetails) {
            return (CustomMemberDetails) principal;
        }
        return null;
    }

    public Member getCurrentMember() {
        CustomMemberDetails member = getCurrentUser();
        return (member != null)? member.getMember() : null;
    }
}
