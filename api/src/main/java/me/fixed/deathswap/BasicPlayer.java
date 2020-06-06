package me.fixed.deathswap;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface BasicPlayer {
    UUID getUniqueId();

    void sendMessage(@NotNull String message);
}
