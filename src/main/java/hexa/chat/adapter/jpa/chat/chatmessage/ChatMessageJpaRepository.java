package hexa.chat.adapter.jpa.chat.chatmessage;

import hexa.chat.domain.chat.chatmessage.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageJpaRepository extends JpaRepository<ChatMessage, Long>, ChatMessageRepositoryCustom{

}
