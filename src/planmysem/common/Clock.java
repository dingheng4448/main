package planmysem.common;

import java.time.Instant;
import java.time.ZoneId;

/**
 * Utility methods
 */
public class Clock {

    private static java.time.Clock clock = java.time.Clock.systemDefaultZone();

    public static java.time.Clock get() {
        return clock;
    }

    /**
     * Checks whether any of the given items are null.
     */
    public static void set(String dateTime) {
        clock = java.time.Clock.fixed(Instant.parse(dateTime), ZoneId.of("UTC"));
    }
}

