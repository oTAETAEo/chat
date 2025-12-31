package hexa.chat.application.chat.chatroommember;

import hexa.chat.application.chat.chatroom.provided.ChatRoomFinder;
import hexa.chat.application.chat.chatroommember.provided.ChatRoomMemberRegister;
import hexa.chat.application.chat.chatroommember.required.ChatRoomMemberRepository;
import hexa.chat.application.member.provided.MemberFinder;
import hexa.chat.domain.chat.chatroom.ChatRoom;
import hexa.chat.domain.chat.chatroommember.ChatRoomMember;
import hexa.chat.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatRoomMemberRegisterService implements ChatRoomMemberRegister {

    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final MemberFinder memberFinder;
    private final ChatRoomFinder chatRoomFinder;

    @Override
    public List<ChatRoomMember> register(Long chatRoomId, List<UUID> memberIds) {

        List<Member> members = memberFinder.findAllByPublicId(memberIds);
        ChatRoom chatRoom = chatRoomFinder.find(chatRoomId);

        List<ChatRoomMember> chatRoomMembers = members.stream()
            .map((m) -> ChatRoomMember.register(m, chatRoom))
            .toList();

        return chatRoomMemberRepository.saveAll(chatRoomMembers);
    }


}
