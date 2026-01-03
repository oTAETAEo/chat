package hexa.snowflake;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class EntityIdentifierGenerator implements IdentifierGenerator {

    private final Snowflake snowflake = new Snowflake();

    @Override
    public Object generate(SharedSessionContractImplementor session, Object entity) {
        return snowflake.nextId();
    }
}
