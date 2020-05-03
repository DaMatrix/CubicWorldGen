package io.github.opencubicchunks.cubicchunks.cubicgen.asm.mixin.common;

import net.minecraft.block.BlockBeacon;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(BlockBeacon.class)
public abstract class MixinBlockBeacon {
    /**
     * God damn it Mojang
     *
     * @author DaPorkchop_
     */
    @Overwrite
    public static void updateColorAsync(final World worldIn, final BlockPos glassPos) {
    }
}
