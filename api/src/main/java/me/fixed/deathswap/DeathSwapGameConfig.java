package me.fixed.deathswap;

import lombok.Builder;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Builder @Getter public class DeathSwapGameConfig {
    public final static long TICKS_PER_SECOND = 20;
    public final static long DEFAULT_TIME_BETWEEN_SWAP = TICKS_PER_SECOND * 60 * 5;
    public final static String TIME_PLACEHOLDER = "{time}";

    @NotNull @Builder.Default private final List<Long> tickSwitchNotifications = Collections.emptyList();
    @Builder.Default private final long ticksBetweenSwap = DEFAULT_TIME_BETWEEN_SWAP;
    @NotNull @Builder.Default private final String timeNotifyMessageFormat = "";
    @NotNull @Builder.Default private final String swapNotifyMessage = "";
    @NotNull @Builder.Default private final String endTimeNotifyMessageFormat = "";
    @NotNull @Builder.Default private final String gameEndNotifyMessage = "";
    @Builder.Default private final int maxSwapTimes = 1;

    public static List<Long> generateList(Number number) {
        return Collections.singletonList(number.longValue() * TICKS_PER_SECOND);
    }

    public static List<Long> generateList(Number... numbers) {
        return generateList(Arrays.asList(numbers));
    }

    public static List<Long> generateList(List<Number> numbers) {
        return numbers.stream()
                .map(Number::longValue)
                .map(l -> l * TICKS_PER_SECOND)
                .sorted(Collections.reverseOrder())
                .collect(Collectors.toList());
    }
}
