package hexa.chat.adapter.web.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexa.chat.adapter.security.SecurityConfig;
import hexa.chat.application.auth.dto.LoginRequest;
import hexa.chat.application.auth.dto.LoginResponse;
import hexa.chat.application.auth.provided.LoginUseCase;
import hexa.chat.domain.auth.InvalidCredentialsException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private LoginUseCase loginUseCase;

    @DisplayName("로그인에 성공한다.")
    @Test
    void login() throws Exception {
        // given
        LoginRequest request = new LoginRequest("email", "password");
        LoginResponse loginResponse = new LoginResponse(
            "ACCESS_TOKEN",
            "REFRESH_TOKEN",
            "NEW_DEVICE_ID"
        );
        given(loginUseCase.login(any(LoginRequest.class), isNull()))
            .willReturn(loginResponse);

        // when
        ResultActions result = mockMvc.perform(
        post("/login")
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
        LoginRequest request = new LoginRequest("email", "password");

        given(loginUseCase.login(any(LoginRequest.class), isNull()))
            .willThrow(new InvalidCredentialsException("이메일 또는 비밀번호가 올바르지 않습니다."));

        // when
        ResultActions result = mockMvc.perform(
            post("/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        );

        result.andExpect(status().isUnauthorized());
    }


}
