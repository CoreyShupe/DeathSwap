package me.fixed.deathswap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class SwapRunnableTest {
    private DeathSwapGameManager<?> gameManager;

    @Before public void setUp() {
        BasicPlayer player1 = mock(BasicPlayer.class);
        BasicPlayer player2 = mock(BasicPlayer.class);
        this.gameManager = mock(DeathSwapGameManager.class);
        when(this.gameManager.getPlayer1()).thenReturn(player1);
        when(this.gameManager.getPlayer2()).thenReturn(player2);
        doAnswer((hook) -> {
            Runnable arg1 = hook.getArgumentAt(0, Runnable.class);
            arg1.run();
            return null;
        }).when(this.gameManager).chainTask(any(Runnable.class), any(Long.class));
        doNothing().when(this.gameManager).swapPositions();
    }

    @Test public void testChainCallNoIntervals() {
        SwapRunnable runnable = helperCreateSwapRunnable();
        runnable.start();
        verify(this.gameManager, times(1)).chainTask(eq(runnable), any(Long.class));
        verify(this.gameManager, times(1)).swapPositions();
    }

    @Test public void testChainCallSingleInterval() {
        SwapRunnable runnable = helperCreateSwapRunnableSingle();
        runnable.start();
        verify(this.gameManager, times(2)).chainTask(eq(runnable), any(Long.class));
        verify(this.gameManager, times(1)).swapPositions();
    }

    @Test public void testChainCallManyIntervals() {
        SwapRunnable runnable = helperCreateSwapRunnable(30, 20, 10, 5, 3);
        runnable.start();
        verify(this.gameManager, times(6)).chainTask(eq(runnable), any(Long.class));
        verify(this.gameManager, times(1)).swapPositions();
    }

    @Test public void testGetNextTickTimeEmptyList() {
        SwapRunnable runnable = helperCreateSwapRunnable();

        Assert.assertEquals(DeathSwapGameConfig.DEFAULT_TIME_BETWEEN_SWAP, runnable.getNextTickTimeAndIncrement());
    }

    @Test public void testGetNextTickTimeSingletonList() {
        SwapRunnable runnable = helperCreateSwapRunnableSingle();

        Assert.assertEquals(DeathSwapGameConfig.DEFAULT_TIME_BETWEEN_SWAP - (20 * 20), runnable.getNextTickTimeAndIncrement());
        Assert.assertEquals(20 * 20, runnable.getNextTickTimeAndIncrement());
    }

    @Test public void testGetNextTickTimeFullList() {
        SwapRunnable runnable = helperCreateSwapRunnable(30, 20, 10, 5, 3);

        Assert.assertEquals(DeathSwapGameConfig.DEFAULT_TIME_BETWEEN_SWAP - (30 * 20), runnable.getNextTickTimeAndIncrement());
        Assert.assertEquals((30 * 20) - (20 * 20), runnable.getNextTickTimeAndIncrement());
        Assert.assertEquals((20 * 20) - (10 * 20), runnable.getNextTickTimeAndIncrement());
        Assert.assertEquals((10 * 20) - (5 * 20), runnable.getNextTickTimeAndIncrement());
        Assert.assertEquals((5 * 20) - (3 * 20), runnable.getNextTickTimeAndIncrement());
        Assert.assertEquals(3 * 20, runnable.getNextTickTimeAndIncrement());
    }

    private SwapRunnable helperCreateSwapRunnable() {
        return new SwapRunnable(helperCreateGame(DeathSwapGameConfig.builder().build()), false);
    }

    private SwapRunnable helperCreateSwapRunnableSingle() {
        return new SwapRunnable(helperCreateGame(DeathSwapGameConfig.builder()
                .tickSwitchNotifications(DeathSwapGameConfig.generateList(20))
                .build()), false);
    }

    private SwapRunnable helperCreateSwapRunnable(Number... numbers) {
        return new SwapRunnable(helperCreateGame(DeathSwapGameConfig.builder()
                .tickSwitchNotifications(DeathSwapGameConfig.generateList(numbers))
                .build()), false);
    }

    private DeathSwapGame helperCreateGame(DeathSwapGameConfig config) {
        return new DeathSwapGame(gameManager, config);
    }
}
