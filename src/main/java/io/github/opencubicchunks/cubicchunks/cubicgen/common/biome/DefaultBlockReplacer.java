package io.github.opencubicchunks.cubicchunks.cubicgen.common.biome;

import com.google.common.collect.Sets;
import io.github.opencubicchunks.cubicchunks.cubicgen.CustomCubicMod;
import io.github.opencubicchunks.cubicchunks.cubicgen.customcubic.builder.IBuilder;
import net.minecraft.block.BlockSand;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.Set;

import static java.lang.Math.abs;

/**
 * Combination of all of the three default {@link TerrainShapeReplacer}s for performance. This allows avoiding tons of expensive interface method calls
 * and makes branch prediction a lot easier.
 *
 * @author DaPorkchop_
 */
public class DefaultBlockReplacer implements IBiomeBlockReplacer {
    protected static final IBlockState RED_SANDSTONE = Blocks.RED_SANDSTONE.getDefaultState();
    protected static final IBlockState SANDSTONE = Blocks.SANDSTONE.getDefaultState();

    private final IBlockState topBlock;
    private final IBlockState fillerBlock;
    private final IBlockState terrainFill;


    public DefaultBlockReplacer(IBlockState topBlock, IBlockState fillerBlock, IBlockState terrainFill) {
        this.topBlock = topBlock;
        this.fillerBlock = fillerBlock;
        this.terrainFill = terrainFill;
    }

    /**
     * Replaces any block with greater than 0 density with stone
     */
    @Override
    public IBlockState getReplacedBlock(IBlockState previousBlock, int x, int y, int z, double dx, double dy, double dz, double density) {
        if (density > 0) {
            if (density > 7 * abs(dy)) {
                return this.terrainFill;
            }

            double depth = 3.0d;
            double densityAdjusted = density / abs(dy);
            if (density + dy <= 0) { // if air above
                return this.top(depth);
            } else {
                if (dy < 0) {
                    double xzSize = Math.sqrt(dx * dx + dz * dz);
                    double depthAdjusted = depth + 1.0d - xzSize / dy;
                    if (densityAdjusted < depthAdjusted) {
                        return this.fillerBlock;
                    } else if (this.fillerBlock.getBlock() == Blocks.SAND && densityAdjusted < depthAdjusted + 3.0d) {
                        return this.fillerBlock.getValue(BlockSand.VARIANT) == BlockSand.EnumType.RED_SAND ? RED_SANDSTONE : SANDSTONE;
                    }
                }
            }
            return this.terrainFill;
        }
        return previousBlock;
    }

    private IBlockState top(double depth) {
        return depth > 0 ? this.topBlock : Blocks.AIR.getDefaultState();
    }

    public static IBiomeBlockReplacerProvider provider() {
        return new IBiomeBlockReplacerProvider() {
            private final ResourceLocation TERRAIN_FILL_BLOCK = CustomCubicMod.location("terrain_fill_block");

            @Override
            public IBiomeBlockReplacer create(World world, CubicBiome cubicBiome, BiomeBlockReplacerConfig conf) {
                Biome biome = cubicBiome.getBiome();
                IBlockState terrainFill = conf.getBlockstate(TERRAIN_FILL_BLOCK, Blocks.STONE.getDefaultState());
                return new DefaultBlockReplacer(biome.topBlock, biome.fillerBlock, terrainFill);
            }

            @Override public Set<ConfigOptionInfo> getPossibleConfigOptions() {
                return Sets.newHashSet(
                        new ConfigOptionInfo(TERRAIN_FILL_BLOCK, Blocks.STONE.getDefaultState())
                );
            }
        };
    }
}
