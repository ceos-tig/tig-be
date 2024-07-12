package tig.server.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tig.server.enums.MemberRoleEnum;
import tig.server.error.BusinessExceptionHandler;
import tig.server.error.ErrorCode;
import tig.server.jwt.TokenProvider;
import tig.server.kakao.dto.KakaoUserInfoResponseDto;
import tig.server.kakao.dto.LoginMemberResponseDto;
import tig.server.member.domain.Member;
import tig.server.member.dto.MemberResponse;
import tig.server.member.dto.RefreshTokenRequestDto;
import tig.server.member.mapper.MemberMapper;
import tig.server.member.repository.MemberRepository;

import java.util.List;
import java.util.Optional;
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
        return tokenProvider.createAccessToken(member.getName(), member.getUniqueId());
    }

    public List<MemberResponse> getAllMembers() {
        return memberRepository.findAll().stream()
                .map(memberMapper::entityToResponse)
                .collect(Collectors.toList());
    }
    public MemberResponse getMemberById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("member not found"));

        return memberMapper.entityToResponse(member);
    }

    @Transactional
    public LoginMemberResponseDto createMember(KakaoUserInfoResponseDto userInfoResponseDto) {
        String username = userInfoResponseDto.kakaoAccount.profile.nickName;
        String uniqueId = "kakao_" + userInfoResponseDto.id;

        String accessToken = tokenProvider.createAccessToken(username, uniqueId);
        String refreshToken = tokenProvider.createRefreshToken(username, uniqueId);

        Optional<Member> findMember = memberRepository.findByUniqueId(uniqueId);
        if (findMember.isPresent()) { // 있는 사용자
            Member existMember = findMember.get();
            return LoginMemberResponseDto.fromMember(existMember, accessToken);
        } else {
            Member member = Member.builder()
                    .memberRoleEnum(MemberRoleEnum.USER)
                    .name(userInfoResponseDto.kakaoAccount.profile.nickName)
                    .email(userInfoResponseDto.kakaoAccount.email)
                    .uniqueId("kakao_" + userInfoResponseDto.id)
                    .profileImage(userInfoResponseDto.kakaoAccount.profile.profileImageUrl)
                    .refreshToken(refreshToken)
                    .build();

            memberRepository.save(member);
            return LoginMemberResponseDto.fromMember(member, accessToken);
        }
    }

    @Transactional
    public MemberDTO.Response changeName(Long memberId, String newName) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessExceptionHandler("member not found", ErrorCode.BAD_REQUEST_ERROR));

        member.updateName(newName);
        return memberMapper.entityToResponse(member);
    }

    @Transactional
    public MemberDTO.Response changePhoneNumber(Long memberId, String newPhoneNumber) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessExceptionHandler("member not found", ErrorCode.BAD_REQUEST_ERROR));

        member.updatePhoneNumber(newPhoneNumber);
        return memberMapper.entityToResponse(member);
    }

    @Transactional
    public MemberDTO.Response changeEmail(Long memberId, String newEmail) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessExceptionHandler("member not found", ErrorCode.BAD_REQUEST_ERROR));

        member.updateEmail(newEmail);
        return memberMapper.entityToResponse(member);
    }
}
