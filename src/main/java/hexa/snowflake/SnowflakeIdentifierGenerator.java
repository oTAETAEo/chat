package hexa.snowflake;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serial;

public class SnowflakeIdentifierGenerator implements IdentifierGenerator {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Snowflake snowflake = new Snowflake();

    @Override
    public Object generate(SharedSessionContractImplementor session, Object entity) {
        return snowflake.nextId();
    }
}
