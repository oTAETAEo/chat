package hexa.chat.adapter.security.oauth2;

import hexa.chat.adapter.security.oauth2.strategy.SocialUserConverterComposite;
import hexa.chat.application.member.required.MemberRepository;
import hexa.chat.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final SocialUserConverterComposite socialUserConverterComposite;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User auth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2Response oAuth2Response = socialUserConverterComposite.convert(registrationId, auth2User.getAttributes());

        String providerId = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();

        Optional<Member> existData = memberRepository.findByProviderId(providerId);

        if (existData.isEmpty()) {
            Member member = Member.oAuth2register(oAuth2Response.getName(), oAuth2Response.getEmail(), providerId, oAuth2Response.getProvider());

            Member save = memberRepository.save(member);

            MemberDto memberDto = new MemberDto(save.getPublicId(), save.getRole());

            return new CustomOAuth2User(memberDto);

        } else {
            Member member = existData.get();

            MemberDto memberDto = new MemberDto(member.getPublicId(), member.getRole());

            return new CustomOAuth2User(memberDto);
        }
    }

}