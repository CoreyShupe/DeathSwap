package me.fixed.deathswap;

import org.jetbrains.annotations.NotNull;

public interface DeathSwapGameManager<T extends BasicPlayer> {
    void swapPositions();

    BasicPlayer getPlayer1();

    BasicPlayer getPlayer2();

    default void sendGameMessage(@NotNull String message) {
        getPlayer1().sendMessage(message);
        getPlayer2().sendMessage(message);
    }

    void chainTask(@NotNull Runnable runnable, long ticksTillNextTask);

    void cancelChainedTask();

    void winCondition(@NotNull BasicPlayer player);

    void noWinCondition();

    void setupPlayers();

    void revertPlayers();
}
