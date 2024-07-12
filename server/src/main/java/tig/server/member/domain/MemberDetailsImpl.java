package tig.server.member.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import tig.server.enums.MemberRoleEnum;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@RequiredArgsConstructor
public class MemberDetailsImpl implements UserDetails {
    private final Member member;        // 1.

    public Member getUser() {         // 2.
        return member;
    }

    @Override
    public String getPassword() {
        //return member.getPassword();
        return "abc";
    }

    @Override
    public String getUsername() {
        return member.getName();
    }

    // 사용자의 권한을 GrantedAuthority 로 추상화 및 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {		// 3.
        MemberRoleEnum role = member.getMemberRoleEnum();
        String authority = role.getAuthority();

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(simpleGrantedAuthority);

        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}