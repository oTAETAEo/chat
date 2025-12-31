package hexa.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hexa.chat.adapter.jpa.chat.chatroommember.ChatRoomMemberRepositoryAdapter;
import hexa.chat.adapter.jpa.chat.chatroom.ChatRoomRepositoryAdapter;
import hexa.chat.adapter.jpa.chat.chatroom.ChatRoomJpaRepository;
import hexa.chat.adapter.jpa.chat.chatroommember.ChatRoomMemberJpaRepository;
import hexa.chat.adapter.jpa.friendship.FriendshipJapRepository;
import hexa.chat.adapter.jpa.friendship.FriendshipRepositoryAdapter;
import hexa.chat.adapter.jpa.member.MemberJpaRepository;
import hexa.chat.adapter.jpa.member.MemberRepositoryAdapter;
import hexa.chat.application.chat.chatroom.required.ChatRoomRepository;
import hexa.chat.application.chat.chatroommember.required.ChatRoomMemberRepository;
import hexa.chat.application.friendship.required.FriendshipRepository;
import hexa.chat.application.member.required.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestQueryDslConfig {

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }

    @Bean
    public FriendshipRepository friendshipRepository(FriendshipJapRepository friendshipJapRepository) {
        return new FriendshipRepositoryAdapter(friendshipJapRepository);
    }

    @Bean
    public MemberRepository memberRepository(MemberJpaRepository memberJpaRepository) {
        return new MemberRepositoryAdapter(memberJpaRepository);
    }

    @Bean
    public ChatRoomRepository chatRoomRepository(ChatRoomJpaRepository chatRoomJpaRepository) {
        return new ChatRoomRepositoryAdapter(chatRoomJpaRepository);
    }

    @Bean
    public ChatRoomMemberRepository chatRoomMemberRepository(ChatRoomMemberJpaRepository chatRoomMemberJpaRepository) {
        return new ChatRoomMemberRepositoryAdapter(chatRoomMemberJpaRepository);
    }
}
