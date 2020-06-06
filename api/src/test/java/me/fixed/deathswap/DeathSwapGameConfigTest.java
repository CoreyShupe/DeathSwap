package me.fixed.deathswap;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class DeathSwapGameConfigTest {
    @Test public void testSortOrder() {
        Assert.assertArrayEquals(DeathSwapGameConfig.generateList(20, 30, 10).toArray(), Arrays.asList(600L, 400L, 200L).toArray());
    }
}
