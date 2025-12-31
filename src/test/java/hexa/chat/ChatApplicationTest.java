package hexa.chat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;

import static org.junit.jupiter.api.Assertions.*;

class ChatApplicationTest {

    @DisplayName("애플리케이션 실행 테스트")
    @Test
    void run() {
        try (MockedStatic<SpringApplication> mocked = Mockito.mockStatic(SpringApplication.class)){
            ChatApplication.main(new String[0]);

            mocked.verify(() -> SpringApplication.run(ChatApplication.class, new String[0]));
        }
    }
}