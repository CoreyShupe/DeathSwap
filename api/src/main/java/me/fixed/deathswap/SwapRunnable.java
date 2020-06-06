package me.fixed.deathswap;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SwapRunnable implements Runnable {
    @NotNull private final DeathSwapGame game;
    @NotNull private final DeathSwapGameManager<?> basicGameManager;
    @NotNull private final DeathSwapGameConfig config;
    private final boolean autoRestart;
    private int tickIndex;
    private int swapIndex;

    protected SwapRunnable(@NotNull DeathSwapGame game) {
        this(game, true);
    }

    protected SwapRunnable(@NotNull DeathSwapGame game, boolean autoRestart) {
        this.game = game;
        this.basicGameManager = game.getGameManager();
        this.config = game.getConfig();
        this.autoRestart = autoRestart;
        this.swapIndex = 0;
    }

    protected void start() {
        if (swapIndex >= config.getMaxSwapTimes()) {
            game.finish();
            basicGameManager.noWinCondition();
            return;
        }
        this.swapIndex++;
        this.tickIndex = 0;
        this.basicGameManager.chainTask(this, getNextTickTimeAndIncrement());
    }

    @Override public void run() {
        if (tickIndex == config.getTickSwitchNotifications().size() + 1) {
            String message = swapIndex >= config.getMaxSwapTimes() ? config.getGameEndNotifyMessage() : config.getSwapNotifyMessage();
            if (!config.getSwapNotifyMessage().isEmpty()) {
                basicGameManager.sendGameMessage(message);
            }
            basicGameManager.swapPositions();
            if (autoRestart) {
                start();
            }
        } else {
            String message = swapIndex >= config.getMaxSwapTimes() ? config.getEndTimeNotifyMessageFormat() : config.getTimeNotifyMessageFormat();
            if (!message.isEmpty()) {
                long timeLeft = config.getTickSwitchNotifications().get(tickIndex - 1);
                basicGameManager.sendGameMessage(message.replace(
                        DeathSwapGameConfig.TIME_PLACEHOLDER, String.valueOf(timeLeft / DeathSwapGameConfig.TICKS_PER_SECOND))
                );
            }
            basicGameManager.chainTask(this, getNextTickTimeAndIncrement());
        }
    }

    protected long getNextTickTimeAndIncrement() {
        long time = getNextTickTime();
        this.tickIndex++;
        return time;
    }

    public long getNextTickTime() {
        List<Long> tickIntervals = config.getTickSwitchNotifications();
        if (tickIntervals.size() == 0) {
            return config.getTicksBetweenSwap();
        } else if (tickIndex == 0) {
            return config.getTicksBetweenSwap() - tickIntervals.get(0);
        } else if (tickIndex == tickIntervals.size()) {
            return tickIntervals.get(tickIndex - 1);
        } else {
            return tickIntervals.get(tickIndex - 1) - tickIntervals.get(tickIndex);
        }
    }
}
