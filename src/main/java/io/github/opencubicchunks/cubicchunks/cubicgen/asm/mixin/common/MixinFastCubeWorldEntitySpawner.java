package io.github.opencubicchunks.cubicchunks.cubicgen.asm.mixin.common;

import io.github.opencubicchunks.cubicchunks.core.world.FastCubeWorldEntitySpawner;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Random;

@Mixin(FastCubeWorldEntitySpawner.class)
public class MixinFastCubeWorldEntitySpawner {
    /**
     * @author daporkchop
     */
    /*@Overwrite
    public int findChunksForSpawning(WorldServer world, boolean hostileEnable, boolean peacefulEnable, boolean spawnOnSetTickRate) {
        return 0;
    }*/

    /**
     * @author daporkchop
     */
    /*@Overwrite
    private int spawnCreatureTypeInAllChunks(EnumCreatureType mobType, WorldServer world) {
        return 0;
    }*/

    /**
     * @author daporkchop
     */
    /*@Overwrite
    public static void initialWorldGenSpawn(World world, Biome biome, int blockX, int blockY, int blockZ, int sizeX, int sizeY, int sizeZ, Random random) {
    }*/
}
