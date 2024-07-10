package tig.server.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tig.server.member.domain.Member;
import tig.server.member.domain.MemberDetailsImpl;
import tig.server.member.repository.MemberRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberDetailsServiceImpl implements UserDetailsService {		// 1.
    private final MemberRepository memberRepository;

    // DB 에 저장된 사용자 정보와 일치하는지 여부를 판단
    @Override
    public UserDetails loadUserByUsername(String uniqueId) throws UsernameNotFoundException {
        Member member = memberRepository.findByUniqueId(uniqueId).orElseThrow(
                () -> new UsernameNotFoundException("Not Found " + uniqueId));

        return new MemberDetailsImpl(member);
    }

    public Optional<Member> getCurrentAuthenticatedMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            String memberName = authentication.getName();
            return memberRepository.findByName(memberName);
        }
        throw new UsernameNotFoundException("인증된 사용자를 찾을 수 없습니다.");
    }
}
