package me.fixed.deathswap;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class DeathSwapGame {
    @Getter @NotNull private final DeathSwapGameManager<?> gameManager;
    @Getter @NotNull private final DeathSwapGameConfig config;

    public DeathSwapGame(@NotNull DeathSwapGameManager<?> gameManager, @NotNull DeathSwapGameConfig config) {
        this.gameManager = gameManager;
        this.config = config;
    }

    public void startGame() {
        gameManager.setupPlayers();
        new SwapRunnable(this).start();
    }

    public void registerDeath(@NotNull UUID id) {
        if (id.equals(gameManager.getPlayer1().getUniqueId())) {
            finish();
            gameManager.winCondition(gameManager.getPlayer2());
        } else if (id.equals(gameManager.getPlayer2().getUniqueId())) {
            finish();
            gameManager.winCondition(gameManager.getPlayer1());
        }
    }

    public void finish() {
        gameManager.revertPlayers();
        gameManager.cancelChainedTask();
    }
}
