package hexa.chat.adapter.web.auth;

import hexa.chat.adapter.security.JwtAuthenticationFilter;
import hexa.chat.adapter.security.SecurityConfig;
import hexa.chat.adapter.web.ApiControllerAdvice;
import hexa.chat.application.auth.provided.LoginUseCase;
import hexa.chat.application.auth.provided.SignUpUseCase;
import hexa.chat.application.auth.required.TokenProvider;
import hexa.chat.application.friendship.dto.FriendshipInfoResponse;
import hexa.chat.application.friendship.dto.FriendshipResponse;
import hexa.chat.application.friendship.provided.FriendshipQuery;
import hexa.chat.application.member.dto.MemberInfoPublicResponse;
import hexa.chat.application.member.provided.MemberFinder;
import hexa.chat.application.member.provided.MemberQuery;
import hexa.chat.domain.friendship.Friendship;
import hexa.chat.domain.member.Member;
import hexa.chat.domain.member.MemberFixture;
import hexa.chat.domain.member.MemberRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockCookie;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class, ApiControllerAdvice.class})
class AuthControllerSecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LoginUseCase loginUseCase;

    @MockitoBean
    private SignUpUseCase signUpUseCase;

    @MockitoBean
    private FriendshipQuery friendshipQuery;

    @MockitoBean
    private MemberQuery memberQuery;

    @MockitoBean
    private TokenProvider tokenProvider;

    @MockitoBean
    private MemberFinder memberFinder;

    @DisplayName("토큰이 없으면 보호된 엔드포인트 접근이 거부된다")
    @Test
    void authInfo_shouldRejectWithoutToken() throws Exception {
        mockMvc.perform(get("/auth/info"))
            .andExpect(status().isUnauthorized());
    }

    @DisplayName("유효한 토큰이면 필터가 인증을 처리한다")
    @Test
    void authInfo_shouldAllowWithToken() throws Exception {
        Member currentMember = MemberFixture.createMember(1L, "han@han.com", "han", "_han_");
        Member to = MemberFixture.createMember(2L, "kim@kim.com", "kim", "_kim_");

        Friendship friendship = Friendship.register(currentMember, to);
        FriendshipResponse response = FriendshipResponse.ofFriend(friendship, currentMember.getId());
        FriendshipInfoResponse friendshipInfo = new FriendshipInfoResponse(
            List.of(response),
            List.of(),
            List.of()
        );

        MemberInfoPublicResponse memberPublicInfo = MemberInfoPublicResponse.of(currentMember);

        given(tokenProvider.parseAccessToken("ACCESS_TOKEN"))
            .willReturn(Optional.of(new TokenProvider.AccessTokenPayload(
                currentMember.getPublicId(),
                MemberRole.USER
            )));
        given(memberFinder.findByPublicId(currentMember.getPublicId()))
            .willReturn(currentMember);
        given(friendshipQuery.memberFriendshipInfo(currentMember.getId()))
            .willReturn(friendshipInfo);
        given(memberQuery.memberPublicInfo(currentMember.getId()))
            .willReturn(memberPublicInfo);

        mockMvc.perform(get("/auth/info")
                .cookie(new MockCookie("ACCESS_TOKEN", "ACCESS_TOKEN")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.memberInfoPublicResponse.publicId")
                .value(memberPublicInfo.publicId().toString()));
    }
}
