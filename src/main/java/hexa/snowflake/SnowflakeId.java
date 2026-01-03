package hexa.snowflake;

import org.hibernate.annotations.IdGeneratorType;

import java.lang.annotation.*;

@IdGeneratorType(EntityIdentifierGenerator.class)
@Target(value = {ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SnowflakeId {
}
