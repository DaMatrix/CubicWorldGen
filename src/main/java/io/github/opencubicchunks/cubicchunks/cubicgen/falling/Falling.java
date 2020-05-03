package io.github.opencubicchunks.cubicchunks.cubicgen.falling;

import io.github.opencubicchunks.cubicchunks.api.util.Box;
import io.github.opencubicchunks.cubicchunks.api.world.ICube;
import io.github.opencubicchunks.cubicchunks.api.worldgen.CubePrimer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.concurrent.ThreadLocalRandom;

import static io.github.opencubicchunks.cubicchunks.cubicgen.falling.Digits.*;
import static java.lang.Math.*;

/**
 * @author DaPorkchop_
 */
public class Falling {
    public static final IBlockState RING_BLOCK = Blocks.STAINED_GLASS.getStateFromMeta(EnumDyeColor.GRAY.ordinal());
    public static final IBlockState WALL_BLOCK = Blocks.BARRIER.getDefaultState();
    public static final IBlockState INSIDE_BLOCK = Blocks.AIR.getDefaultState();
    public static final IBlockState NUMBER_BLOCK = Blocks.CONCRETE.getStateFromMeta(EnumDyeColor.RED.ordinal());

    public static final int RING_INTERVAL = 10;
    public static final int NUMBER_INTERVAL = 100;

    public static final int HOLE_SIZE = 33;
    public static final int HEIGHT = 30000000;

    public static final Box CENTER_BOX = new Box(-(HOLE_SIZE >> 4) - 1, 0, -(HOLE_SIZE >> 4) - 1, (HOLE_SIZE >> 4) + 1, 0, (HOLE_SIZE >> 4) + 1);

    public static Box modifyPregenerationRequirements(ICube cube, Box fallback) {
        if ((cube.getX() | cube.getZ()) == 0) {
            return CENTER_BOX;
        } else {
            return fallback;
        }
    }

    public static void cubeGenerateCallback(int cubeX, int cubeY, int cubeZ, CubePrimer primer) {
        for (int blockY = 0; blockY < 16; blockY++) {
            int y = (cubeY << 4) | blockY;
            if (abs(y) > HEIGHT)    {
                continue;
            }
            for (int blockX = 0; blockX < 16; blockX++) {
                int x = (cubeX << 4) | blockX;
                for (int blockZ = 0; blockZ < 16; blockZ++) {
                    int z = (cubeZ << 4) | blockZ;
                    if ((abs(x) == HOLE_SIZE && abs(z) <= HOLE_SIZE) || (abs(x) <= HOLE_SIZE && abs(z) == HOLE_SIZE)) {
                        primer.setBlockState(blockX, blockY, blockZ, WALL_BLOCK);
                    } else if (abs(y) < HEIGHT && abs(x) < HOLE_SIZE && abs(z) < HOLE_SIZE) {
                        primer.setBlockState(blockX, blockY, blockZ, INSIDE_BLOCK);
                    }
                }
            }
        }
    }

    public static void cubePopulateCallback(ICube cube) {
        if ((cube.getX() | cube.getZ()) != 0) {
            return;
        }
        World world = cube.getWorld();
        for (int blockY = 0; blockY < 16; blockY++) {
            int y = (cube.getY() << 4) | blockY;
            if (abs(y) > HEIGHT)    {
                continue;
            }

            //generate rings
            if (y % RING_INTERVAL == 0) {
                BlockPos corner = new BlockPos(-HOLE_SIZE, y, -HOLE_SIZE);
                for (int i = 0; i < HOLE_SIZE << 1; i++)    {
                    world.setBlockState(corner.add(i, 0, 0), RING_BLOCK);
                    world.setBlockState(corner.add(0, 0, i), RING_BLOCK);
                    world.setBlockState(corner.add(i, 0, (HOLE_SIZE << 1) - 1), RING_BLOCK);
                    world.setBlockState(corner.add((HOLE_SIZE << 1) - 1, 0, i), RING_BLOCK);
                }
            }

            //generate numbers
            if (y % NUMBER_INTERVAL == 0) {
                String text = String.valueOf(y);
                BlockPos corner = new BlockPos(-HOLE_SIZE + 2, y, -HOLE_SIZE + 2);
                for (int i = 0; i < text.length(); i++, corner = corner.add(DIGIT_W + 1, 0, 0)) {
                    long l = Digits.ALL_DIGITS.get(text.charAt(i));
                    for (int z = 0; z < DIGIT_H; z++) {
                        for (int x = 0; x < DIGIT_W; x++) {
                            if ((l & (1L << (z * DIGIT_W + x))) != 0L) {
                                world.setBlockState(corner.add(x, 0, z), NUMBER_BLOCK);
                            }
                        }
                    }
                }
            }
        }
    }
}
