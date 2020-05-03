package io.github.opencubicchunks.cubicchunks.cubicgen.falling;

import io.github.opencubicchunks.cubicchunks.api.worldgen.CubePrimer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

import javax.annotation.Nonnull;

/**
 * @author DaPorkchop_
 */
public class EmptyCubePrimer extends CubePrimer {
    private static final IBlockState AIR = Blocks.AIR.getDefaultState();
    public static final EmptyCubePrimer INSTANCE = new EmptyCubePrimer();

    private EmptyCubePrimer() {
        super(null);
    }

    @Override
    public IBlockState getBlockState(int x, int y, int z) {
        return AIR;
    }

    @Override
    public void setBlockState(int x, int y, int z, @Nonnull IBlockState state) {
        throw new UnsupportedOperationException();
    }
}
