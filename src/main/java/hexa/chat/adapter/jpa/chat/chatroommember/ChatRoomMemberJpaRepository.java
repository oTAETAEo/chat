package hexa.chat.adapter.jpa.chat.chatroommember;

import hexa.chat.domain.chat.chatroommember.ChatRoomMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomMemberJpaRepository extends JpaRepository<ChatRoomMember, Long>, ChatRoomMemberRepositoryCustom{

    List<ChatRoomMember> findAllByChatRoom_Id(Long chatRoomId);
}
