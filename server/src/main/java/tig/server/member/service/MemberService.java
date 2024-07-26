package tig.server.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tig.server.enums.MemberRoleEnum;
import tig.server.global.exception.BusinessExceptionHandler;
import tig.server.global.code.ErrorCode;
import tig.server.jwt.TokenProvider;
import tig.server.oauth2.google.dto.GoogleInfoResponseDto;
import tig.server.oauth2.kakao.dto.KakaoUserInfoResponseDto;
import tig.server.oauth2.kakao.dto.LoginMemberResponseDto;
import tig.server.member.domain.Member;
import tig.server.member.dto.MemberResponse;
import tig.server.member.dto.RefreshTokenResponseDto;
import tig.server.member.mapper.MemberMapper;
import tig.server.member.repository.MemberRepository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final RedisTemplate<String, Object> redisTemplateRT;

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    private final MemberMapper memberMapper;

    @Transactional
    public void saveOrUpdateRefreshToken(String uniqueId,String refreshToken) {
        Member member = memberRepository.findByUniqueId(uniqueId).orElseThrow(() -> new RuntimeException(uniqueId + " does not exist"));
        member.updateRefreshToken(refreshToken);
    }

    public Member getMemberByUniqueId(String uniqueId) {
        return memberRepository.findByUniqueId(uniqueId)
                .orElseThrow(() -> new BusinessExceptionHandler("member not found by uniqueId", ErrorCode.NOT_FOUND_ERROR));
    }

    @Transactional
    public RefreshTokenResponseDto reissueAccessToken(String refreshToken) {
        String key = "blacklist:" + refreshToken;
        if (redisTemplateRT.hasKey(key)) {
            throw new BusinessExceptionHandler("!!!Refresh Token Blacklisted!!!",ErrorCode.FORBIDDEN_ERROR);
        } else {
            String uniqueId = tokenProvider.getUniqueId(refreshToken);
            Member member = getMemberByUniqueId(uniqueId);

            String accessToken = tokenProvider.createAccessToken(member.getName(), member.getUniqueId());
            String newRefreshToken = tokenProvider.createRefreshToken(member.getName(), member.getUniqueId());
            member.updateRefreshToken(newRefreshToken);

            // 새로운 액세스 토큰으로 인증 객체 생성 및 설정
            Authentication authentication = tokenProvider.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return RefreshTokenResponseDto.fromRefreshToken(accessToken, newRefreshToken);
        }

    }

    public List<MemberResponse> getAllMembers() {
        return memberRepository.findAll().stream()
                .map(memberMapper::entityToResponse)
                .collect(Collectors.toList());
    }
    public MemberResponse getMemberById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new BusinessExceptionHandler("member not found",ErrorCode.NOT_FOUND_ERROR));

        return memberMapper.entityToResponse(member);
    }

    @Transactional
    public LoginMemberResponseDto createKakaoMember(KakaoUserInfoResponseDto userInfoResponseDto) {
        String username = userInfoResponseDto.kakaoAccount.profile.nickName;
        String uniqueId = "kakao_" + userInfoResponseDto.id;

        String accessToken = tokenProvider.createAccessToken(username, uniqueId);
        String refreshToken = tokenProvider.createRefreshToken(username, uniqueId);

        Optional<Member> findMember = memberRepository.findByUniqueId(uniqueId);
        if (findMember.isPresent()) { // 있는 사용자
            Member existMember = findMember.get();
            existMember.updateRefreshToken(refreshToken);
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
    public LoginMemberResponseDto createGoogleMember(GoogleInfoResponseDto googleInfoResponseDto) {
        String username = googleInfoResponseDto.getName();
        String uniqueId = "google_" + googleInfoResponseDto.getSub();

        String accessToken = tokenProvider.createAccessToken(username, uniqueId);
        String refreshToken = tokenProvider.createRefreshToken(username, uniqueId);

        Optional<Member> findMember = memberRepository.findByUniqueId(uniqueId);
        if (findMember.isPresent()) { // 있는 사용자
            Member existMember = findMember.get();
            existMember.updateRefreshToken(refreshToken);
            return LoginMemberResponseDto.fromMember(existMember, accessToken);
        } else {
            Member member = Member.builder()
                    .memberRoleEnum(MemberRoleEnum.USER)
                    .name(googleInfoResponseDto.getName())
                    .email(googleInfoResponseDto.getEmail())
                    .uniqueId("google_" + googleInfoResponseDto.getSub())
                    .refreshToken(refreshToken)
                    .build();

            memberRepository.save(member);
            return LoginMemberResponseDto.fromMember(member, accessToken);
        }
    }

    @Transactional
    public MemberResponse changeName(Long memberId, String newName) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessExceptionHandler("member not found", ErrorCode.NOT_FOUND_ERROR));

        member.updateName(newName);
        return memberMapper.entityToResponse(member);
    }

    @Transactional
    public MemberResponse changePhoneNumber(Long memberId, String newPhoneNumber) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessExceptionHandler("member not found", ErrorCode.NOT_FOUND_ERROR));

        member.updatePhoneNumber(newPhoneNumber);
        return memberMapper.entityToResponse(member);
    }

    @Transactional
    public MemberResponse changeEmail(Long memberId, String newEmail) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessExceptionHandler("member not found", ErrorCode.NOT_FOUND_ERROR));

        member.updateEmail(newEmail);
        return memberMapper.entityToResponse(member);
    }

    @Transactional
    public void logout(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessExceptionHandler("Member not found", ErrorCode.NOT_FOUND_ERROR));

        // 기존 리프레시 토큰을 블랙리스트에 추가
        String key = "blacklist:" + member.getRefreshToken();
        redisTemplateRT.opsForValue().set(key, "blacklisted", tokenProvider.getRefreshTokenExpiration(member.getRefreshToken()), TimeUnit.MILLISECONDS);

        member.updateRefreshToken(null);
    }
}
