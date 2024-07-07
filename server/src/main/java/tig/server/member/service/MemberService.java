package tig.server.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tig.server.jwt.TokenProvider;
import tig.server.member.domain.Member;
import tig.server.member.dto.MemberDTO;
import tig.server.member.dto.RefreshTokenRequestDto;
import tig.server.member.mapper.MemberMapper;
import tig.server.member.repository.MemberRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    private final MemberMapper memberMapper;

    @Transactional
    public void saveOrUpdateRefreshToken(String uniqueId,String refreshToken) {
        Member member = memberRepository.findByUniqueId(uniqueId).orElseThrow(() -> new RuntimeException(uniqueId + " does not exist"));
        member.updateRefreshToken(refreshToken);
    }

    public String reissueAccessToken(Member member, RefreshTokenRequestDto refreshTokenRequestDto) {
        Authentication authentication = tokenProvider.getAuthentication(refreshTokenRequestDto.getRefreshToken());
        return tokenProvider.createAccessToken(member.getName(), member.getUniqueId(), authentication);
    }

    public List<MemberDTO.Response> getAllMembers() {
        return memberRepository.findAll().stream()
                .map(memberMapper::entityToResponse)
                .collect(Collectors.toList());
    }
    public MemberDTO.Response getMemberById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("member not found"));

        return memberMapper.entityToResponse(member);
    }
}
