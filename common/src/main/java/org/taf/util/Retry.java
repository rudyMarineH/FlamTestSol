package org.taf.util;

import lombok.experimental.UtilityClass;
import org.awaitility.core.ConditionTimeoutException;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.awaitility.Awaitility.await;

@UtilityClass
public class Retry {

    private static boolean enabled = true;

    static void configure(boolean enabled) {
        Retry.enabled = enabled;
    }

    public static <T> T until(Supplier<T> action, Predicate<T> condition) {
        if (!enabled) return action.get();
        AtomicReference<T> ref = new AtomicReference<>();
        try {
            await()
                .atMost(Duration.ofSeconds(6))
                .pollInterval(Duration.ofSeconds(2))
                .pollInSameThread()
                .until(() -> {
                    ref.set(action.get());
                    return condition.test(ref.get());
                });
        } catch (ConditionTimeoutException ignored) {}
        return ref.get();
    }
}
