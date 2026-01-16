package hexa.config;

import hexa.chat.application.friendship.FriendshipQueryService;
import hexa.chat.application.member.MemberFinderService;
import hexa.chat.application.member.MemberQueryService;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({
    MemberFinderService.class,
    MemberQueryService.class,

    FriendshipQueryService.class,

    TestQueryDslConfig.class
})
public abstract class QueryTestBase {

    @Autowired
    protected EntityManager em;

    protected void cleanJpaCache() {
        em.flush();
        em.clear();
    }
}
