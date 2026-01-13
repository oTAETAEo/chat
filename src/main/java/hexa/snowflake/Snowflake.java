package hexa.snowflake;

import java.io.Serial;
import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;

public final class Snowflake implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final int UNUSED_BITS = 1;
    private static final int EPOCH_BITS = 41;
    private static final int NODE_ID_BITS = 10;
    private static final int SEQUENCE_BITS = 12;

    private static final long MAX_NODE_ID = (1L << NODE_ID_BITS) - 1;
    private static final long MAX_SEQUENCE = (1L << SEQUENCE_BITS) - 1;

    // UTC = 2024-01-01T00:00:00Z
    private static final long START_TIME_MILLIS = 1704067200000L;

    private final long nodeId = ThreadLocalRandom.current().nextLong(MAX_NODE_ID + 1);

    private long lastTimeMillis = START_TIME_MILLIS;
    private long sequence = 0L;

    public synchronized long nextId() {
        long currentTimeMillis = System.currentTimeMillis();

        if (currentTimeMillis < lastTimeMillis) {
            throw new IllegalStateException("Invalid Time");
        }

        if (currentTimeMillis == lastTimeMillis) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            if (sequence == 0) {
                currentTimeMillis = waitNextMillis(currentTimeMillis);
            }
        } else {
            sequence = 0;
        }

        lastTimeMillis = currentTimeMillis;

        return ((currentTimeMillis - START_TIME_MILLIS) << (NODE_ID_BITS + SEQUENCE_BITS))
            | (nodeId << SEQUENCE_BITS)
            | sequence;
    }

    private long waitNextMillis(long currentTimestamp) {
        while (currentTimestamp <= lastTimeMillis) {
            currentTimestamp = System.currentTimeMillis();
        }
        return currentTimestamp;
    }
}
