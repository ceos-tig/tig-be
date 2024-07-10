package tig.server.oauth2.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import tig.server.discord.DiscordMessageProvider;
import tig.server.discord.EventMessage;
import tig.server.enums.MemberRoleEnum;
import tig.server.member.domain.Member;
import tig.server.member.repository.MemberRepository;
import tig.server.oauth2.memberInfo.KakaoMemberInfo;
import tig.server.oauth2.memberInfo.OAuth2MemberInfo;
import tig.server.oauth2.PrincipalDetails;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuth2MemberService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    private final DiscordMessageProvider discordMessageProvider;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("getAttributes : {}",oAuth2User.getAttributes());
        OAuth2MemberInfo memberInfo = null;

        String accessToken = userRequest.getAccessToken().getTokenValue();
        System.out.println("카카오 서버의 accessToken = " + accessToken);

        if (userRequest.getClientRegistration().getRegistrationId().equals("kakao")) {
            memberInfo = new KakaoMemberInfo(oAuth2User.getAttributes());
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            //memberInfo = new GoogleMemberInfo(oAuth2User.getAttributes());
        } else {
            System.out.println("로그인 실패");
        }

        System.out.println("memberInfo.getEmail() = " + memberInfo.getEmail());
        System.out.println("memberInfo.getName() = " + memberInfo.getName());
        System.out.println("memberInfo.getProfileImage() = " + memberInfo.getProfileImage());
        System.out.println("memberInfo.getProviderId() = " + memberInfo.getProviderId());

        String provider = memberInfo.getProvider();
        String providerId = memberInfo.getProviderId();
        String uniqueId = provider + "_" + providerId; //중복이 발생하지 않도록 provider와 providerId를 조합 -> DB에 저장해야 할듯?

        Member saveMember = getOrSave(memberInfo,uniqueId);

        String role = "ROLE_USER"; //일반 유저

        // discord-webhook
        discordMessageProvider.sendJoinMessage(EventMessage.SIGN_UP_EVENT);

        return new PrincipalDetails(saveMember, oAuth2User.getAttributes());
    }

    private Member getOrSave(OAuth2MemberInfo oAuth2MemberInfo,String uniqueId) {
        Optional<Member> findMember = memberRepository.findByUniqueId(uniqueId);
        if(findMember.isEmpty()){
            Member member = Member.builder()
                    .name(oAuth2MemberInfo.getName())
                    .email(oAuth2MemberInfo.getEmail())
                    .phoneNumber(null)
                    .uniqueId(uniqueId)
                    .memberRoleEnum(MemberRoleEnum.USER)
                    .profileImage(oAuth2MemberInfo.getProfileImage())
                    .build();
            return memberRepository.save(member);
        } else { // 있다면 ,,,
            return findMember.get();
        }
    }
}