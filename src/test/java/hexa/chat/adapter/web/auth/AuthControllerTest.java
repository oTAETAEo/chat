package hexa.chat.adapter.web.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexa.chat.adapter.security.JwtAuthenticationFilter;
import hexa.chat.adapter.security.MemberPrincipal;
import hexa.chat.adapter.web.ApiControllerAdvice;
import hexa.chat.application.auth.dto.*;
import hexa.chat.application.auth.provided.LogOutUseCase;
import hexa.chat.application.auth.provided.LoginUseCase;
import hexa.chat.application.auth.provided.SignUpFixture;
import hexa.chat.application.auth.provided.SignUpUseCase;
import hexa.chat.application.friendship.dto.FriendshipInfoResponse;
import hexa.chat.application.friendship.dto.FriendshipResponse;
import hexa.chat.application.friendship.provided.FriendshipQuery;
import hexa.chat.application.member.dto.MemberInfoPublicResponse;
import hexa.chat.application.member.provided.MemberQuery;
import hexa.chat.domain.auth.InvalidCredentialsException;
import hexa.chat.domain.friendship.Friendship;
import hexa.chat.domain.member.Member;
import hexa.chat.domain.member.MemberFixture;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest(
    controllers = AuthController.class,
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = JwtAuthenticationFilter.class
    )
)
@AutoConfigureMockMvc(addFilters = false)
@Import(ApiControllerAdvice.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private LoginUseCase loginUseCase;

    @MockitoBean
    private LogOutUseCase logOutUseCase;

    @MockitoBean
    private SignUpUseCase signUpUseCase;

    @MockitoBean
    private FriendshipQuery friendshipQuery;

    @MockitoBean
    private MemberQuery memberQuery;

    @DisplayName("로그인에 성공한다.")
    @Test
    void login() throws Exception {
        // given
        LoginRequest request = new LoginRequest("test@test.com", "testtest1234");
        LoginResponse loginResponse = new LoginResponse(
            "ACCESS_TOKEN",
            "REFRESH_TOKEN",
            "NEW_DEVICE_ID"
        );
        given(loginUseCase.login(any(LoginRequest.class), isNull()))
            .willReturn(loginResponse);

        // when
        ResultActions result = mockMvc.perform(
            post("/api/auth/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        );

        // then
        result.andExpect(status().isOk())
            .andExpect(res -> {
                List<String> setCookies = res.getResponse().getHeaders(HttpHeaders.SET_COOKIE);
                String all = String.join("\n", setCookies);

                assertThat(all).contains("ACCESS_TOKEN");
                assertThat(all).contains("REFRESH_TOKEN");
                assertThat(all).contains("DEVICE_ID");
            });
    }

    @DisplayName("인증 정보 불일치로 로그인에 실패한다.")
    @Test
    void loginFail() throws Exception {
        // given
        LoginRequest request = new LoginRequest("test@test.com", "testtest1234");

        given(loginUseCase.login(any(LoginRequest.class), isNull()))
            .willThrow(new InvalidCredentialsException("이메일 또는 비밀번호가 올바르지 않습니다."));

        // when
        ResultActions result = mockMvc.perform(
            post("/api/auth/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        );

        result.andExpect(status().isUnauthorized());
    }

    @DisplayName("이메일 비밀번호 입력 형식에 맞지 않아 로그인에 실패한다.")
    @Test
    void login_fail_when_email_or_password_invalid_format() throws Exception {
        // given
        LoginRequest request = new LoginRequest("no email format", "test");

        // when
        ResultActions result = mockMvc.perform(
            post("/api/auth/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        );

        // then
        result.andExpect(status().isBadRequest())
            .andExpect(
                jsonPath("$.errors[*].field").value(containsInAnyOrder("email"))
            );
    }

    @DisplayName("로그인 회원의 정보를 조회한다")
    @Test
    @WithMockUser
    void authInfo_shouldReturnFriendshipInfo() throws Exception {
        // given
        Member currentMember = MemberFixture.createMember(1L, "han@han.com", "han", "_han_");

        Member to = MemberFixture.createMember(2L, "kim@kim.com", "kim", "_kim_");

        Friendship friendship = Friendship.register(currentMember, to);
        FriendshipResponse response = FriendshipResponse.ofFriend(friendship, currentMember.getId());

        FriendshipInfoResponse friendshipInfo = new FriendshipInfoResponse(List.of(response), List.of(), List.of());
        MemberInfoPublicResponse memberPublicInfo = MemberInfoPublicResponse.of(currentMember);

        given(friendshipQuery.memberFriendshipInfo(currentMember.getId()))
            .willReturn(friendshipInfo);

        given(memberQuery.memberPublicInfo(currentMember.getId()))
            .willReturn(memberPublicInfo);

        // when & then
        mockMvc.perform(get("/api/auth/info")
                .with(memberPrincipalProcessor(currentMember.getId())))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.memberInfoPublicResponse.publicId")
                .value(memberPublicInfo.publicId().toString()))
            .andExpect(jsonPath("$.memberInfoPublicResponse.email")
                .value(memberPublicInfo.email()))
            .andExpect(jsonPath("$.memberInfoPublicResponse.name")
                .value(memberPublicInfo.name()))
            .andExpect(jsonPath("$.memberInfoPublicResponse.nickname")
                .value(memberPublicInfo.nickname()))
            .andExpect(jsonPath("$.friendshipInfoResponse.friendships").isArray())
            .andExpect(jsonPath("$.friendshipInfoResponse.senderPendingFriendships").isArray())
            .andExpect(jsonPath("$.friendshipInfoResponse.receivePendingFriendships").isArray());

    }

    @DisplayName("회원가입에 성공한다")
    @Test
    void signUp_success() throws Exception {

        // given
        SignUpRequest signUpRequest = SignUpFixture.createSignUpRequest();

        SignUpResponse response = new SignUpResponse("test 님 가입을 환영합니다.");

        given(signUpUseCase.signUp(any(SignUpRequest.class)))
            .willReturn(response);

        // when - then
        ResultActions perform = mockMvc.perform(post("/api/auth/sign-up")
            .with(csrf())
            .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
            .content(objectMapper.writeValueAsString(signUpRequest))
        );

        perform
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.welcomeMessage")
                .value("test 님 가입을 환영합니다."));

    }

    @DisplayName("로그아웃에 성공한다.")
    @Test
    @WithMockUser
    void logOut_success() throws Exception {
        // given
        Long memberId = 1L;
        String deviceId = "test-device-id";

        // when
        ResultActions result = mockMvc.perform(
            post("/api/auth/logout")
                .with(memberPrincipalProcessor(memberId))
                .with(csrf())
                .cookie(new jakarta.servlet.http.Cookie("DEVICE_ID", deviceId))
        );

        // then
        result.andExpect(status().isOk());
    }

    @DisplayName("이메일 중복 확인에 성공한다.")
    @Test
    void checkEmail_success() throws Exception {
        // given
        EmailCheckRequest request = new EmailCheckRequest("newemail@test.com");
        EmailCheckResponse response = EmailCheckResponse.of(false);

        given(signUpUseCase.checkEmail(request))
            .willReturn(response);

        // when
        ResultActions result = mockMvc.perform(
            post("/api/auth/check-email")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        );

        // then
        result.andExpect(status().isOk())
            .andExpect(jsonPath("$.result").value(true));
    }

    @DisplayName("이미 존재하는 이메일이므로 중복 확인에 실패한다.")
    @Test
    void checkEmail_fail_when_email_already_exists() throws Exception {
        // given
        EmailCheckRequest request = new EmailCheckRequest("existing@test.com");
        EmailCheckResponse response = EmailCheckResponse.of(true);

        given(signUpUseCase.checkEmail(request))
            .willReturn(response);

        // when
        ResultActions result = mockMvc.perform(
            post("/api/auth/check-email")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        );

        // then
        result.andExpect(status().isOk())
            .andExpect(jsonPath("$.result").value(false));
    }

    @DisplayName("닉네임 중복 확인에 성공한다.")
    @Test
    void checkName_success() throws Exception {
        // given
        NameCheckRequest request = new NameCheckRequest("newname");
        NameCheckResponse response = NameCheckResponse.available("사용 가능한 이름 입니다.");

        given(signUpUseCase.checkName(request))
            .willReturn(response);

        // when
        ResultActions result = mockMvc.perform(
            post("/api/auth/check-name")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        );

        // then
        result.andExpect(status().isOk())
            .andExpect(jsonPath("$.result").value(true));
    }

    @DisplayName("이미 존재하는 닉네임이므로 중복 확인에 실패한다.")
    @Test
    void checkName_fail_when_name_already_exists() throws Exception {
        // given
        NameCheckRequest request = new NameCheckRequest("existingname");
        NameCheckResponse response = NameCheckResponse.unavailable("사용 불가능한 이름 입니다.");

        given(signUpUseCase.checkName(request))
            .willReturn(response);

        // when
        ResultActions result = mockMvc.perform(
            post("/api/auth/check-name")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        );

        // then
        result.andExpect(status().isOk())
            .andExpect(jsonPath("$.result").value(false));
    }
    private RequestPostProcessor memberPrincipalProcessor(Long memberId) {
        return request -> {
            MemberPrincipal principal = new MemberPrincipal(memberId, List.of());
            Authentication auth = new UsernamePasswordAuthenticationToken(
                principal, null, principal.authorities()
            );
            SecurityContextHolder.getContext().setAuthentication(auth);
            return request;
        };
    }
}
